package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.*;
import java.util.*;

@Schema(description = "用户 APP - 存储每个场景中的声音信息新增/修改 Request VO")
@Data
public class AppCourseSceneVoicesSaveReqVO {

    @Schema(description = "场景声音ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17311")
    private Long id;

    @Schema(description = "场景ID，关联digitalcourse_course_scenes表", requiredMode = Schema.RequiredMode.REQUIRED, example = "26726")
    private Long sceneId;

    @Schema(description = "entityId")
    private Long entityId;

    @Schema(description = "声音ID，关联digitalcourse_voices表", requiredMode = Schema.RequiredMode.REQUIRED, example = "28531")
    private Integer voiceId;

    @Schema(description = "音调（0-100）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer tonePitch;

    @Schema(description = "声音类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer voiceType;

    @Schema(description = "语速", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double speechRate;

    @Schema(description = "自定义名称", example = "芋艿")
    private String name;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

}