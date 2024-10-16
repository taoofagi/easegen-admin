package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 存储每个场景中的声音信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppCourseSceneVoicesPageReqVO extends PageParam {

    @Schema(description = "音调（0-100）")
    private Integer tonePitch;

    @Schema(description = "声音类型", example = "2")
    private Integer voiceType;

    @Schema(description = "语速")
    private Double speechRate;

    @Schema(description = "自定义名称", example = "芋艿")
    private String name;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "状态", example = "2")
    private Integer status;

}