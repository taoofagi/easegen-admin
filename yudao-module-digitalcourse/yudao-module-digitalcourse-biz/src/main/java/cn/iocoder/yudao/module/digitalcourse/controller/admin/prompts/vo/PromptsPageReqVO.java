package cn.iocoder.yudao.module.digitalcourse.controller.admin.prompts.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 存储提示词模板的信息，包括提示词的名称、类型、排序等信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PromptsPageReqVO extends PageParam {

    @Schema(description = "提示词名称", example = "张三")
    private String name;

    @Schema(description = "提示词内容")
    private String content;

    @Schema(description = "排序")
    private Integer order;

    @Schema(description = "类型", example = "1")
    private Integer type;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "状态", example = "2")
    private Integer status;

}