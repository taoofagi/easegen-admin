package cn.iocoder.yudao.module.digitalcourse.controller.admin.prompts.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 存储提示词模板的信息，包括提示词的名称、类型、排序等信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PromptsRespVO {

    @Schema(description = "提示词模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5213")
    @ExcelProperty("提示词模板ID")
    private Long id;

    @Schema(description = "提示词名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("提示词名称")
    private String name;

    @Schema(description = "提示词内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("提示词内容")
    private String content;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排序")
    private Integer order;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("类型")
    private Integer type;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

}