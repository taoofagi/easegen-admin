package cn.iocoder.yudao.module.digitalcourse.controller.admin.voices.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 声音管理新增/修改 Request VO")
@Data
public class VoicesSaveReqVO {

    @Schema(description = "声音ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2413")
    private Long id;

    @Schema(description = "声音名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "声音名称不能为空")
    private String name;

    @Schema(description = "声音编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "声音编码不能为空")
    private String code;

    @Schema(description = "试听URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "试听URL不能为空")
    private String auditionUrl;

    @Schema(description = "头像URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "头像URL不能为空")
    private String avatarUrl;

    @Schema(description = "语言类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "语言类型不能为空")
    private String language;

    @Schema(description = "性别", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "性别不能为空")
    private Integer gender;

    @Schema(description = "介绍")
    private String introduction;

    @Schema(description = "音质评分", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "音质评分不能为空")
    private Integer quality;

    @Schema(description = "声音类型 ", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "声音类型 不能为空")
    private Integer voiceType;

    @Schema(description = "状态 (0: 正常, 1: 异常)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态 (0: 正常, 1: 异常)不能为空")
    private Integer status;

}