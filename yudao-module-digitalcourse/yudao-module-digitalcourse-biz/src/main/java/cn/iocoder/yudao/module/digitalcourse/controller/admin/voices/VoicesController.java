package cn.iocoder.yudao.module.digitalcourse.controller.admin.voices;

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

import cn.iocoder.yudao.module.digitalcourse.controller.admin.voices.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.voices.VoicesDO;
import cn.iocoder.yudao.module.digitalcourse.service.voices.VoicesService;

@Tag(name = "管理后台 - 声音管理")
@RestController
@RequestMapping("/digitalcourse/voices")
@Validated
public class VoicesController {

    @Resource
    private VoicesService voicesService;

    @PostMapping("/create")
    @Operation(summary = "创建声音管理")
    @PreAuthorize("@ss.hasPermission('digitalcourse:voices:create')")
    public CommonResult<Long> createVoices(@Valid @RequestBody VoicesSaveReqVO createReqVO) {
        return success(voicesService.createVoices(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新声音管理")
    @PreAuthorize("@ss.hasPermission('digitalcourse:voices:update')")
    public CommonResult<Boolean> updateVoices(@Valid @RequestBody VoicesSaveReqVO updateReqVO) {
        voicesService.updateVoices(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除声音管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('digitalcourse:voices:delete')")
    public CommonResult<Boolean> deleteVoices(@RequestParam("id") Long id) {
        voicesService.deleteVoices(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得声音管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('digitalcourse:voices:query')")
    public CommonResult<VoicesRespVO> getVoices(@RequestParam("id") Long id) {
        VoicesDO voices = voicesService.getVoices(id);
        return success(BeanUtils.toBean(voices, VoicesRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得声音管理分页")
    @PreAuthorize("@ss.hasPermission('digitalcourse:voices:query')")
    public CommonResult<PageResult<VoicesRespVO>> getVoicesPage(@Valid VoicesPageReqVO pageReqVO) {
        PageResult<VoicesDO> pageResult = voicesService.getVoicesPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VoicesRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出声音管理 Excel")
    @PreAuthorize("@ss.hasPermission('digitalcourse:voices:export')")
    public void exportVoicesExcel(@Valid VoicesPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VoicesDO> list = voicesService.getVoicesPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "声音管理.xls", "数据", VoicesRespVO.class,
                        BeanUtils.toBean(list, VoicesRespVO.class));
    }

}