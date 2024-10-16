package cn.iocoder.yudao.module.digitalcourse.controller.admin.backgrounds;

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

import cn.iocoder.yudao.module.digitalcourse.controller.admin.backgrounds.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.backgrounds.BackgroundsDO;
import cn.iocoder.yudao.module.digitalcourse.service.backgrounds.BackgroundsService;

@Tag(name = "管理后台 - 背景信息（PPT背景、板书、插图、字幕等）")
@RestController
@RequestMapping("/digitalcourse/backgrounds")
@Validated
public class BackgroundsController {

    @Resource
    private BackgroundsService backgroundsService;

    @PostMapping("/create")
    @Operation(summary = "创建背景信息（PPT背景、板书、插图、字幕等）")
    @PreAuthorize("@ss.hasPermission('digitalcourse:backgrounds:create')")
    public CommonResult<Long> createBackgrounds(@Valid @RequestBody BackgroundsSaveReqVO createReqVO) {
        return success(backgroundsService.createBackgrounds(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新背景信息（PPT背景、板书、插图、字幕等）")
    @PreAuthorize("@ss.hasPermission('digitalcourse:backgrounds:update')")
    public CommonResult<Boolean> updateBackgrounds(@Valid @RequestBody BackgroundsSaveReqVO updateReqVO) {
        backgroundsService.updateBackgrounds(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除背景信息（PPT背景、板书、插图、字幕等）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('digitalcourse:backgrounds:delete')")
    public CommonResult<Boolean> deleteBackgrounds(@RequestParam("id") Long id) {
        backgroundsService.deleteBackgrounds(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得背景信息（PPT背景、板书、插图、字幕等）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:backgrounds:query')")
    public CommonResult<BackgroundsRespVO> getBackgrounds(@RequestParam("id") Long id) {
        BackgroundsDO backgrounds = backgroundsService.getBackgrounds(id);
        return success(BeanUtils.toBean(backgrounds, BackgroundsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得背景信息（PPT背景、板书、插图、字幕等）分页")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:backgrounds:query')")
    public CommonResult<PageResult<BackgroundsRespVO>> getBackgroundsPage(@Valid BackgroundsPageReqVO pageReqVO) {
        PageResult<BackgroundsDO> pageResult = backgroundsService.getBackgroundsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BackgroundsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出背景信息（PPT背景、板书、插图、字幕等） Excel")
    @PreAuthorize("@ss.hasPermission('digitalcourse:backgrounds:export')")
    public void exportBackgroundsExcel(@Valid BackgroundsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BackgroundsDO> list = backgroundsService.getBackgroundsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "背景信息（PPT背景、板书、插图、字幕等）.xls", "数据", BackgroundsRespVO.class,
                        BeanUtils.toBean(list, BackgroundsRespVO.class));
    }

}