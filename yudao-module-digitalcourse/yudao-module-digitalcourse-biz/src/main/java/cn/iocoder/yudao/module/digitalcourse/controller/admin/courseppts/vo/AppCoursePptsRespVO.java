package cn.iocoder.yudao.module.digitalcourse.controller.admin.courseppts.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 存储课程的PPT信息，包括文件名、文件大小、类型等 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppCoursePptsRespVO {

    @Schema(description = "课程PPT ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9985")
    @ExcelProperty("课程PPT ID")
    private Long id;

    @Schema(description = "课程ID，关联digitalcourse_courses表", requiredMode = Schema.RequiredMode.REQUIRED, example = "8429")
    @ExcelProperty("课程ID，关联digitalcourse_courses表")
    private Integer courseId;

    @Schema(description = "文件名", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("文件名")
    private String filename;

    @Schema(description = "文件大小（字节）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("文件大小（字节）")
    private Long size;

    @Schema(description = "文件MD5校验值", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("文件MD5校验值")
    private String md5;

    @Schema(description = "文档类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("文档类型")
    private Integer docType;

    @Schema(description = "扩展信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("扩展信息")
    private String extInfo;

    @Schema(description = "解析类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("解析类型")
    private Integer resolveType;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "状态 (0: 正常, 1: 异常)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态 (0: 正常, 1: 异常)")
    private Integer status;

    @Schema(description = "ppt总页数", example = "1")
    @ExcelProperty("ppt总页数")
    private Integer pageSize;

}