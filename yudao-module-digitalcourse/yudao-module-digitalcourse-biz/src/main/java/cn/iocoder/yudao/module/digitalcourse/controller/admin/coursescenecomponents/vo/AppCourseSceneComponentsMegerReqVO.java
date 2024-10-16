package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenecomponents.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 存储每个场景中的组件信息，包括PPT、数字人等新增/修改 Request VO")
@Data
public class AppCourseSceneComponentsMegerReqVO {

    @Schema(description = "组件ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5575")
    private Long id;

    @Schema(description = "场景ID，关联digitalcourse_course_scenes表", requiredMode = Schema.RequiredMode.REQUIRED, example = "22468")
    @NotNull(message = "场景ID，关联digitalcourse_course_scenes表不能为空")
    private Long sceneId;

    @Schema(description = "组件名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "组件名称不能为空")
    private String name;

    @Schema(description = "资源URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "资源URL不能为空")
    private String src;

    @Schema(description = "封面URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cover;

    @Schema(description = "组件宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "组件宽度不能为空")
    private Integer width;

    @Schema(description = "组件高度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "组件高度不能为空")
    private Integer height;

    @Schema(description = "原始宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "原始宽度不能为空")
    private Integer originWidth;

    @Schema(description = "原始高度", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "原始高度不能为空")
    private Integer originHeight;

    @Schema(description = "类别", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "类别不能为空")
    private Integer category;

    @Schema(description = "深度", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer depth;

    @Schema(description = "上边距", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "上边距不能为空")
    private Integer top;

    @Schema(description = "左边距", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "左边距不能为空")
    private Integer marginLeft;

    @Schema(description = "实体ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31129")
    @NotNull(message = "实体ID不能为空")
    private String entityId;

    @Schema(description = "实体类型 (0: 其他, 1: 数字人)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "实体类型 (0: 其他, 1: 数字人)不能为空")
    private Integer entityType;

    @Schema(description = "业务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5116")
    @NotEmpty(message = "业务ID不能为空")
    private String businessId;

    @Schema(description = "状态 (0: 正常, 1: 异常)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态 (0: 正常, 1: 异常)不能为空")
    private Integer status;

    @Schema(description = "数字人类型 (0: 普通, 1: 专属) 关联digitalcourse_digital_humans表type")
    private Integer digitbotType;

    @Schema(description = "抠图标识 (0: 否, 1: 是) 默认1")
    private Integer matting;

    @Schema(description = "预留，是否修改过，默认(0false,1true)")
    private Boolean marker;

}