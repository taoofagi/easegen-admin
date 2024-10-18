package cn.iocoder.yudao.module.digitalcourse.util;



import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
            log.info("开始解析ppt,pptId:"+pptId+",file:"+file);
//            String path = file.substring(file.lastIndexOf("/")+1);
//            byte[] fileContent = fileApi.getFileContent(4L, path);
//            Path tempFilePath = Files.createTempFile(path,".pptx");
//            File tempFile = tempFilePath.toFile();
//            tempFile.deleteOnExit();
//            try (InputStream inputStream = new ByteArrayInputStream(fileContent);
//                 java.io.FileOutputStream outputStream = new java.io.FileOutputStream(tempFile)) {
//                byte[] buffer = new byte[1024];
//                int len;
//                while ((len = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, len);
//                }
//            }
            //直接传文件url
            HashMap<String, Object> param = new HashMap<>();
            param.put("file",file);
            param.put("pptId",pptId);
//            String apiUrl = configApi.getConfigValueByKey(EASEGEN_CORE_PPT_ANALYSIS_URL) + "/admin-api/digitalcourse/course-ppts/analysisPpt";
//            String apiUrl = "http://127.0.0.1:7962" + "/admin-api/digitalcourse/course-ppts/analysisPpt";
//            String body = HttpRequest.post(apiUrl)
//                    .form("file", file)
//                    .form("pptId", pptId)
//                    .execute().body();
//            JSONObject entries = JSONUtil.parseObj(body);
//            JSONArray data = JSONUtil.parseArray(entries.getStr("data"));
            ArrayList<JSONObject> data = analysisPptLocal(file, pptId);
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
        ExecutorService executorService = Executors.newFixedThreadPool(5);  // 降低并发量，减轻负载

        try {
            // 下载PPT文件
            URL url = new URL(fileUrl);
            InputStream inputStream = url.openStream();
            //在高并发情况下，临时文件名称需要保持唯一
            String tempFileName = getTimeStamp();
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
            File pdfFile = convertPptToPdf(pptFile);

            if (!isValidPdf(pdfFile)) {
                throw new RuntimeException("生成的PDF文件无效");
            }

            // 将PDF转换为图片
            PDDocument document = null;
            try {
                document = PDDocument.load(pdfFile);
            } catch (IOException e) {
                throw new RuntimeException("PDF加载失败: " + e.getMessage(), e);
            }

            try {
                int pageCount = document.getNumberOfPages();

                // 保存PPT总页数到Redis
                log.info("PPT总页数：" + pageCount);
                redisCache.opsForValue().set(ANALYSIS_PPT_COUNT_KEY + pptId, String.valueOf(pageCount));

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
            throw new RuntimeException("PPT解析失败: " + e.getMessage(), e);
        } finally {
            executorService.shutdown();
        }
        return picList;
    }

    private File convertPptToPdf(File pptFile) throws IOException {
        String tempFileName = getTimeStamp();
        File pdfFile = File.createTempFile("ppt_to_pdf_"+tempFileName, ".pdf");
        String command;
        if (isWindows()) {
            command = String.format("\"C:\\Program Files\\LibreOffice\\program\\soffice.exe\" --headless --convert-to pdf --outdir %s %s", pdfFile.getParent(), pptFile.getAbsolutePath());
        } else {
            command = String.format("libreoffice --headless --convert-to pdf --outdir %s %s", pdfFile.getParent(), pptFile.getAbsolutePath());
        }
        Process process = Runtime.getRuntime().exec(command);
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("LibreOffice转换过程中被中断", e);
        }
        return new File(pdfFile.getParent(), pptFile.getName().replaceFirst(".pptx?$", ".pdf"));

    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private boolean isValidPdf(File pdfFile) {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            return document.getNumberOfPages() > 0;
        } catch (IOException e) {
            log.error("PDF文件无效: " + e.getMessage(), e);
            return false;
        }
    }

    private String getPptNotes(int pageIndex, XMLSlideShow ppt) {
        String text = "";
        try {
            XSLFSlide slide = getSlideByIndex(pageIndex, ppt);
            if (slide != null) {
                if (slide.getNotes() != null) {
                    XSLFNotes notes = slide.getNotes();
                    List<List<XSLFTextParagraph>> paragraphsList = notes.getTextParagraphs();
                    if (paragraphsList != null) {
                        StringBuilder notesText = new StringBuilder();
                        for (List<XSLFTextParagraph> paragraphs : paragraphsList) {
                            for (XSLFTextParagraph paragraph : paragraphs) {
                                notesText.append(paragraph.getText()).append("\n");
                            }
                        }
                        text = notesText.toString().trim();
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