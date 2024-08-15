package cn.iocoder.yudao.module.digitalcourse.controller.admin.backgrounds.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 背景信息（PPT背景、板书、插图、字幕等） Response VO")
@Data
@ExcelIgnoreUnannotated
public class BackgroundsRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "7647")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "背景类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "背景类型", converter = DictConvert.class)
    @DictFormat("digitalcourse_backgrounds_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer backgroundType;

    @Schema(description = "背景名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("背景名称")
    private String name;

    @Schema(description = "原始图片URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("原始图片URL")
    private String originalUrl;

    @Schema(description = "图片URL")
    @ExcelProperty("图片URL")
    private String pictureUrl;

    @Schema(description = "图片宽度")
    @ExcelProperty("图片宽度")
    private Integer width;

    @Schema(description = "图片高度")
    @ExcelProperty("图片高度")
    private Integer height;

    @Schema(description = "文件大小 ")
    @ExcelProperty("文件大小 ")
    private Long size;

    @Schema(description = "时长")
    @ExcelProperty("时长")
    private Integer duration;

    @Schema(description = "预设标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "预设标识", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer preset;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    private String pptRemark;

}