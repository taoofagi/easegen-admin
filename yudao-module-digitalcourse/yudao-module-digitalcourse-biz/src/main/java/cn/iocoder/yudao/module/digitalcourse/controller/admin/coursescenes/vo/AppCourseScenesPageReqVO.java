package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 存储课程的场景信息，包括背景、组件、声音等分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppCourseScenesPageReqVO extends PageParam {

    @Schema(description = "场景顺序号")
    private Integer orderNo;

    @Schema(description = "时长（秒）")
    private Integer duration;

    @Schema(description = "驱动类型", example = "1")
    private Integer driverType;

    @Schema(description = "业务ID", example = "23586")
    private String businessId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "状态 (0: 正常, 1: 异常)", example = "1")
    private Integer status;

}