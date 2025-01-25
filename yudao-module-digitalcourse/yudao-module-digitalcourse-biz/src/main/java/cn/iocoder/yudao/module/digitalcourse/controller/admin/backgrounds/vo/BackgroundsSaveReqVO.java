package cn.iocoder.yudao.module.digitalcourse.controller.admin.backgrounds.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 背景信息（PPT背景、板书、插图、字幕等）新增/修改 Request VO")
@Data
public class BackgroundsSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "7647")
    private Long id;

    @Schema(description = "背景类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "背景类型不能为空")
    private Integer backgroundType;

    @Schema(description = "背景名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "背景名称不能为空")
    private String name;

    @Schema(description = "原始图片URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "原始图片URL不能为空")
    private String originalUrl;

    @Schema(description = "图片URL")
    private String pictureUrl;

    @Schema(description = "图片宽度")
    private Integer width;

    @Schema(description = "图片高度")
    private Integer height;

    @Schema(description = "文件大小 ")
    private Long size;

    @Schema(description = "时长")
    private Integer duration;

    @Schema(description = "预设标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "预设标识不能为空")
    private Integer preset;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    @Schema(description = "pptRemark", requiredMode = Schema.RequiredMode.REQUIRED)
    private String pptRemark;
    @Schema(description = "templateSize")
    private String templateSize;

}