package cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials.vo.AppPptMaterialsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials.vo.AppPptMaterialsRespVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials.vo.AppPptMaterialsSaveReqVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

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

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.pptmaterials.PptMaterialsDO;
import cn.iocoder.yudao.module.digitalcourse.service.pptmaterials.PptMaterialsService;

@Tag(name = "用户 APP - 存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等")
@RestController
@RequestMapping("/digitalcourse/ppt-materials")
@Validated
public class AppPptMaterialsController {

    @Resource
    private PptMaterialsService pptMaterialsService;

    @PostMapping("/create")
    @Operation(summary = "创建存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等")
    public CommonResult<Long> createPptMaterials(@Valid @RequestBody AppPptMaterialsSaveReqVO createReqVO) {
        return success(pptMaterialsService.createPptMaterials(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等")
    public CommonResult<Boolean> updatePptMaterials(@Valid @RequestBody AppPptMaterialsSaveReqVO updateReqVO) {
        pptMaterialsService.updatePptMaterials(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deletePptMaterials(@RequestParam("id") Long id) {
        pptMaterialsService.deletePptMaterials(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppPptMaterialsRespVO> getPptMaterials(@RequestParam("id") Long id) {
        PptMaterialsDO pptMaterials = pptMaterialsService.getPptMaterials(id);
        return success(BeanUtils.toBean(pptMaterials, AppPptMaterialsRespVO.class));
    }

    @GetMapping("/copy")
    @Operation(summary = "复制")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Long> copy(@RequestParam("id") Long id) {
        PptMaterialsDO pptMaterials = pptMaterialsService.getPptMaterials(id);
        pptMaterials.setId(null);
        AppPptMaterialsSaveReqVO bean = BeanUtils.toBean(pptMaterials, AppPptMaterialsSaveReqVO.class);
        Long copyId = pptMaterialsService.createPptMaterials(bean);
        return success(copyId);
    }

    @GetMapping("/page")
    @Operation(summary = "获得存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等分页")
    public CommonResult<PageResult<AppPptMaterialsRespVO>> getPptMaterialsPage(@Valid AppPptMaterialsPageReqVO pageReqVO) {
        PageResult<PptMaterialsDO> pageResult = pptMaterialsService.getPptMaterialsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppPptMaterialsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等 Excel")
    public void exportPptMaterialsExcel(@Valid AppPptMaterialsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PptMaterialsDO> list = pptMaterialsService.getPptMaterialsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等.xls", "数据", AppPptMaterialsRespVO.class,
                        BeanUtils.toBean(list, AppPptMaterialsRespVO.class));
    }

}