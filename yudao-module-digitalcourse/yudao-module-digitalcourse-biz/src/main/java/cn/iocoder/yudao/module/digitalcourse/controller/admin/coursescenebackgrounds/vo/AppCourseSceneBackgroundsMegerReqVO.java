package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 存储每个场景的背景信息新增/修改 Request VO")
@Data
public class AppCourseSceneBackgroundsMegerReqVO {

    @Schema(description = "场景背景ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23305")
    private Long id;

    @Schema(description = "场景ID，关联digitalcourse_course_scenes表", requiredMode = Schema.RequiredMode.REQUIRED, example = "14119")
    private Long sceneId;

    @Schema(description = "背景类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "背景类型不能为空")
    private Integer backgroundType;

    @Schema(description = "背景实体ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3388")
    private String entityId;

    @Schema(description = "背景资源URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String src;

    @Schema(description = "背景封面URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cover;

    @Schema(description = "背景宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer width;

    @Schema(description = "背景高度", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer height;

    @Schema(description = "深度", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer depth;

    @Schema(description = "原始宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer originWidth;

    @Schema(description = "原始高度", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer originHeight;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "ppt口播稿")
    private String pptRemark;

    private String color;

}