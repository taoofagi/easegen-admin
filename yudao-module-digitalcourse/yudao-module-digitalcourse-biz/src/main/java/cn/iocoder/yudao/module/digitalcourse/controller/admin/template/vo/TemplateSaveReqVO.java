package cn.iocoder.yudao.module.digitalcourse.controller.admin.template.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 模板新增/修改 Request VO")
@Data
public class TemplateSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "30885")
    private Long id;

    @Schema(description = "是否显示背景", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否显示背景不能为空")
    private Integer showBackground;

    @Schema(description = "是否显示数字人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否显示数字人不能为空")
    private Integer showDigitalHuman;

    @Schema(description = "是否显示ppt", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否显示ppt不能为空")
    private Integer showPpt;

    @Schema(description = "ppt宽", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ppt宽不能为空")
    private BigDecimal pptW;

    @Schema(description = "ppt高", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ppt高不能为空")
    private BigDecimal pptH;

    @Schema(description = "ppt距离顶部位置", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ppt距离顶部位置不能为空")
    private BigDecimal pptX;

    @Schema(description = "ppt距离左侧位置", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ppt距离左侧位置不能为空")
    private BigDecimal pptY;

    @Schema(description = "数字人宽", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数字人宽不能为空")
    private BigDecimal humanW;

    @Schema(description = "数字人高", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数字人高不能为空")
    private BigDecimal humanH;

    @Schema(description = "数字人距离顶部位置", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数字人距离顶部位置不能为空")
    private BigDecimal humanX;

    @Schema(description = "数字人距离左侧位置", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数字人距离左侧位置不能为空")
    private BigDecimal humanY;

    @Schema(description = "背景图片", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "背景图片不能为空")
    private String bgImage;

    @Schema(description = "效果图")
    private String previewImage;

}