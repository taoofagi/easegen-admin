package cn.iocoder.yudao.module.digitalcourse.controller.admin.digitalhumans;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.constraints.*;
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

import cn.iocoder.yudao.module.digitalcourse.controller.admin.digitalhumans.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.digitalhumans.DigitalHumansDO;
import cn.iocoder.yudao.module.digitalcourse.service.digitalhumans.DigitalHumansService;

@Tag(name = "管理后台 - 数字人模型")
@RestController
@RequestMapping("/digitalcourse/digital-humans")
@Validated
public class DigitalHumansController {

    @Resource
    private DigitalHumansService digitalHumansService;

    @PostMapping("/create")
    @Operation(summary = "创建数字人模型")
    public CommonResult<Long> createDigitalHumans(@Valid @RequestBody DigitalHumansSaveReqVO createReqVO) {
        return success(digitalHumansService.createDigitalHumans(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新数字人模型")
    public CommonResult<Boolean> updateDigitalHumans(@Valid @RequestBody DigitalHumansSaveReqVO updateReqVO) {
        digitalHumansService.updateDigitalHumans(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除数字人模型")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('digitalcourse:digital-humans:delete')")
    public CommonResult<Boolean> deleteDigitalHumans(@RequestParam("id") Long id) {
        digitalHumansService.deleteDigitalHumans(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "数字人模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:digital-humans:query')")
    public CommonResult<DigitalHumansRespVO> getDigitalHumans(@RequestParam("id") Long id) {
        DigitalHumansDO digitalHumans = digitalHumansService.getDigitalHumans(id);
        return success(BeanUtils.toBean(digitalHumans, DigitalHumansRespVO.class));
    }

    @GetMapping("/auditing")
    @Operation(summary = "查看自己流程中的数字人")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> auditing() {
        return success(digitalHumansService.auditing());
    }

    @GetMapping("/page")
    @Operation(summary = "数字人模型分页")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:digital-humans:query')")
    public CommonResult<PageResult<DigitalHumansRespVO>> getDigitalHumansPage(@Valid DigitalHumansPageReqVO pageReqVO) {
        PageResult<DigitalHumansDO> pageResult = digitalHumansService.getDigitalHumansPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DigitalHumansRespVO.class));
    }
    @GetMapping("/common-page")
    @Operation(summary = "数字人模型分页")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:digital-humans:query')")
    public CommonResult<PageResult<DigitalHumansRespVO>> getDigitalHumansCommonPage(@Valid DigitalHumansPageReqVO pageReqVO) {
        PageResult<DigitalHumansDO> pageResult = digitalHumansService.getDigitalHumansCommonPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DigitalHumansRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出数字人模型 Excel")
    @PreAuthorize("@ss.hasPermission('digitalcourse:digital-humans:export')")
    public void exportDigitalHumansExcel(@Valid DigitalHumansPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DigitalHumansDO> list = digitalHumansService.getDigitalHumansPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "数字人模型.xls", "数据", DigitalHumansRespVO.class,
                        BeanUtils.toBean(list, DigitalHumansRespVO.class));
    }

}