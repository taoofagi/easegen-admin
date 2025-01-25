package cn.iocoder.yudao.module.digitalcourse.controller.admin.voices;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.voices.vo.VoicesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.voices.vo.VoicesRespVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.voices.vo.VoicesSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.voices.AuditionDO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.voices.VoicesDO;
import cn.iocoder.yudao.module.digitalcourse.service.voices.VoicesService;
import cn.iocoder.yudao.module.infra.api.config.ConfigApi;
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

import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.BAD_REQUEST;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 声音管理")
@RestController
@RequestMapping("/digitalcourse/voices")
@Validated
public class VoicesController {

    static final String Limit_AUDITIONWORD = "limit.auditionWord";

    @Resource
    private ConfigApi configApi;

    @Resource
    private VoicesService voicesService;

    @PostMapping("/create")
    @Operation(summary = "创建声音管理")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:voices:create')")
    public CommonResult<Long> createVoices(@Valid @RequestBody VoicesSaveReqVO createReqVO) {
        return success(voicesService.createVoices(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新声音管理")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:voices:update')")
    public CommonResult<Boolean> updateVoices(@Valid @RequestBody VoicesSaveReqVO updateReqVO) {
        voicesService.updateVoices(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除声音管理")
    @Parameter(name = "id", description = "编号", required = true)
    //@PreAuthorize("@ss.hasPermission('digitalcourse:voices:delete')")
    public CommonResult<Boolean> deleteVoices(@RequestParam("id") Long id) {
        voicesService.deleteVoices(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得声音管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:voices:query')")
    public CommonResult<VoicesRespVO> getVoices(@RequestParam("id") Long id) {
        VoicesDO voices = voicesService.getVoices(id);
        return success(BeanUtils.toBean(voices, VoicesRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得声音管理分页")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:voices:query')")
    public CommonResult<PageResult<VoicesRespVO>> getVoicesPage(@Valid VoicesPageReqVO pageReqVO) {
        PageResult<VoicesDO> pageResult = voicesService.getVoicesPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VoicesRespVO.class));
    }

    @GetMapping("/common-page")
    @Operation(summary = "获得声音管理分页")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:voices:query')")
    public CommonResult<PageResult<VoicesRespVO>> getVoicesCommonPage(@Valid VoicesPageReqVO pageReqVO) {
        PageResult<VoicesDO> pageResult = voicesService.getVoicesCommonPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VoicesRespVO.class));
    }

    @GetMapping("/auditing")
    @Operation(summary = "查看自己流程中的数字人")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> auditing() {
        return success(voicesService.auditing());
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

    @PostMapping("/audition")
    @Operation(summary = "试听")
    public CommonResult<String> audition(@Valid @RequestBody AuditionDO auditionDO) {
        int limitWord = Integer.parseInt(configApi.getConfigValueByKey(Limit_AUDITIONWORD));
        if (limitWord<(auditionDO.getText().length())) {
            return CommonResult.error(BAD_REQUEST.getCode(), "试听文字超出字数限制，最多【"+limitWord+"】字");
        }
        return success(voicesService.audition(auditionDO));
    }

}