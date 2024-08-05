package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenecomponents;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenecomponents.vo.AppCourseSceneComponentsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenecomponents.vo.AppCourseSceneComponentsRespVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenecomponents.vo.AppCourseSceneComponentsSaveReqVO;
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

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenecomponents.CourseSceneComponentsDO;
import cn.iocoder.yudao.module.digitalcourse.service.coursescenecomponents.CourseSceneComponentsService;

@Tag(name = "用户 APP - 存储每个场景中的组件信息，包括PPT、数字人等")
@RestController
@RequestMapping("/digitalcourse/course-scene-components")
@Validated
public class AppCourseSceneComponentsController {

    @Resource
    private CourseSceneComponentsService courseSceneComponentsService;

    @PostMapping("/create")
    @Operation(summary = "创建存储每个场景中的组件信息，包括PPT、数字人等")
    public CommonResult<Long> createCourseSceneComponents(@Valid @RequestBody AppCourseSceneComponentsSaveReqVO createReqVO) {
        return success(courseSceneComponentsService.createCourseSceneComponents(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新存储每个场景中的组件信息，包括PPT、数字人等")
    public CommonResult<Boolean> updateCourseSceneComponents(@Valid @RequestBody AppCourseSceneComponentsSaveReqVO updateReqVO) {
        courseSceneComponentsService.updateCourseSceneComponents(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除存储每个场景中的组件信息，包括PPT、数字人等")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCourseSceneComponents(@RequestParam("id") Long id) {
        courseSceneComponentsService.deleteCourseSceneComponents(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得存储每个场景中的组件信息，包括PPT、数字人等")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppCourseSceneComponentsRespVO> getCourseSceneComponents(@RequestParam("id") Long id) {
        CourseSceneComponentsDO courseSceneComponents = courseSceneComponentsService.getCourseSceneComponents(id);
        return success(BeanUtils.toBean(courseSceneComponents, AppCourseSceneComponentsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得存储每个场景中的组件信息，包括PPT、数字人等分页")
    public CommonResult<PageResult<AppCourseSceneComponentsRespVO>> getCourseSceneComponentsPage(@Valid AppCourseSceneComponentsPageReqVO pageReqVO) {
        PageResult<CourseSceneComponentsDO> pageResult = courseSceneComponentsService.getCourseSceneComponentsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppCourseSceneComponentsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出存储每个场景中的组件信息，包括PPT、数字人等 Excel")
    public void exportCourseSceneComponentsExcel(@Valid AppCourseSceneComponentsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CourseSceneComponentsDO> list = courseSceneComponentsService.getCourseSceneComponentsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "存储每个场景中的组件信息，包括PPT、数字人等.xls", "数据", AppCourseSceneComponentsRespVO.class,
                        BeanUtils.toBean(list, AppCourseSceneComponentsRespVO.class));
    }

}