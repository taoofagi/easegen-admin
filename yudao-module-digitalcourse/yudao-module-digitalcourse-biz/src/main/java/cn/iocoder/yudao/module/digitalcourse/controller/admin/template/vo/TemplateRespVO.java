package cn.iocoder.yudao.module.digitalcourse.controller.admin.template.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 模板 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TemplateRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "30885")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "0 不显示，1显示", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("0 不显示，1显示")
    private Integer showBackground;

    @Schema(description = "0 不显示，1显示", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("0 不显示，1显示")
    private Integer showDigitalHuman;

    @Schema(description = "0 不显示，1显示", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("0 不显示，1显示")
    private Integer showPpt;

    @Schema(description = "ppt宽", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ppt宽")
    private BigDecimal pptW;

    @Schema(description = "ppt高", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ppt高")
    private BigDecimal pptH;

    @Schema(description = "ppt距离顶部位置", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ppt距离顶部位置")
    private BigDecimal pptX;

    @Schema(description = "ppt距离左侧位置", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ppt距离左侧位置")
    private BigDecimal pptY;

    @Schema(description = "数字人宽", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("数字人宽")
    private BigDecimal humanW;

    @Schema(description = "数字人高", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("数字人高")
    private BigDecimal humanH;

    @Schema(description = "数字人距离顶部位置", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("数字人距离顶部位置")
    private BigDecimal humanX;

    @Schema(description = "数字人距离左侧位置", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("数字人距离左侧位置")
    private BigDecimal humanY;

    @Schema(description = "背景图片", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("背景图片")
    private String bgImage;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "效果图")
    private String previewImage;
    @Schema(description = "效果图")
    private String templateName;
    @Schema(description = "模板尺寸")
    private String templateSize;

}