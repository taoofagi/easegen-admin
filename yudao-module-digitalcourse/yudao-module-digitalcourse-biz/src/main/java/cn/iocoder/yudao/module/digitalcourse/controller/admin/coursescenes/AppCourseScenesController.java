package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo.AppCourseScenesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo.AppCourseScenesRespVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo.AppCourseScenesSaveReqVO;
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

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenes.CourseScenesDO;
import cn.iocoder.yudao.module.digitalcourse.service.coursescenes.CourseScenesService;

@Tag(name = "用户 APP - 存储课程的场景信息，包括背景、组件、声音等")
@RestController
@RequestMapping("/digitalcourse/course-scenes")
@Validated
public class AppCourseScenesController {

    @Resource
    private CourseScenesService courseScenesService;

    @PostMapping("/create")
    @Operation(summary = "创建存储课程的场景信息，包括背景、组件、声音等")
    public CommonResult<Long> createCourseScenes(@Valid @RequestBody AppCourseScenesSaveReqVO createReqVO) {
        return success(courseScenesService.createCourseScenes(createReqVO));
    }

    @PostMapping("/copy")
    @Operation(summary = "创建存储课程的场景信息，包括背景、组件、声音等")
    public CommonResult<Long> copy(@Valid @RequestBody AppCourseScenesSaveReqVO createReqVO) {
        if (createReqVO.getId() != null){
            createReqVO.setId(null);
        }
        if (createReqVO.getStatus() == null) createReqVO.setStatus(0);
        return success(courseScenesService.createCourseScenes(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新存储课程的场景信息，包括背景、组件、声音等")
    public CommonResult<Boolean> updateCourseScenes(@Valid @RequestBody AppCourseScenesSaveReqVO updateReqVO) {
        courseScenesService.updateCourseScenes(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除存储课程的场景信息，包括背景、组件、声音等")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCourseScenes(@RequestParam("id") Long id) {
        courseScenesService.deleteCourseScenes(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得存储课程的场景信息，包括背景、组件、声音等")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppCourseScenesRespVO> getCourseScenes(@RequestParam("id") Long id) {
        CourseScenesDO courseScenes = courseScenesService.getCourseScenes(id);
        return success(BeanUtils.toBean(courseScenes, AppCourseScenesRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得存储课程的场景信息，包括背景、组件、声音等分页")
    public CommonResult<PageResult<AppCourseScenesRespVO>> getCourseScenesPage(@Valid AppCourseScenesPageReqVO pageReqVO) {
        PageResult<CourseScenesDO> pageResult = courseScenesService.getCourseScenesPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppCourseScenesRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出存储课程的场景信息，包括背景、组件、声音等 Excel")
    public void exportCourseScenesExcel(@Valid AppCourseScenesPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CourseScenesDO> list = courseScenesService.getCourseScenesPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "存储课程的场景信息，包括背景、组件、声音等.xls", "数据", AppCourseScenesRespVO.class,
                        BeanUtils.toBean(list, AppCourseScenesRespVO.class));
    }

}