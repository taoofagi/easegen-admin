package cn.iocoder.yudao.module.digitalcourse.service.courseppts;

import cn.hutool.json.JSONObject;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courseppts.vo.AppCoursePptsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courseppts.vo.AppCoursePptsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials.vo.AppPptMaterialsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.service.pptmaterials.PptMaterialsService;
import cn.iocoder.yudao.module.digitalcourse.util.PPTUtil;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.fonts.FontInfo;
import org.apache.poi.sl.draw.DrawFontManagerDefault;
import org.apache.poi.sl.draw.Drawable;
import org.apache.poi.sl.usermodel.Placeholder;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courseppts.CoursePptsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.courseppts.CoursePptsMapper;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 存储课程的PPT信息，包括文件名、文件大小、类型等 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CoursePptsServiceImpl implements CoursePptsService {

    private static final String ANALYSIS_PPT_KEY = "analysis_ppt:";

    @Resource
    private CoursePptsMapper coursePptsMapper;

   /* @Resource
    private RedisMQTemplate redisMQTemplate;*/

    @Resource
    private PPTUtil pptUtil;

    @Resource
    private FileApi fileApi;
    @Resource
    private StringRedisTemplate redisCache;
    @Resource
    private PptMaterialsService pptMaterialsService;

    @Override
    public Long createCoursePpts(AppCoursePptsSaveReqVO createReqVO) {
        // 插入
        CoursePptsDO coursePpts = BeanUtils.toBean(createReqVO, CoursePptsDO.class);
        coursePpts.setStatus(2);
        coursePptsMapper.insert(coursePpts);
        pptUtil.analysisPpt(coursePpts.getUrl(), coursePpts.getId());
        // 返回
        return coursePpts.getId();
    }

    @Override
    public void updateCoursePpts(AppCoursePptsSaveReqVO updateReqVO) {
        // 校验存在
        validateCoursePptsExists(updateReqVO.getId());
        // 更新
        CoursePptsDO updateObj = BeanUtils.toBean(updateReqVO, CoursePptsDO.class);
        coursePptsMapper.updateById(updateObj);
    }

    @Override
    public void deleteCoursePpts(Long id) {
        // 校验存在
        validateCoursePptsExists(id);
        // 删除
        coursePptsMapper.deleteById(id);
    }

    private void validateCoursePptsExists(Long id) {
        if (coursePptsMapper.selectById(id) == null) {
            throw exception(COURSE_PPTS_NOT_EXISTS);
        }
    }

    @Override
    public CoursePptsDO getCoursePpts(Long id) {
        return coursePptsMapper.selectById(id);
    }

    @Override
    public PageResult<CoursePptsDO> getCoursePptsPage(AppCoursePptsPageReqVO pageReqVO) {
        return coursePptsMapper.selectPage(pageReqVO);
    }

    @Override
    public CommonResult getSchedule(Long id) {
        String value = redisCache.opsForValue().get(ANALYSIS_PPT_KEY + id);
        if (StringUtils.isBlank(value)) {
            List<AppPptMaterialsSaveReqVO> appPptMaterialsSaveReqVOS = pptMaterialsService.selectListByPptId(id);
            if (appPptMaterialsSaveReqVOS.isEmpty()) return CommonResult.success(null);
            return CommonResult.success(appPptMaterialsSaveReqVOS);
        }
        if (Double.valueOf(value) == 1) {
            List<AppPptMaterialsSaveReqVO> appPptMaterialsSaveReqVOS = pptMaterialsService.selectListByPptId(id);
            redisCache.delete(ANALYSIS_PPT_KEY + id);
            return CommonResult.success(appPptMaterialsSaveReqVOS);
        }
        return CommonResult.success(value);
    }

    @Override
    public ResponseEntity<Object> mergePPTs(MultipartFile[] files) {
        if (files.length < 2) {
            return ResponseEntity.badRequest().body("At least two PPT files are required.".getBytes());
        }

        try {
            // 1. 使用第1个PPT作为基础文件（不建议创建空对象，然后从第1个开始合并操作）
            XMLSlideShow ppt = new XMLSlideShow(files[0].getInputStream());
            // 2. 从第2个文件开始遍历，合并
            for (int i = 1; i < files.length; i++) {
                XMLSlideShow src = new XMLSlideShow(files[i].getInputStream());
                // 遍历每张幻灯片
                for (XSLFSlide srcSlide : src.getSlides()) {
                    XSLFSlideMaster master = srcSlide.getSlideMaster();

                    // 检查目标文件是否已包含相同的母版
                    XSLFSlideMaster destMaster = null;
                    for (XSLFSlideMaster destMasterCandidate : ppt.getSlideMasters()) {
                        if (master.equals(destMasterCandidate)) {
                            destMaster = destMasterCandidate;
                            break;
                        }
                    }

                    // 如果目标文件中没有对应的母版，则复制源文件中的母版
                    if (destMaster == null) {
                        destMaster = master;
                        ppt.getSlideMasters().add(destMaster);
                    }

                    // 获取母版的第一个布局，默认使用第一个布局
                    XSLFSlideLayout destLayout = destMaster.getSlideLayouts()[0];
                    XSLFBackground bg = destLayout.getBackground();
                    // 合并
                    XSLFSlide destSlide = ppt.createSlide(destLayout);
                    destSlide.importContent(srcSlide);
                }
            }

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                ppt.write(out);
                String path = fileApi.createFile("PPT黑板模板合并.pptx", null, out.toByteArray());
                // 关闭资源
                out.close();
                return ResponseEntity.ok().body(path);
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error merging PPT files: " + e.getMessage()).getBytes());
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> convertPptToImages(MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        if (file.isEmpty()) {
            map.put("error", "No file provided");
            return ResponseEntity.badRequest().body(map);
        }

        try {
            // Save the uploaded file
            File pptFile = File.createTempFile("ppt_", ".pptx");
            file.transferTo(pptFile);

            // Convert PPT to images and extract notes
            Map<String, Object> result = convertPptToImagesAndNotes(pptFile);

            // Delete the temporary file
            pptFile.delete();

            return ResponseEntity.ok(result);
        } catch (IOException e) {
            map.put("error", e.getMessage());
            return ResponseEntity.status(500).body(map);
        }
    }

    @Override
    public Boolean cancelAnalysis(Long id) {
        return redisCache.delete(ANALYSIS_PPT_KEY + id);
    }

    private Map<String, Object> convertPptToImagesAndNotes(File pptFile) throws IOException {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> slidesData = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(pptFile);
             XMLSlideShow ppt = new XMLSlideShow(fis)) {

            Dimension pgsize = ppt.getPageSize();
            for (XSLFSlide slide : ppt.getSlides()) {
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();

                // 有些字体乱码识别不了，采用字体映射功能，将其映射为宋体
                graphics.setRenderingHint(Drawable.FONT_HANDLER, new DrawFontManagerDefault() {
                    public FontInfo getMappedFont(Graphics2D graphics, FontInfo fontInfo) {
                        try {
                            //把所有字体都映射成 宋体
                            fontInfo.setTypeface("宋体");
                        } catch (Exception e) {
                            //有一些字体是只读属性，会抛异常，忽略掉
                        }
                        return super.getMappedFont(graphics, fontInfo);
                    }
                });

                slide.draw(graphics);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(img, "png", out);
                String path = fileApi.createFile(out.toByteArray());

                // 获取ppt的备注信息
                XSLFNotes notes = slide.getNotes();
                String remark = "";
                if (notes != null) {
                    for (XSLFShape shape : notes) {
                        if (shape instanceof XSLFTextShape && Placeholder.BODY == ((XSLFTextShape) shape).getTextType()) {
                            XSLFTextShape txShape = (XSLFTextShape) shape;
                            remark = txShape.getText();
                            break;
                        }
                    }
                }

                // Store image path and notes
                Map<String, String> slideData = new HashMap<>();
                slideData.put("imagePath", path);
                slideData.put("remark", remark);

                slidesData.add(slideData);
            }
        }

        result.put("slides", slidesData);
        return result;
    }

}