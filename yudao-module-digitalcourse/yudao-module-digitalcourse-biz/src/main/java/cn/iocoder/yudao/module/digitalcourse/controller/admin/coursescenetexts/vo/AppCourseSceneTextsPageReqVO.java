package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 存储场景中的文本信息，包括文本内容、音调、速度等分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppCourseSceneTextsPageReqVO extends PageParam {

    @Schema(description = "音调")
    private Integer pitch;

    @Schema(description = "速度")
    private Double speed;

    @Schema(description = "音量")
    private Double volume;

    @Schema(description = "智能速度")
    private Double smartSpeed;

    @Schema(description = "语速")
    private Double speechRate;

    @Schema(description = "文本内容（JSON格式）")
    private String textJson;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "状态", example = "1")
    private Integer status;

}