package cn.iocoder.yudao.module.digitalcourse.controller.admin.voices.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 声音管理 Response VO")
@Data
@ExcelIgnoreUnannotated
public class VoicesRespVO {

    @Schema(description = "声音ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2413")
    @ExcelProperty("声音ID")
    private Long id;

    @Schema(description = "声音名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("声音名称")
    private String name;

    @Schema(description = "声音编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("声音编码")
    private String code;

    @Schema(description = "试听URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "试听URL不能为空")
    private String auditionUrl;

    @Schema(description = "头像URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "头像URL不能为空")
    private String avatarUrl;

    @Schema(description = "语言类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "语言类型", converter = DictConvert.class)
    @DictFormat("digitalcourse_voices_language") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String language;

    @Schema(description = "性别", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "性别", converter = DictConvert.class)
    @DictFormat("system_user_sex") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer gender;

    @Schema(description = "介绍")
    private String introduction;

    @Schema(description = "音质评分", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @ExcelProperty("音质评分")
    private Integer quality;

    @Schema(description = "声音类型 ", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "声音类型 ", converter = DictConvert.class)
    @DictFormat("digitalcourse_voices_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer voiceType;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "状态 (0: 正常, 1: 待审核，2：已受理，3：训练中，4：不通过，5：训练失败)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态 (0: 正常, 1: 待审核，2：已受理，3：训练中，4：不通过，5：训练失败)")
    private Integer status;

    private String fixAuditionUrl;

    private Date expireDate;

    @Schema(description = "克隆类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

}