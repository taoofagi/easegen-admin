package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.backgrounds.vo.BackgroundsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios.vo.AppCourseSceneAudiosSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo.AppCourseSceneBackgroundsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenecomponents.vo.AppCourseSceneComponentsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo.AppCourseSceneTextsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo.AppCourseSceneVoicesSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.voices.vo.VoicesSaveReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.*;
import java.util.*;

@Schema(description = "用户 APP - 存储课程的场景信息，包括背景、组件、声音等新增/修改 Request VO")
@Data
public class AppCourseScenesSaveReqVO {

    @Schema(description = "场景ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16122")
    private Long id;

    @Schema(description = "课程ID，关联digitalcourse_courses表", requiredMode = Schema.RequiredMode.REQUIRED, example = "15012")
    private Long courseId;

    @Schema(description = "场景顺序号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long orderNo;

    @Schema(description = "时长（秒）", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long duration;

    @Schema(description = "驱动类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "驱动类型不能为空")
    private Integer driverType;

    @Schema(description = "业务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23586")
    @NotEmpty(message = "业务ID不能为空")
    private String businessId;

    @Schema(description = "状态 (0: 正常, 1: 异常)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    private AppCourseSceneBackgroundsSaveReqVO background;

    private List<AppCourseSceneComponentsSaveReqVO> components;

    private AppCourseSceneVoicesSaveReqVO voice;

    private AppCourseSceneTextsSaveReqVO textDriver;

    private AppCourseSceneAudiosSaveReqVO audioDriver;

}