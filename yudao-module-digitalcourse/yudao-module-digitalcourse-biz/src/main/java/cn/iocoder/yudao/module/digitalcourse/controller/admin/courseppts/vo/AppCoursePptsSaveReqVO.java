package cn.iocoder.yudao.module.digitalcourse.controller.admin.courseppts.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Schema(description = "用户 APP - 存储课程的PPT信息，包括文件名、文件大小、类型等新增/修改 Request VO")
@Data
public class AppCoursePptsSaveReqVO {

    @Schema(description = "课程PPT ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9985")
    private Long id;

    @Schema(description = "课程ID，关联digitalcourse_courses表", requiredMode = Schema.RequiredMode.REQUIRED, example = "8429")
    @NotNull(message = "课程ID，关联digitalcourse_courses表不能为空")
    private Integer courseId;

    @Schema(description = "文件名", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "文件名不能为空")
    private String filename;

    @Schema(description = "文件大小（字节）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "文件大小（字节）不能为空")
    private Long size;

    @Schema(description = "文件MD5校验值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "文件MD5校验值不能为空")
    private String md5;

    @Schema(description = "文档类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "文档类型不能为空")
    private Integer docType;

    @Schema(description = "扩展信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "扩展信息不能为空")
    private String extInfo;

    @Schema(description = "ppt Url", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "文件地址不能为空")
    private String url;

    @Schema(description = "解析类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "解析类型不能为空")
    private Integer resolveType;

    @Schema(description = "状态 (0: 正常, 1: 异常)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态 (0: 正常, 1: 异常)不能为空")
    private Integer status;

    @Schema(description = "ppt总页数", example = "1")
    @ExcelProperty("ppt总页数")
    private Integer pageSize;

}