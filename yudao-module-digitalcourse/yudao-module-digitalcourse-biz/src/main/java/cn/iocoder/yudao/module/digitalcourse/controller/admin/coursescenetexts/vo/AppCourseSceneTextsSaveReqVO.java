package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.*;
import java.util.*;

@Schema(description = "用户 APP - 存储场景中的文本信息，包括文本内容、音调、速度等新增/修改 Request VO")
@Data
public class AppCourseSceneTextsSaveReqVO {

    @Schema(description = "场景文本ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24432")
    private Long id;

    @Schema(description = "场景ID，关联digitalcourse_course_scenes表", requiredMode = Schema.RequiredMode.REQUIRED, example = "26595")
    private Long sceneId;

    @Schema(description = "音调", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer pitch;

    @Schema(description = "速度", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double speed;

    @Schema(description = "音量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double volume;

    @Schema(description = "智能速度", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double smartSpeed;

    @Schema(description = "语速", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double speechRate;

    @Schema(description = "文本内容（JSON格式）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String textJson;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

}