package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "用户 APP - 存储每个场景的背景信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppCourseSceneBackgroundsRespVO {

    @Schema(description = "场景背景ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23305")
    @ExcelProperty("场景背景ID")
    private Long id;

    @Schema(description = "场景ID，关联digitalcourse_course_scenes表", requiredMode = Schema.RequiredMode.REQUIRED, example = "14119")
    @ExcelProperty("场景ID，关联digitalcourse_course_scenes表")
    private Integer sceneId;

    @Schema(description = "背景类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "背景类型", converter = DictConvert.class)
    @DictFormat("digitalcourse_backgrounds_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer backgroundType;

    @Schema(description = "背景实体ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3388")
    @ExcelProperty("背景实体ID")
    private Integer entityId;

    @Schema(description = "背景资源URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("背景资源URL")
    private String src;

    @Schema(description = "背景封面URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("背景封面URL")
    private String cover;

    @Schema(description = "背景宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("背景宽度")
    private Integer width;

    @Schema(description = "背景高度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("背景高度")
    private Integer height;

    @Schema(description = "深度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("深度")
    private Integer depth;

    @Schema(description = "原始宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("原始宽度")
    private Integer originWidth;

    @Schema(description = "原始高度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("原始高度")
    private Integer originHeight;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

}