package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo.AppCourseSceneBackgroundsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo.AppCourseSceneBackgroundsRespVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo.AppCourseSceneBackgroundsSaveReqVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenebackgrounds.CourseSceneBackgroundsDO;
import cn.iocoder.yudao.module.digitalcourse.service.coursescenebackgrounds.CourseSceneBackgroundsService;

@Tag(name = "用户 APP - 存储每个场景的背景信息")
@RestController
@RequestMapping("/digitalcourse/course-scene-backgrounds")
@Validated
public class AppCourseSceneBackgroundsController {

    @Resource
    private CourseSceneBackgroundsService courseSceneBackgroundsService;

    @PostMapping("/create")
    @Operation(summary = "创建存储每个场景的背景信息")
    public CommonResult<Long> createCourseSceneBackgrounds(@Valid @RequestBody AppCourseSceneBackgroundsSaveReqVO createReqVO) {
        return success(courseSceneBackgroundsService.createCourseSceneBackgrounds(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新存储每个场景的背景信息")
    public CommonResult<Boolean> updateCourseSceneBackgrounds(@Valid @RequestBody AppCourseSceneBackgroundsSaveReqVO updateReqVO) {
        courseSceneBackgroundsService.updateCourseSceneBackgrounds(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除存储每个场景的背景信息")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCourseSceneBackgrounds(@RequestParam("id") Long id) {
        courseSceneBackgroundsService.deleteCourseSceneBackgrounds(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得存储每个场景的背景信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppCourseSceneBackgroundsRespVO> getCourseSceneBackgrounds(@RequestParam("id") Long id) {
        CourseSceneBackgroundsDO courseSceneBackgrounds = courseSceneBackgroundsService.getCourseSceneBackgrounds(id);
        return success(BeanUtils.toBean(courseSceneBackgrounds, AppCourseSceneBackgroundsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得存储每个场景的背景信息分页")
    public CommonResult<PageResult<AppCourseSceneBackgroundsRespVO>> getCourseSceneBackgroundsPage(@Valid AppCourseSceneBackgroundsPageReqVO pageReqVO) {
        PageResult<CourseSceneBackgroundsDO> pageResult = courseSceneBackgroundsService.getCourseSceneBackgroundsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppCourseSceneBackgroundsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出存储每个场景的背景信息 Excel")
    public void exportCourseSceneBackgroundsExcel(@Valid AppCourseSceneBackgroundsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CourseSceneBackgroundsDO> list = courseSceneBackgroundsService.getCourseSceneBackgroundsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "存储每个场景的背景信息.xls", "数据", AppCourseSceneBackgroundsRespVO.class,
                        BeanUtils.toBean(list, AppCourseSceneBackgroundsRespVO.class));
    }

}