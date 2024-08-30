package cn.iocoder.yudao.module.digitalcourse.controller.admin.fonts;

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

import cn.iocoder.yudao.module.digitalcourse.controller.admin.fonts.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.fonts.FontsDO;
import cn.iocoder.yudao.module.digitalcourse.service.fonts.FontsService;

@Tag(name = "管理后台 - 存储字体的信息，包括字体的别名、预览URL、名称等")
@RestController
@RequestMapping("/digitalcourse/fonts")
@Validated
public class FontsController {

    @Resource
    private FontsService fontsService;

    @PostMapping("/create")
    @Operation(summary = "创建存储字体的信息，包括字体的别名、预览URL、名称等")
    @PreAuthorize("@ss.hasPermission('digitalcourse:fonts:create')")
    public CommonResult<Long> createFonts(@Valid @RequestBody FontsSaveReqVO createReqVO) {
        return success(fontsService.createFonts(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新存储字体的信息，包括字体的别名、预览URL、名称等")
    @PreAuthorize("@ss.hasPermission('digitalcourse:fonts:update')")
    public CommonResult<Boolean> updateFonts(@Valid @RequestBody FontsSaveReqVO updateReqVO) {
        fontsService.updateFonts(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除存储字体的信息，包括字体的别名、预览URL、名称等")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('digitalcourse:fonts:delete')")
    public CommonResult<Boolean> deleteFonts(@RequestParam("id") Long id) {
        fontsService.deleteFonts(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得存储字体的信息，包括字体的别名、预览URL、名称等")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:fonts:query')")
    public CommonResult<FontsRespVO> getFonts(@RequestParam("id") Long id) {
        FontsDO fonts = fontsService.getFonts(id);
        return success(BeanUtils.toBean(fonts, FontsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得存储字体的信息，包括字体的别名、预览URL、名称等分页")
//    @PreAuthorize("@ss.hasPermission('digitalcourse:fonts:query')")
    public CommonResult<PageResult<FontsRespVO>> getFontsPage(@Valid FontsPageReqVO pageReqVO) {
        PageResult<FontsDO> pageResult = fontsService.getFontsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, FontsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出存储字体的信息，包括字体的别名、预览URL、名称等 Excel")
    @PreAuthorize("@ss.hasPermission('digitalcourse:fonts:export')")
    public void exportFontsExcel(@Valid FontsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<FontsDO> list = fontsService.getFontsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "存储字体的信息，包括字体的别名、预览URL、名称等.xls", "数据", FontsRespVO.class,
                        BeanUtils.toBean(list, FontsRespVO.class));
    }

}