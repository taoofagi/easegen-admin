package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaMegerVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaRespVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesUpdateReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia.CourseMediaDO;
import cn.iocoder.yudao.module.digitalcourse.service.coursemedia.CourseMediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 课程媒体")
@RestController
@RequestMapping("/digitalcourse/course-media")
@Validated
public class CourseMediaController {

    @Resource
    private CourseMediaService courseMediaService;

    @PostMapping("/create")
    @Operation(summary = "创建课程媒体")
    @PreAuthorize("@ss.hasPermission('digitalcourse:course-media:create')")
    public CommonResult<Long> createCourseMedia(@Valid @RequestBody CourseMediaSaveReqVO createReqVO) {
        return success(courseMediaService.createCourseMedia(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新课程媒体")
    @PreAuthorize("@ss.hasPermission('digitalcourse:course-media:update')")
    public CommonResult<Boolean> updateCourseMedia(@Valid @RequestBody CourseMediaSaveReqVO updateReqVO) {
        courseMediaService.updateCourseMedia(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除课程媒体")
    @Parameter(name = "id", description = "编号", required = true)
//    @PreAuthorize("@ss.hasPermission('digitalcourse:course-media:delete')")
    public CommonResult<Boolean> deleteCourseMedia(@RequestParam("id") Long id) {
        courseMediaService.deleteCourseMedia(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得课程媒体")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:course-media:query')")
    public CommonResult<CourseMediaRespVO> getCourseMedia(@RequestParam("id") Long id) {
        CourseMediaDO courseMedia = courseMediaService.getCourseMedia(id);
        return success(BeanUtils.toBean(courseMedia, CourseMediaRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得课程媒体分页")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:course-media:query')")
    public CommonResult<PageResult<CourseMediaRespVO>> getCourseMediaPage(@Valid CourseMediaPageReqVO pageReqVO) {
        PageResult<CourseMediaDO> pageResult = courseMediaService.getCourseMediaPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CourseMediaRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出课程媒体 Excel")
    @PreAuthorize("@ss.hasPermission('digitalcourse:course-media:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCourseMediaExcel(@Valid CourseMediaPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CourseMediaDO> list = courseMediaService.getCourseMediaPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "课程媒体.xls", "数据", CourseMediaRespVO.class,
                        BeanUtils.toBean(list, CourseMediaRespVO.class));
    }

    /**
     * 合成视频
     * @param updateReqVO
     * @return
     */
    @PostMapping("/megerMedia")
    @Operation(summary = "合成视频")
    public CommonResult megerMedia(@Valid @RequestBody CourseMediaMegerVO mediaMegerVO){
        return courseMediaService.megerMedia(mediaMegerVO);
    }

    /**
     * 重新生成
     * @param updateReqVO
     * @return
     */
    @PostMapping("/reMegerMedia")
    @Operation(summary = "重新生成")
    public CommonResult reMegerMedia(@RequestBody CourseMediaMegerVO mediaMegerVO){
        return courseMediaService.reMegerMedia(mediaMegerVO);
    }

}