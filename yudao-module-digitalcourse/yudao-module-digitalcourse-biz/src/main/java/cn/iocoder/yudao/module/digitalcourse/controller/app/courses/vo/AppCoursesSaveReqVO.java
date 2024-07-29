package cn.iocoder.yudao.module.digitalcourse.controller.app.courses.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "用户 APP - 存储课程的基本信息，包括课程名称、时长、状态等新增/修改 Request VO")
@Data
public class AppCoursesSaveReqVO {

    @Schema(description = "课程ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30853")
    private Long id;

    @Schema(description = "账户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "361")
    @NotNull(message = "账户ID不能为空")
    private Integer accountId;

    @Schema(description = "课程名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "课程名称不能为空")
    private String name;

    @Schema(description = "屏幕比例", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "屏幕比例不能为空")
    private String aspect;

    @Schema(description = "时长（秒）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "时长（秒）不能为空")
    private Integer duration;

    @Schema(description = "高度（像素）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "高度（像素）不能为空")
    private Integer height;

    @Schema(description = "宽度（像素）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "宽度（像素）不能为空")
    private Integer width;

    @Schema(description = "是否抠图标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否抠图标识不能为空")
    private Integer matting;

    @Schema(description = "页面模式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "页面模式不能为空")
    private Integer pageMode;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "页面信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "页面信息不能为空")
    private String pageInfo;

    @Schema(description = "字幕样式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "字幕样式不能为空")
    private String subtitlesStyle;

}