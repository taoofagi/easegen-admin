package cn.iocoder.yudao.module.digitalcourse.controller.app.coursescenebackgrounds.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "用户 APP - 存储每个场景的背景信息新增/修改 Request VO")
@Data
public class AppCourseSceneBackgroundsSaveReqVO {

    @Schema(description = "场景背景ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23305")
    private Long id;

    @Schema(description = "场景ID，关联digitalcourse_course_scenes表", requiredMode = Schema.RequiredMode.REQUIRED, example = "14119")
    @NotNull(message = "场景ID，关联digitalcourse_course_scenes表不能为空")
    private Integer sceneId;

    @Schema(description = "背景类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "背景类型不能为空")
    private Integer backgroundType;

    @Schema(description = "背景实体ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3388")
    @NotNull(message = "背景实体ID不能为空")
    private Integer entityId;

    @Schema(description = "背景资源URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "背景资源URL不能为空")
    private String src;

    @Schema(description = "背景封面URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "背景封面URL不能为空")
    private String cover;

    @Schema(description = "背景宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "背景宽度不能为空")
    private Integer width;

    @Schema(description = "背景高度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "背景高度不能为空")
    private Integer height;

    @Schema(description = "深度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "深度不能为空")
    private Integer depth;

    @Schema(description = "原始宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "原始宽度不能为空")
    private Integer originWidth;

    @Schema(description = "原始高度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "原始高度不能为空")
    private Integer originHeight;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

}