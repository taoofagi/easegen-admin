package cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.*;
import java.util.*;

@Schema(description = "用户 APP - 存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等新增/修改 Request VO")
@Data
public class AppPptMaterialsSaveReqVO {

    @Schema(description = "PPT课件ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23980")
    private Long id;

    @Schema(description = "课程PPT ID，关联digitalcourse_course_ppts表", requiredMode = Schema.RequiredMode.REQUIRED, example = "2798")
    @NotNull(message = "课程PPT ID，关联digitalcourse_course_ppts表不能为空")
    private Integer pptId;

    @Schema(description = "课件名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "课件名称不能为空")
    private String name;

    @Schema(description = "背景类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "背景类型不能为空")
    private Integer backgroundType;

    @Schema(description = "图片URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotEmpty(message = "图片URL不能为空")
    private String pictureUrl;

    @Schema(description = "原始图片URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotEmpty(message = "原始图片URL不能为空")
    private String originalUrl;

    @Schema(description = "宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer width;

    @Schema(description = "高度", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer height;

    @Schema(description = "页面索引", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "页面索引不能为空")
    private Integer indexNo;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    private String pptRemark;

}