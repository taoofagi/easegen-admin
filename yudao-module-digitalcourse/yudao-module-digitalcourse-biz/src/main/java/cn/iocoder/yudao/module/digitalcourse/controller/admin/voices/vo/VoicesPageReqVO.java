package cn.iocoder.yudao.module.digitalcourse.controller.admin.voices.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 声音管理分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VoicesPageReqVO extends PageParam {

    @Schema(description = "声音名称", example = "张三")
    private String name;

    @Schema(description = "语言类型")
    private String language;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "音质评分", example = "10")
    private Integer quality;

    @Schema(description = "声音类型 ", example = "2")
    private Integer voiceType;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "状态 (0: 正常, 1: 异常)", example = "2")
    private Integer status;

}