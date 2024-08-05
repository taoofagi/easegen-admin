package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "用户 APP - 存储场景中的文本信息，包括文本内容、音调、速度等 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppCourseSceneTextsRespVO {

    @Schema(description = "场景文本ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24432")
    @ExcelProperty("场景文本ID")
    private Long id;

    @Schema(description = "场景ID，关联digitalcourse_course_scenes表", requiredMode = Schema.RequiredMode.REQUIRED, example = "26595")
    @ExcelProperty("场景ID，关联digitalcourse_course_scenes表")
    private Integer sceneId;

    @Schema(description = "音调", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("音调")
    private Integer pitch;

    @Schema(description = "速度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("速度")
    private Double speed;

    @Schema(description = "音量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("音量")
    private Double volume;

    @Schema(description = "智能速度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("智能速度")
    private Double smartSpeed;

    @Schema(description = "语速", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("语速")
    private Double speechRate;

    @Schema(description = "文本内容（JSON格式）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("文本内容（JSON格式）")
    private String textJson;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

}