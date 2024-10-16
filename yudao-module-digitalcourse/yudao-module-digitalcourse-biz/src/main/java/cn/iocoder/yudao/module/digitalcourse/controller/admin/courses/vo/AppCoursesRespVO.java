package cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "用户 APP - 存储课程的基本信息，包括课程名称、时长、状态等 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppCoursesRespVO {

    @Schema(description = "课程ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30853")
    @ExcelProperty("课程ID")
    private Long id;

    @Schema(description = "账户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "361")
    @ExcelProperty("账户ID")
    private Integer accountId;

    @Schema(description = "课程名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("课程名称")
    private String name;

    @Schema(description = "屏幕比例", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("屏幕比例")
    private String aspect;

    @Schema(description = "时长（秒）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("时长（秒）")
    private Integer duration;

    @Schema(description = "高度（像素）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("高度（像素）")
    private Integer height;

    @Schema(description = "宽度（像素）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("宽度（像素）")
    private Integer width;

    @Schema(description = "是否抠图标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否抠图标识")
    private Integer matting;

    @Schema(description = "页面模式", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("页面模式")
    private Integer pageMode;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "页面信息", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("页面信息")
    private String pageInfo;

    @Schema(description = "缩略图", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @ExcelProperty("页面信息")
    private String thumbnail;

    @Schema(description = "字幕样式", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("字幕样式")
    private String subtitlesStyle;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}