package cn.iocoder.yudao.module.digitalcourse.controller.app.coursescenes.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "用户 APP - 存储课程的场景信息，包括背景、组件、声音等新增/修改 Request VO")
@Data
public class AppCourseScenesSaveReqVO {

    @Schema(description = "场景ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16122")
    private Long id;

    @Schema(description = "课程ID，关联digitalcourse_courses表", requiredMode = Schema.RequiredMode.REQUIRED, example = "15012")
    @NotNull(message = "课程ID，关联digitalcourse_courses表不能为空")
    private Integer courseId;

    @Schema(description = "场景顺序号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "场景顺序号不能为空")
    private Integer orderNo;

    @Schema(description = "时长（秒）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "时长（秒）不能为空")
    private Integer duration;

    @Schema(description = "驱动类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "驱动类型不能为空")
    private Integer driverType;

    @Schema(description = "业务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23586")
    @NotEmpty(message = "业务ID不能为空")
    private String businessId;

    @Schema(description = "状态 (0: 正常, 1: 异常)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态 (0: 正常, 1: 异常)不能为空")
    private Integer status;

}