package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 课程媒体分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CourseMediaPageReqVO extends PageParam {

    @Schema(description = "状态 (1、合成中，2、成功，3、失败)", example = "2")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "时长")
    private Long duration;

    @Schema(description = "合成时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private String[] finishTime;

    @Schema(description = "媒体类型", example = "1")
    private String mediaType;

    @Schema(description = "名称", example = "芋艿")
    private String name;

    @Schema(description = "媒体链接", example = "https://www.iocoder.cn")
    private String previewUrl;

    @Schema(description = "进度（预留）")
    private Float progress;

    @Schema(description = "课程id", example = "9112")
    private Long courseId;

    @Schema(description = "课程名称", example = "李四")
    private String courseName;

}