package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 存储每个场景的背景信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppCourseSceneBackgroundsPageReqVO extends PageParam {

    @Schema(description = "背景类型", example = "1")
    private Integer backgroundType;

    @Schema(description = "背景实体ID", example = "3388")
    private Integer entityId;

    @Schema(description = "背景资源URL")
    private String src;

    @Schema(description = "背景封面URL")
    private String cover;

    @Schema(description = "背景宽度")
    private Integer width;

    @Schema(description = "背景高度")
    private Integer height;

    @Schema(description = "深度")
    private Integer depth;

    @Schema(description = "原始宽度")
    private Integer originWidth;

    @Schema(description = "原始高度")
    private Integer originHeight;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "状态", example = "1")
    private Integer status;

}