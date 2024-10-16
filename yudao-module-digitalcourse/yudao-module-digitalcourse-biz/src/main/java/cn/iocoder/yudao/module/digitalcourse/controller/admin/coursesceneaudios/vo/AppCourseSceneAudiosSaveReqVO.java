package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.*;
import java.util.*;

@Schema(description = "用户 APP - 存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等新增/修改 Request VO")
@Data
public class AppCourseSceneAudiosSaveReqVO {

    @Schema(description = "场景音频ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2031")
    private Long id;

    @Schema(description = "场景ID，关联digitalcourse_course_scenes表", requiredMode = Schema.RequiredMode.REQUIRED, example = "15047")
    @NotNull(message = "场景ID，关联digitalcourse_course_scenes表不能为空")
    private Long sceneId;

    @Schema(description = "音频ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15881")
    private Integer audioId;

    @Schema(description = "audioUrl")
    private String audioUrl;

    @Schema(description = "是否使用视频背景音频", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer useVideoBackgroundAudio;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

    @Schema(description = "文件名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String fileName;



}