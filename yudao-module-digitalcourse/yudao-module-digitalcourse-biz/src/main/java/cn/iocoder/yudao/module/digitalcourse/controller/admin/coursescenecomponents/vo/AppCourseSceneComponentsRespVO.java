package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenecomponents.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "用户 APP - 存储每个场景中的组件信息，包括PPT、数字人等 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppCourseSceneComponentsRespVO {

    @Schema(description = "组件ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5575")
    @ExcelProperty("组件ID")
    private Long id;

    @Schema(description = "场景ID，关联digitalcourse_course_scenes表", requiredMode = Schema.RequiredMode.REQUIRED, example = "22468")
    @ExcelProperty("场景ID，关联digitalcourse_course_scenes表")
    private Integer sceneId;

    @Schema(description = "组件名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("组件名称")
    private String name;

    @Schema(description = "资源URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("资源URL")
    private String src;

    @Schema(description = "封面URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("封面URL")
    private String cover;

    @Schema(description = "组件宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("组件宽度")
    private Integer width;

    @Schema(description = "组件高度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("组件高度")
    private Integer height;

    @Schema(description = "原始宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("原始宽度")
    private Integer originWidth;

    @Schema(description = "原始高度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("原始高度")
    private Integer originHeight;

    @Schema(description = "类别", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "类别", converter = DictConvert.class)
    @DictFormat("digitalcourse_course_scene_components_category") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer category;

    @Schema(description = "深度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("深度")
    private Integer depth;

    @Schema(description = "上边距", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("上边距")
    private Integer top;

    @Schema(description = "左边距", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("左边距")
    private Integer marginLeft;

    @Schema(description = "实体ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31129")
    @ExcelProperty("实体ID")
    private String entityId;

    @Schema(description = "实体类型 (0: 其他, 1: 数字人)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "实体类型 (0: 其他, 1: 数字人)", converter = DictConvert.class)
    @DictFormat("digitalcourse_course_scene_components_entity_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer entityType;

    @Schema(description = "业务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5116")
    @ExcelProperty("业务ID")
    private String businessId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "状态 (0: 正常, 1: 异常)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "状态 (0: 正常, 1: 异常)", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

}