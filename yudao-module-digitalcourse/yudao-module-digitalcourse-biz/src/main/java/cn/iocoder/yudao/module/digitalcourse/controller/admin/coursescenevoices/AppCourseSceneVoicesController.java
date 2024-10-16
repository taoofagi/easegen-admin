package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo.AppCourseSceneVoicesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo.AppCourseSceneVoicesRespVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo.AppCourseSceneVoicesSaveReqVO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.annotation.Resource;
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


import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenevoices.CourseSceneVoicesDO;
import cn.iocoder.yudao.module.digitalcourse.service.coursescenevoices.CourseSceneVoicesService;

@Tag(name = "用户 APP - 存储每个场景中的声音信息")
@RestController
@RequestMapping("/digitalcourse/course-scene-voices")
@Validated
public class AppCourseSceneVoicesController {

    @Resource
    private CourseSceneVoicesService courseSceneVoicesService;

    @PostMapping("/create")
    @Operation(summary = "创建存储每个场景中的声音信息")
    public CommonResult<Long> createCourseSceneVoices(@Valid @RequestBody AppCourseSceneVoicesSaveReqVO createReqVO) {
        return success(courseSceneVoicesService.createCourseSceneVoices(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新存储每个场景中的声音信息")
    public CommonResult<Boolean> updateCourseSceneVoices(@Valid @RequestBody AppCourseSceneVoicesSaveReqVO updateReqVO) {
        courseSceneVoicesService.updateCourseSceneVoices(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除存储每个场景中的声音信息")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCourseSceneVoices(@RequestParam("id") Long id) {
        courseSceneVoicesService.deleteCourseSceneVoices(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得存储每个场景中的声音信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppCourseSceneVoicesRespVO> getCourseSceneVoices(@RequestParam("id") Long id) {
        CourseSceneVoicesDO courseSceneVoices = courseSceneVoicesService.getCourseSceneVoices(id);
        return success(BeanUtils.toBean(courseSceneVoices, AppCourseSceneVoicesRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得存储每个场景中的声音信息分页")
    public CommonResult<PageResult<AppCourseSceneVoicesRespVO>> getCourseSceneVoicesPage(@Valid AppCourseSceneVoicesPageReqVO pageReqVO) {
        PageResult<CourseSceneVoicesDO> pageResult = courseSceneVoicesService.getCourseSceneVoicesPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppCourseSceneVoicesRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出存储每个场景中的声音信息 Excel")
    public void exportCourseSceneVoicesExcel(@Valid AppCourseSceneVoicesPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CourseSceneVoicesDO> list = courseSceneVoicesService.getCourseSceneVoicesPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "存储每个场景中的声音信息.xls", "数据", AppCourseSceneVoicesRespVO.class,
                        BeanUtils.toBean(list, AppCourseSceneVoicesRespVO.class));
    }

}