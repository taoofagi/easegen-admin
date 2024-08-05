package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "用户 APP - 存储每个场景中的声音信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppCourseSceneVoicesRespVO {

    @Schema(description = "场景声音ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17311")
    @ExcelProperty("场景声音ID")
    private Long id;

    @Schema(description = "场景ID，关联digitalcourse_course_scenes表", requiredMode = Schema.RequiredMode.REQUIRED, example = "26726")
    @ExcelProperty("场景ID，关联digitalcourse_course_scenes表")
    private Integer sceneId;

    @Schema(description = "声音ID，关联digitalcourse_voices表", requiredMode = Schema.RequiredMode.REQUIRED, example = "28531")
    @ExcelProperty("声音ID，关联digitalcourse_voices表")
    private Integer voiceId;

    @Schema(description = "音调（0-100）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("音调（0-100）")
    private Integer tonePitch;

    @Schema(description = "声音类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "声音类型", converter = DictConvert.class)
    @DictFormat("digitalcourse_course_scene_voices_voice_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer voiceType;

    @Schema(description = "语速", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("语速")
    private Double speechRate;

    @Schema(description = "自定义名称", example = "芋艿")
    @ExcelProperty("自定义名称")
    private String name;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

}