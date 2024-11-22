package cn.iocoder.yudao.module.digitalcourse.util;



import cn.hutool.json.JSONObject;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courseppts.CoursePptsDO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.pptmaterials.PptMaterialsDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.courseppts.CoursePptsMapper;
import cn.iocoder.yudao.module.digitalcourse.service.pptmaterials.PptMaterialsService;
import cn.iocoder.yudao.module.infra.api.config.ConfigApi;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author fmi110
 * @description ppt 转图片；图片转pdf 工具
 * @date 2021/8/19 20:16
 */

@Component
@Slf4j
public class PPTUtil {


    private static final String EASEGEN_CORE_PPT_ANALYSIS_URL = "easegen.core.ppt.analysis.url";
    private static final String ANALYSIS_PPT_COUNT_KEY = "analysis_ppt_count:";
    private static final String ANALYSIS_PPT_KEY = "analysis_ppt:";
     @Resource
    private FileApi fileApi;
    @Resource
    private ConfigApi configApi;
     @Resource
     private PptMaterialsService pptMaterialsService;
    @Resource
    private CoursePptsMapper coursePptsMapper;
    @Resource
    private StringRedisTemplate redisCache;



    private static String getTimeStamp(){
            return String.valueOf(System.currentTimeMillis());
    }

    @Async
    public void analysisPpt(String file,Long pptId){
        Boolean b = false;
        try {
            log.info("[analysisPpt][开始] pptId:{}, file:{}", pptId, file);
            ArrayList<JSONObject> data = analysisPptLocal(file, pptId);
            log.info("[analysisPpt][解析完成] pptId:{}, 页数:{}", pptId, data.size());
            List<PptMaterialsDO> list = new ArrayList<>();
            log.info("解析ppt数据："+data);

            CoursePptsDO coursePptsDO = coursePptsMapper.selectById(pptId);
            coursePptsDO.setPageSize(data.size());
            coursePptsMapper.updateById(coursePptsDO);

            for (int i = 0; i < data.size(); i++) {
                PptMaterialsDO pptMaterialsDO = new PptMaterialsDO();
                JSONObject obj = BeanUtils.toBean(data.get(i), JSONObject.class);
                String picFile = obj.getStr("file");
                try {
//                    byte[] bytes = Files.readAllBytes(Path.of(picFile));
//                    String picPath = savePicture(bytes, getTimeStamp());
                    pptMaterialsDO.setPptId(pptId);
                    pptMaterialsDO.setName(picFile);
                    pptMaterialsDO.setPictureUrl(picFile);
                    pptMaterialsDO.setOriginalUrl(picFile);
                    pptMaterialsDO.setIndexNo(i);
                    pptMaterialsDO.setBackgroundType(1);
                    pptMaterialsDO.setPptRemark(String.valueOf(obj.get("text")));
                    list.add(pptMaterialsDO);
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }

            b = pptMaterialsService.batchInsert(list);
            log.info("[analysisPpt][保存完成] pptId:{}, 结果:{}", pptId, b);
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            CoursePptsDO coursePptsDO = new CoursePptsDO();
            coursePptsDO.setStatus(b ? 0 : 1);
            coursePptsDO.setId(pptId);
            coursePptsMapper.updateById(coursePptsDO);
        }
    }

    public ArrayList<JSONObject> analysisPptLocal(String fileUrl, Long pptId) {
        ArrayList<JSONObject> picList = new ArrayList<>();
        ExecutorService executorService = null;

        try {
            // 更新进度为开始
            redisCache.opsForValue().set(ANALYSIS_PPT_KEY + pptId, "0", 1, TimeUnit.DAYS);
            // 下载PPT文件
            log.info("[analysisPptLocal][开始下载] pptId:{}, url:{}", pptId, fileUrl);
            URL url = new URL(fileUrl);
            InputStream inputStream = url.openStream();
            String tempFileName = UUID.randomUUID().toString();
            File pptFile = File.createTempFile("downloaded_ppt_"+tempFileName, ".pptx");
            try (FileOutputStream outputStream = new FileOutputStream(pptFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            XMLSlideShow ppt = new XMLSlideShow();
            // 重新打开文件输入流来创建 XMLSlideShow
            try (FileInputStream fileInputStream = new FileInputStream(pptFile)) {
                ppt = new XMLSlideShow(fileInputStream);
            }



            // 使用LibreOffice将PPT转换为PDF
            log.info("[analysisPptLocal][开始转换PDF] pptId:{}", pptId);
            File pdfFile = convertPptToPdf(pptFile);
            log.info("[analysisPptLocal][转换完成] pptId:{}", pptId);

            if (!isValidPdf(pdfFile)) {
                log.error("[analysisPptLocal][PDF无效] pptId:{}", pptId);
                redisCache.opsForValue().set(ANALYSIS_PPT_KEY + pptId, "-1", 1, TimeUnit.DAYS);
                throw new RuntimeException("生成的PDF文件无效");
            }
            executorService = Executors.newFixedThreadPool(1);
            // 将PDF转换为图片
            PDDocument document = null;
            try {
                log.info("[analysisPptLocal][PDF加载] pptId:{}", pptId);
                document = PDDocument.load(pdfFile);
                log.info("[analysisPptLocal][PDF加载完成] pptId:{}", pptId);
            } catch (IOException e) {
                throw new RuntimeException("PDF加载失败: " + e.getMessage(), e);
            }

            try {
                int pageCount = document.getNumberOfPages();
                log.info("[analysisPptLocal][开始处理页面] pptId:{}, 总页数:{}", pptId, pageCount);

                // 保存PPT总页数到Redis
                log.info("PPT总页数：" + pageCount);
                redisCache.opsForValue().set(ANALYSIS_PPT_COUNT_KEY + pptId, String.valueOf(pageCount), 1, TimeUnit.DAYS);

                List<Future<JSONObject>> futures = new ArrayList<>();
                for (int i = 0; i < pageCount; i++) {
                    int pageIndex = i;
                    PDDocument splitDoc = new PDDocument();
                    splitDoc.addPage(document.getPage(pageIndex));
                    XMLSlideShow finalPpt = ppt;
                    Future<JSONObject> future = executorService.submit(() -> processPdfPage(splitDoc, pageIndex, pptId, pageCount, finalPpt));
                    futures.add(future);
                }

                // 等待所有任务完成并收集结果
                for (Future<JSONObject> future : futures) {
                    try {
                        picList.add(future.get());
                    } catch (ExecutionException ee) {
                        log.error("处理PDF页面时发生错误: " + ee.getMessage(), ee);
                    }
                }

                // 更新进度为完成
                redisCache.opsForValue().set(ANALYSIS_PPT_KEY + pptId, "1");
            } finally {
                if (document != null) {
                    document.close();
                }
            }

            inputStream.close();
            pdfFile.delete();
            pptFile.delete();
        } catch (Exception e) {
            // 所有异常都更新Redis进度为-1
            redisCache.opsForValue().set(ANALYSIS_PPT_KEY + pptId, "-1", 1, TimeUnit.DAYS);
            log.error("[analysisPptLocal][异常] pptId:{}, 错误:{}", pptId, e.getMessage(), e);
            throw new RuntimeException("PPT解析失败: " + e.getMessage(), e);
        } finally {
            executorService.shutdown();
        }
        return picList;
    }

    private File convertPptToPdf(File pptFile) throws IOException, InterruptedException {
        log.info("[convertPptToPdf][开始] pptFile:{}", pptFile.getName());
        String tempFileName = UUID.randomUUID().toString();
        File pdfFile = File.createTempFile("ppt_to_pdf_"+tempFileName, ".pdf");
        String command;
        if (isWindows()) {
            command = String.format("\"C:\\Program Files\\LibreOffice\\program\\soffice.com\" --headless --convert-to pdf --outdir %s %s", pdfFile.getParent(), pptFile.getAbsolutePath());
        } else {
            command = String.format("libreoffice --headless --convert-to pdf --outdir %s %s", pdfFile.getParent(), pptFile.getAbsolutePath());
        }
        log.info("[convertPptToPdf][执行命令] command:{}", command);
        Process process = Runtime.getRuntime().exec(command);
        try (BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = stdError.readLine()) != null) {
                log.error("LibreOffice 错误信息: {}", line);
            }
        }
        if (process.waitFor() != 0) {
            throw new IOException("LibreOffice 转换失败，退出码: " + process.exitValue());
        }
        log.info("[convertPptToPdf][转换成功] pptFile:{}", pptFile.getName());
        return new File(pdfFile.getParent(), pptFile.getName().replaceFirst(".pptx?$", ".pdf"));

    }


    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private boolean isValidPdf(File pdfFile) {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            return document.getNumberOfPages() > 0;
        } catch (IOException e) {
            log.error("验证PDF文件失败: {}, 原因: {}", pdfFile.getAbsolutePath(), e.getMessage(), e);
            return false;
        }
    }

