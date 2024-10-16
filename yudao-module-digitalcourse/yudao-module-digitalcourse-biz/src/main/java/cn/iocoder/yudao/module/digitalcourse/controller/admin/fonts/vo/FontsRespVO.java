package cn.iocoder.yudao.module.digitalcourse.controller.admin.fonts.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 存储字体的信息，包括字体的别名、预览URL、名称等 Response VO")
@Data
@ExcelIgnoreUnannotated
public class FontsRespVO {

    @Schema(description = "字体ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16620")
    @ExcelProperty("字体ID")
    private Long id;

    @Schema(description = "字体别名", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("字体别名")
    private String alias;

    @Schema(description = "字体名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("字体名称")
    private String name;

    @Schema(description = "选择预览URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @ExcelProperty("选择预览URL")
    private String choicePreviewUrl;

    @Schema(description = "查看预览URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @ExcelProperty("查看预览URL")
    private String viewPreviewUrl;

    @Schema(description = "排序号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排序号")
    private Integer orderNo;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

}