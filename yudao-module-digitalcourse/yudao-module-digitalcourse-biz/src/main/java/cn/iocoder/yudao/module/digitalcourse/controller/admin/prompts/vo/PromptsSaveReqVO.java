package cn.iocoder.yudao.module.digitalcourse.controller.admin.prompts.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.*;
import java.util.*;

@Schema(description = "管理后台 - 存储提示词模板的信息，包括提示词的名称、类型、排序等信息新增/修改 Request VO")
@Data
public class PromptsSaveReqVO {

    @Schema(description = "提示词模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5213")
    private Long id;

    @Schema(description = "提示词名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "提示词名称不能为空")
    private String name;

    @Schema(description = "提示词内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "提示词内容不能为空")
    private String content;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序不能为空")
    private Integer order;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态不能为空")
    private Integer status;

}