package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo.AppCourseSceneTextsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo.AppCourseSceneTextsRespVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo.AppCourseSceneTextsSaveReqVO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenetexts.CourseSceneTextsDO;
import cn.iocoder.yudao.module.digitalcourse.service.coursescenetexts.CourseSceneTextsService;

@Tag(name = "用户 APP - 存储场景中的文本信息，包括文本内容、音调、速度等")
@RestController
@RequestMapping("/digitalcourse/course-scene-texts")
@Validated
public class AppCourseSceneTextsController {

    @Resource
    private CourseSceneTextsService courseSceneTextsService;

    @PostMapping("/create")
    @Operation(summary = "创建存储场景中的文本信息，包括文本内容、音调、速度等")
    public CommonResult<Long> createCourseSceneTexts(@Valid @RequestBody AppCourseSceneTextsSaveReqVO createReqVO) {
        return success(courseSceneTextsService.createCourseSceneTexts(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新存储场景中的文本信息，包括文本内容、音调、速度等")
    public CommonResult<Boolean> updateCourseSceneTexts(@Valid @RequestBody AppCourseSceneTextsSaveReqVO updateReqVO) {
        courseSceneTextsService.updateCourseSceneTexts(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除存储场景中的文本信息，包括文本内容、音调、速度等")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCourseSceneTexts(@RequestParam("id") Long id) {
        courseSceneTextsService.deleteCourseSceneTexts(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得存储场景中的文本信息，包括文本内容、音调、速度等")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppCourseSceneTextsRespVO> getCourseSceneTexts(@RequestParam("id") Long id) {
        CourseSceneTextsDO courseSceneTexts = courseSceneTextsService.getCourseSceneTexts(id);
        return success(BeanUtils.toBean(courseSceneTexts, AppCourseSceneTextsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得存储场景中的文本信息，包括文本内容、音调、速度等分页")
    public CommonResult<PageResult<AppCourseSceneTextsRespVO>> getCourseSceneTextsPage(@Valid AppCourseSceneTextsPageReqVO pageReqVO) {
        PageResult<CourseSceneTextsDO> pageResult = courseSceneTextsService.getCourseSceneTextsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppCourseSceneTextsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出存储场景中的文本信息，包括文本内容、音调、速度等 Excel")
    public void exportCourseSceneTextsExcel(@Valid AppCourseSceneTextsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CourseSceneTextsDO> list = courseSceneTextsService.getCourseSceneTextsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "存储场景中的文本信息，包括文本内容、音调、速度等.xls", "数据", AppCourseSceneTextsRespVO.class,
                        BeanUtils.toBean(list, AppCourseSceneTextsRespVO.class));
    }

}