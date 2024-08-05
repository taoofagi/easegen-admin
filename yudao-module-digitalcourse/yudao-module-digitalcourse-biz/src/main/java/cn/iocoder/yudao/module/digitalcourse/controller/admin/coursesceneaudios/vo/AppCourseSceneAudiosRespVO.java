package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "用户 APP - 存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppCourseSceneAudiosRespVO {

    @Schema(description = "场景音频ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2031")
    @ExcelProperty("场景音频ID")
    private Long id;

    @Schema(description = "场景ID，关联digitalcourse_course_scenes表", requiredMode = Schema.RequiredMode.REQUIRED, example = "15047")
    @ExcelProperty("场景ID，关联digitalcourse_course_scenes表")
    private Integer sceneId;

    @Schema(description = "音频ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15881")
    @ExcelProperty("音频ID")
    private Integer audioId;

    @Schema(description = "是否使用视频背景音频", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "是否使用视频背景音频", converter = DictConvert.class)
    @DictFormat("use_video_background_audio") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer useVideoBackgroundAudio;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

}