package cn.iocoder.yudao.module.digitalcourse.controller.admin.courses;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesRespVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesSaveReqVO;
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


import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courses.CoursesDO;
import cn.iocoder.yudao.module.digitalcourse.service.courses.CoursesService;

@Tag(name = "用户 APP - 存储课程的基本信息，包括课程名称、时长、状态等")
@RestController
@RequestMapping("/digitalcourse/courses")
@Validated
public class AppCoursesController {

    @Resource
    private CoursesService coursesService;

    @PostMapping("/create")
    @Operation(summary = "创建存储课程的基本信息，包括课程名称、时长、状态等")
    public CommonResult<Long> createCourses(@Valid @RequestBody AppCoursesSaveReqVO createReqVO) {
        return success(coursesService.createCourses(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新存储课程的基本信息，包括课程名称、时长、状态等")
    public CommonResult<Boolean> updateCourses(@Valid @RequestBody AppCoursesSaveReqVO updateReqVO) {
        coursesService.updateCourses(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除存储课程的基本信息，包括课程名称、时长、状态等")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCourses(@RequestParam("id") Long id) {
        coursesService.deleteCourses(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得存储课程的基本信息，包括课程名称、时长、状态等")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppCoursesRespVO> getCourses(@RequestParam("id") Long id) {
        CoursesDO courses = coursesService.getCourses(id);
        return success(BeanUtils.toBean(courses, AppCoursesRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得存储课程的基本信息，包括课程名称、时长、状态等分页")
    public CommonResult<PageResult<AppCoursesRespVO>> getCoursesPage(@Valid AppCoursesPageReqVO pageReqVO) {
        PageResult<CoursesDO> pageResult = coursesService.getCoursesPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppCoursesRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出存储课程的基本信息，包括课程名称、时长、状态等 Excel")
    public void exportCoursesExcel(@Valid AppCoursesPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CoursesDO> list = coursesService.getCoursesPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "存储课程的基本信息，包括课程名称、时长、状态等.xls", "数据", AppCoursesRespVO.class,
                        BeanUtils.toBean(list, AppCoursesRespVO.class));
    }

}