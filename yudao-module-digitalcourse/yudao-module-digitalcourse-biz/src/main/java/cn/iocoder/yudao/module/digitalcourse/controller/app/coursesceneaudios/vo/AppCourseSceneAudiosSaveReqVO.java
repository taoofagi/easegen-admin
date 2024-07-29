package cn.iocoder.yudao.module.digitalcourse.controller.app.coursesceneaudios.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "用户 APP - 存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等新增/修改 Request VO")
@Data
public class AppCourseSceneAudiosSaveReqVO {

    @Schema(description = "场景音频ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2031")
    private Long id;

    @Schema(description = "场景ID，关联digitalcourse_course_scenes表", requiredMode = Schema.RequiredMode.REQUIRED, example = "15047")
    @NotNull(message = "场景ID，关联digitalcourse_course_scenes表不能为空")
    private Integer sceneId;

    @Schema(description = "音频ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15881")
    @NotNull(message = "音频ID不能为空")
    private Integer audioId;

    @Schema(description = "是否使用视频背景音频", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否使用视频背景音频不能为空")
    private Integer useVideoBackgroundAudio;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态不能为空")
    private Integer status;

}