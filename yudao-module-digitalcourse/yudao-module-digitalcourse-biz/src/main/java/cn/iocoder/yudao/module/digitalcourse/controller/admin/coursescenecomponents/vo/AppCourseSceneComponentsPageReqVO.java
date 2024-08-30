package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenecomponents.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 存储每个场景中的组件信息，包括PPT、数字人等分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppCourseSceneComponentsPageReqVO extends PageParam {

    @Schema(description = "组件名称", example = "芋艿")
    private String name;

    @Schema(description = "资源URL")
    private String src;

    @Schema(description = "封面URL")
    private String cover;

    @Schema(description = "组件宽度")
    private Integer width;

    @Schema(description = "组件高度")
    private Integer height;

    @Schema(description = "原始宽度")
    private Integer originWidth;

    @Schema(description = "原始高度")
    private Integer originHeight;

    @Schema(description = "类别")
    private Integer category;

    @Schema(description = "深度")
    private Integer depth;

    @Schema(description = "上边距")
    private Integer top;

    @Schema(description = "左边距")
    private Integer marginLeft;

    @Schema(description = "实体ID", example = "31129")
    private String entityId;

    @Schema(description = "实体类型 (0: 其他, 1: 数字人)", example = "2")
    private Integer entityType;

    @Schema(description = "业务ID", example = "5116")
    private String businessId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "状态 (0: 正常, 1: 异常)", example = "1")
    private Integer status;

}