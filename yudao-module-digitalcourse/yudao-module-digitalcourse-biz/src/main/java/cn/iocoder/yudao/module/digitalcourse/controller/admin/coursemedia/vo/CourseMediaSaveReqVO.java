package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 课程媒体新增/修改 Request VO")
@Data
public class CourseMediaSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "12757")
    private Long id;

    @Schema(description = "状态 (1、合成中，2、成功，3、失败)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态 (1、合成中，2、成功，3、失败)不能为空")
    private Integer status;

    @Schema(description = "时长")
    private Long duration;

    @Schema(description = "合成时间")
    private String finishTime;

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