    private String getPptNotes(int pageIndex, XMLSlideShow ppt) {
        String text = "";
        try {
            XSLFSlide slide = getSlideByIndex(pageIndex, ppt);
            if (slide != null) {

                // 检查是否存在 NotesSlide，并避免重复创建
                XSLFNotes notesSlide = null;
                try {
                    notesSlide = slide.getSlideShow().getNotesSlide(slide);
                } catch (Exception e) {
                    // NotesSlide 不存在或无法获取，可能是个合法情况
                }

                if (notesSlide != null) {
                    XSLFTextShape[] placeholders = notesSlide.getPlaceholders();
                    if (placeholders != null && placeholders.length > 1) {
                        String notesText = placeholders[1].getText().trim();
                        if (!isPlaceholderText(notesText)) {
                            text = notesText;
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取PPT备注时发生错误，页面索引: " + pageIndex, e);
        }
        return text;
    }


    // 用于判断是否为占位符文本的方法
    private boolean isPlaceholderText(String text) {
        String[] placeholderTexts = {
                "Click to edit Master text styles",
                "Second level",
                "Third level",
                "Fourth level",
                "Fifth level",
                "Click to add title",
                "Click to add text",
                "Click to add subtitle"
        };
        for (String placeholder : placeholderTexts) {
            if (text.contains(placeholder)) {
                return true;
            }
        }
        return false;
    }

    private XSLFSlide getSlideByIndex(int index, XMLSlideShow ppt) {
        try {
            // 验证索引有效性
            if (ppt == null) {
                throw new IllegalStateException("PPT文件未正确初始化");
            }
            if (index < 0 || index >= ppt.getSlides().size()) {
                throw new IllegalArgumentException("Invalid slide index: " + index);
            }

            // 返回对应的slide
            return ppt.getSlides().get(index);
        } catch (IllegalArgumentException e) {
            log.error("非法的幻灯片索引: " + index, e);
        } catch (IllegalStateException e) {
            log.error("PPT文件未正确初始化", e);
        }
        return null; // 如果出错，返回null表示无法找到对应slide
    }

    private String extractTextFromPdfPage(PDDocument document, int startIndex) {
        PDDocument tempdoc = new PDDocument();
        try {
            tempdoc.addPage(document.getPage(0));
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setStartPage(1);
            pdfStripper.setSortByPosition(true); // 尝试启用按位置排序，避免字体问题导致的解析错误
            return pdfStripper.getText(tempdoc).trim();
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error("提取PDF页面时发生数组越界错误，页面范围: " + startIndex , e);
            return "";
        } catch (IOException e) {
            log.error("无法提取PDF页面的文字，页面范围: " + startIndex , e);
            return "";
        } catch (Exception e) {
            log.error("提取PDF页面时发生未知错误，页面范围: " + startIndex , e);
            return "";
        } finally {
            try {
                if (tempdoc != null) {
                    tempdoc.close();
                }
            } catch (IOException e) {
                log.error("关闭PDF文档时发生错误", e);
            }
        }
    }


    private JSONObject processPdfPage(PDDocument document, int pageIndex, Long pptId, int count, XMLSlideShow ppt) throws IOException {
        String text = "";
        // 尝试获取PPT备注
        String notesText = getPptNotes(pageIndex, ppt);
        if (!notesText.isEmpty()) {
            text = notesText;
        } else {
            // 如果备注不存在，提取PDF页面的文字
            text = extractTextFromPdfPage(document, pageIndex);
        }
        try {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 150);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            // 保存图片到文件系统或云存储
            String s3FilePath = savePicture(imageBytes, pageIndex + "-" + pptId + ".jpg");

            // 保存文件和备注内容到 JSON
            JSONObject entries = new JSONObject();
            entries.put("file", s3FilePath);
            entries.put("text", text);

            // 更新进度
            redisCache.opsForValue().set(ANALYSIS_PPT_KEY + pptId, String.valueOf((double) (pageIndex + 1) / count));

            return entries;
        } catch (IOException e) {
            log.error("渲染PDF页面失败，页面索引: " + pageIndex, e);
            throw e;
        }
    }
    private String savePicture(final byte[] data, final String fileName) throws IOException {
        String file = fileApi.createFile(data);
        return file;
    }




}