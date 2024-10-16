package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "用户 APP - 存储课程的场景信息，包括背景、组件、声音等 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppCourseScenesRespVO {

    @Schema(description = "场景ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16122")
    @ExcelProperty("场景ID")
    private Long id;

    @Schema(description = "课程ID，关联digitalcourse_courses表", requiredMode = Schema.RequiredMode.REQUIRED, example = "15012")
    @ExcelProperty("课程ID，关联digitalcourse_courses表")
    private Integer courseId;

    @Schema(description = "场景顺序号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("场景顺序号")
    private Integer orderNo;

    @Schema(description = "时长（秒）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("时长（秒）")
    private Integer duration;

    @Schema(description = "驱动类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("驱动类型")
    private Integer driverType;

    @Schema(description = "业务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23586")
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