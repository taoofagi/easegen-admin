package cn.iocoder.yudao.module.crm.controller.admin.receivable;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.crm.controller.admin.receivable.vo.*;
import cn.iocoder.yudao.module.crm.dal.dataobject.receivable.ReceivablePlanDO;
import cn.iocoder.yudao.module.crm.convert.receivable.ReceivablePlanConvert;
import cn.iocoder.yudao.module.crm.service.receivable.ReceivablePlanService;

/**
 * @author 赤焰
 */
@Tag(name = "管理后台 - 回款计划")
@RestController
@RequestMapping("/crm/receivable-plan")
@Validated
public class ReceivablePlanController {

    @Resource
    private ReceivablePlanService receivablePlanService;

    @PostMapping("/create")
    @Operation(summary = "创建回款计划")
    @PreAuthorize("@ss.hasPermission('crm:receivable-plan:create')")
    public CommonResult<Long> createReceivablePlan(@Valid @RequestBody ReceivablePlanCreateReqVO createReqVO) {
        return success(receivablePlanService.createReceivablePlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新回款计划")
    @PreAuthorize("@ss.hasPermission('crm:receivable-plan:update')")
    public CommonResult<Boolean> updateReceivablePlan(@Valid @RequestBody ReceivablePlanUpdateReqVO updateReqVO) {
        receivablePlanService.updateReceivablePlan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除回款计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:receivable-plan:delete')")
    public CommonResult<Boolean> deleteReceivablePlan(@RequestParam("id") Long id) {
        receivablePlanService.deleteReceivablePlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得回款计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:receivable-plan:query')")
    public CommonResult<ReceivablePlanRespVO> getReceivablePlan(@RequestParam("id") Long id) {
        ReceivablePlanDO receivablePlan = receivablePlanService.getReceivablePlan(id);
        return success(ReceivablePlanConvert.INSTANCE.convert(receivablePlan));
    }

    @GetMapping("/list")
    @Operation(summary = "获得回款计划列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('crm:receivable-plan:query')")
    public CommonResult<List<ReceivablePlanRespVO>> getReceivablePlanList(@RequestParam("ids") Collection<Long> ids) {
        List<ReceivablePlanDO> list = receivablePlanService.getReceivablePlanList(ids);
        return success(ReceivablePlanConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得回款计划分页")
    @PreAuthorize("@ss.hasPermission('crm:receivable-plan:query')")
    public CommonResult<PageResult<ReceivablePlanRespVO>> getReceivablePlanPage(@Valid ReceivablePlanPageReqVO pageVO) {
        PageResult<ReceivablePlanDO> pageResult = receivablePlanService.getReceivablePlanPage(pageVO);
        return success(ReceivablePlanConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出回款计划 Excel")
    @PreAuthorize("@ss.hasPermission('crm:receivable-plan:export')")
    @OperateLog(type = EXPORT)
    public void exportReceivablePlanExcel(@Valid ReceivablePlanExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ReceivablePlanDO> list = receivablePlanService.getReceivablePlanList(exportReqVO);
        // 导出 Excel
        List<ReceivablePlanExcelVO> datas = ReceivablePlanConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "回款计划.xls", "数据", ReceivablePlanExcelVO.class, datas);
    }

}
