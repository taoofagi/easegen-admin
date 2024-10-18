package cn.iocoder.yudao.module.digitalcourse.controller.admin.courseppts.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 存储课程的PPT信息，包括文件名、文件大小、类型等分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppCoursePptsPageReqVO extends PageParam {

    @Schema(description = "课程ID，关联digitalcourse_courses表", example = "8429")
    private Integer courseId;

    @Schema(description = "文件名", example = "赵六")
    private String filename;

    @Schema(description = "文件大小（字节）")
    private Long size;

    @Schema(description = "文件MD5校验值")
    private String md5;

    @Schema(description = "文档类型", example = "2")
    private Integer docType;

    @Schema(description = "扩展信息")
    private String extInfo;

    @Schema(description = "解析类型", example = "1")
    private Integer resolveType;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "状态 (0: 正常, 1: 异常)", example = "1")
    private Integer status;

    @Schema(description = "ppt总页数", example = "1")
    private Integer pageSize;

}