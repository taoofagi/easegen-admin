package cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 存储课程的基本信息，包括课程名称、时长、状态等分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppCoursesPageReqVO extends PageParam {

    @Schema(description = "课程名称", example = "李四")
    private String name;

    @Schema(description = "屏幕比例")
    private String aspect;

    @Schema(description = "时长（秒）")
    private Integer duration;

    @Schema(description = "高度（像素）")
    private Integer height;

    @Schema(description = "宽度（像素）")
    private Integer width;

    @Schema(description = "是否抠图标识")
    private Integer matting;

    @Schema(description = "页面模式")
    private Integer pageMode;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "页面信息")
    private String pageInfo;

    @Schema(description = "缩略图")
    private String thumbnail;

    @Schema(description = "字幕样式")
    private String subtitlesStyle;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    private String creator;

}