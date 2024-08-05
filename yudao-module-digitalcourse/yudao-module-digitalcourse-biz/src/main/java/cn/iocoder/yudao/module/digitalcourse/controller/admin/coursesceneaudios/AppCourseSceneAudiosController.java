package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios.vo.AppCourseSceneAudiosPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios.vo.AppCourseSceneAudiosRespVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios.vo.AppCourseSceneAudiosSaveReqVO;
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


import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursesceneaudios.CourseSceneAudiosDO;
import cn.iocoder.yudao.module.digitalcourse.service.coursesceneaudios.CourseSceneAudiosService;

@Tag(name = "用户 APP - 存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等")
@RestController
@RequestMapping("/digitalcourse/course-scene-audios")
@Validated
public class AppCourseSceneAudiosController {

    @Resource
    private CourseSceneAudiosService courseSceneAudiosService;

    @PostMapping("/create")
    @Operation(summary = "创建存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等")
    public CommonResult<Long> createCourseSceneAudios(@Valid @RequestBody AppCourseSceneAudiosSaveReqVO createReqVO) {
        return success(courseSceneAudiosService.createCourseSceneAudios(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等")
    public CommonResult<Boolean> updateCourseSceneAudios(@Valid @RequestBody AppCourseSceneAudiosSaveReqVO updateReqVO) {
        courseSceneAudiosService.updateCourseSceneAudios(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCourseSceneAudios(@RequestParam("id") Long id) {
        courseSceneAudiosService.deleteCourseSceneAudios(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppCourseSceneAudiosRespVO> getCourseSceneAudios(@RequestParam("id") Long id) {
        CourseSceneAudiosDO courseSceneAudios = courseSceneAudiosService.getCourseSceneAudios(id);
        return success(BeanUtils.toBean(courseSceneAudios, AppCourseSceneAudiosRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等分页")
    public CommonResult<PageResult<AppCourseSceneAudiosRespVO>> getCourseSceneAudiosPage(@Valid AppCourseSceneAudiosPageReqVO pageReqVO) {
        PageResult<CourseSceneAudiosDO> pageResult = courseSceneAudiosService.getCourseSceneAudiosPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppCourseSceneAudiosRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等 Excel")
    public void exportCourseSceneAudiosExcel(@Valid AppCourseSceneAudiosPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CourseSceneAudiosDO> list = courseSceneAudiosService.getCourseSceneAudiosPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等.xls", "数据", AppCourseSceneAudiosRespVO.class,
                        BeanUtils.toBean(list, AppCourseSceneAudiosRespVO.class));
    }

}