package cn.iocoder.yudao.module.member.controller.admin.workcenter;

import cn.iocoder.yudao.module.member.dal.dataobject.workcenter.WorkCenterDO;
import cn.iocoder.yudao.module.member.service.workcenter.WorkCenterService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.*;
import jakarta.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.member.controller.admin.workcenter.vo.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 作品中心")
@RestController
@RequestMapping("/member/work-center")
@Validated
public class WorkCenterController {

    @Resource
    private WorkCenterService workCenterService;

    @PostMapping("/create")
    @Operation(summary = "创建作品中心")
    @PreAuthorize("@ss.hasPermission('member:work-center:create')")
    public CommonResult<Long> createWorkCenter(@Valid @RequestBody WorkCenterSaveReqVO createReqVO) {
        return success(workCenterService.createWorkCenter(createReqVO));
    }

    @PostMapping("/upload")
    @Operation(summary = "创建作品中心")
    @PreAuthorize("@ss.hasPermission('member:work-center:upload')")
    public CommonResult upload(@RequestBody MultipartFile file) throws IOException {
         Map map = workCenterService.uploadWorkCenter(file);
        return success(map);
    }

    @PutMapping("/update")
    @Operation(summary = "更新作品中心")
    @PreAuthorize("@ss.hasPermission('member:work-center:update')")
    public CommonResult<Boolean> updateWorkCenter(@Valid @RequestBody WorkCenterSaveReqVO updateReqVO) {
        workCenterService.updateWorkCenter(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除作品中心")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('member:work-center:delete')")
    public CommonResult<Boolean> deleteWorkCenter(@RequestParam("id") Long id) {
        workCenterService.deleteWorkCenter(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得作品中心")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('member:work-center:query')")
    public CommonResult<WorkCenterRespVO> getWorkCenter(@RequestParam("id") Long id) {
        WorkCenterDO workCenter = workCenterService.getWorkCenter(id);
        return success(BeanUtils.toBean(workCenter, WorkCenterRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得作品中心分页")
    @PreAuthorize("@ss.hasPermission('member:work-center:query')")
    public CommonResult<PageResult<WorkCenterRespVO>> getWorkCenterPage(@Valid WorkCenterPageReqVO pageReqVO) {
        PageResult<WorkCenterDO> pageResult = workCenterService.getWorkCenterPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WorkCenterRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出作品中心 Excel")
    @PreAuthorize("@ss.hasPermission('member:work-center:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportWorkCenterExcel(@Valid WorkCenterPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WorkCenterDO> list = workCenterService.getWorkCenterPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "作品中心.xls", "数据", WorkCenterRespVO.class,
                        BeanUtils.toBean(list, WorkCenterRespVO.class));
    }

}