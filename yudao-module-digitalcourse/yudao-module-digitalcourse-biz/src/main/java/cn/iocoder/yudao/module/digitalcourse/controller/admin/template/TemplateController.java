package cn.iocoder.yudao.module.digitalcourse.controller.admin.template;

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

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.template.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.template.TemplateDO;
import cn.iocoder.yudao.module.digitalcourse.service.template.TemplateService;

@Tag(name = "管理后台 - 模板")
@RestController
@RequestMapping("/digitalcourse/template")
@Validated
public class TemplateController {

    @Resource
    private TemplateService templateService;

    @PostMapping("/create")
    @Operation(summary = "创建模板")
    @PreAuthorize("@ss.hasPermission('digitalcourse:template:create')")
    public CommonResult<Long> createTemplate(@Valid @RequestBody TemplateSaveReqVO createReqVO) {
        return success(templateService.createTemplate(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新模板")
    @PreAuthorize("@ss.hasPermission('digitalcourse:template:update')")
    public CommonResult<Boolean> updateTemplate(@Valid @RequestBody TemplateSaveReqVO updateReqVO) {
        templateService.updateTemplate(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除模板")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('digitalcourse:template:delete')")
    public CommonResult<Boolean> deleteTemplate(@RequestParam("id") Long id) {
        templateService.deleteTemplate(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得模板")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('digitalcourse:template:query')")
    public CommonResult<TemplateRespVO> getTemplate(@RequestParam("id") Long id) {
        TemplateDO template = templateService.getTemplate(id);
        return success(BeanUtils.toBean(template, TemplateRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得模板分页")
    @PreAuthorize("@ss.hasPermission('digitalcourse:template:query')")
    public CommonResult<PageResult<TemplateRespVO>> getTemplatePage(@Valid TemplatePageReqVO pageReqVO) {
        PageResult<TemplateDO> pageResult = templateService.getTemplatePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TemplateRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出模板 Excel")
    @PreAuthorize("@ss.hasPermission('digitalcourse:template:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportTemplateExcel(@Valid TemplatePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<TemplateDO> list = templateService.getTemplatePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "模板.xls", "数据", TemplateRespVO.class,
                        BeanUtils.toBean(list, TemplateRespVO.class));
    }

}