package cn.iocoder.yudao.module.digitalcourse.controller.admin.courseppts;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courseppts.vo.AppCoursePptsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courseppts.vo.AppCoursePptsRespVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courseppts.vo.AppCoursePptsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courseppts.CoursePptsDO;
import cn.iocoder.yudao.module.digitalcourse.service.courseppts.CoursePptsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 存储课程的PPT信息，包括文件名、文件大小、类型等")
@RestController
@RequestMapping("/digitalcourse/course-ppts")
@Validated
public class AppCoursePptsController {

    @Resource
    private CoursePptsService coursePptsService;

    @PostMapping("/create")
    @Operation(summary = "创建存储课程的PPT信息，包括文件名、文件大小、类型等")
    public CommonResult<Long> createCoursePpts( @Valid @RequestBody AppCoursePptsSaveReqVO createReqVO) {
        return success(coursePptsService.createCoursePpts(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新存储课程的PPT信息，包括文件名、文件大小、类型等")
    public CommonResult<Boolean> updateCoursePpts(@Valid @RequestBody AppCoursePptsSaveReqVO updateReqVO) {
        coursePptsService.updateCoursePpts(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除存储课程的PPT信息，包括文件名、文件大小、类型等")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCoursePpts(@RequestParam("id") Long id) {
        coursePptsService.deleteCoursePpts(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得存储课程的PPT信息，包括文件名、文件大小、类型等")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppCoursePptsRespVO> getCoursePpts(@RequestParam("id") Long id) {
        CoursePptsDO coursePpts = coursePptsService.getCoursePpts(id);
        return success(BeanUtils.toBean(coursePpts, AppCoursePptsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得存储课程的PPT信息，包括文件名、文件大小、类型等分页")
    public CommonResult<PageResult<AppCoursePptsRespVO>> getCoursePptsPage(@Valid AppCoursePptsPageReqVO pageReqVO) {
        PageResult<CoursePptsDO> pageResult = coursePptsService.getCoursePptsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppCoursePptsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出存储课程的PPT信息，包括文件名、文件大小、类型等 Excel")
    public void exportCoursePptsExcel(@Valid AppCoursePptsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CoursePptsDO> list = coursePptsService.getCoursePptsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "存储课程的PPT信息，包括文件名、文件大小、类型等.xls", "数据", AppCoursePptsRespVO.class,
                        BeanUtils.toBean(list, AppCoursePptsRespVO.class));
    }

    @GetMapping("/getSchedule")
    public CommonResult getSchedule(@Valid Long id){
       return coursePptsService.getSchedule(id);
    }

    @PostMapping("/merge")
    @Operation(summary = "合并ppt")
    public ResponseEntity<Object> mergePPTs(@RequestParam("files") MultipartFile[] files) {
        return coursePptsService.mergePPTs(files);
    }

    @PostMapping("/convert")
    @Operation(summary = "解析PPT内容")
    public ResponseEntity<Map<String, Object>> convertPptToImages(@RequestParam("file") MultipartFile file) {
        return coursePptsService.convertPptToImages(file);
    }

}