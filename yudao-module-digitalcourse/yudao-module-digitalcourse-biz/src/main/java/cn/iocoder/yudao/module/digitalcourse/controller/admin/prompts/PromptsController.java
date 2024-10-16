package cn.iocoder.yudao.module.digitalcourse.controller.admin.prompts;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
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

import cn.iocoder.yudao.module.digitalcourse.controller.admin.prompts.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.prompts.PromptsDO;
import cn.iocoder.yudao.module.digitalcourse.service.prompts.PromptsService;

@Tag(name = "管理后台 - 存储提示词模板的信息，包括提示词的名称、类型、排序等信息")
@RestController
@RequestMapping("/digitalcourse/prompts")
@Validated
public class PromptsController {

    @Resource
    private PromptsService promptsService;

    @PostMapping("/create")
    @Operation(summary = "创建存储提示词模板的信息，包括提示词的名称、类型、排序等信息")
    @PreAuthorize("@ss.hasPermission('digitalcourse:prompts:create')")
    public CommonResult<Long> createPrompts(@Valid @RequestBody PromptsSaveReqVO createReqVO) {
        return success(promptsService.createPrompts(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新存储提示词模板的信息，包括提示词的名称、类型、排序等信息")
    @PreAuthorize("@ss.hasPermission('digitalcourse:prompts:update')")
    public CommonResult<Boolean> updatePrompts(@Valid @RequestBody PromptsSaveReqVO updateReqVO) {
        promptsService.updatePrompts(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除存储提示词模板的信息，包括提示词的名称、类型、排序等信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('digitalcourse:prompts:delete')")
    public CommonResult<Boolean> deletePrompts(@RequestParam("id") Long id) {
        promptsService.deletePrompts(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得存储提示词模板的信息，包括提示词的名称、类型、排序等信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:prompts:query')")
    public CommonResult<PromptsRespVO> getPrompts(@RequestParam("id") Long id) {
        PromptsDO prompts = promptsService.getPrompts(id);
        return success(BeanUtils.toBean(prompts, PromptsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得存储提示词模板的信息，包括提示词的名称、类型、排序等信息分页")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:prompts:query')")
    public CommonResult<PageResult<PromptsRespVO>> getPromptsPage(@Valid PromptsPageReqVO pageReqVO) {
        PageResult<PromptsDO> pageResult = promptsService.getPromptsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PromptsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出存储提示词模板的信息，包括提示词的名称、类型、排序等信息 Excel")
    @PreAuthorize("@ss.hasPermission('digitalcourse:prompts:export')")
    public void exportPromptsExcel(@Valid PromptsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PromptsDO> list = promptsService.getPromptsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "存储提示词模板的信息，包括提示词的名称、类型、排序等信息.xls", "数据", PromptsRespVO.class,
                        BeanUtils.toBean(list, PromptsRespVO.class));
    }

}