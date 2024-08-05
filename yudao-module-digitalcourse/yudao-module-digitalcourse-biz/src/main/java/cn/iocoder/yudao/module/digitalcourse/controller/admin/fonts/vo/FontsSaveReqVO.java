package cn.iocoder.yudao.module.digitalcourse.controller.admin.fonts.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.*;
import java.util.*;

@Schema(description = "管理后台 - 存储字体的信息，包括字体的别名、预览URL、名称等新增/修改 Request VO")
@Data
public class FontsSaveReqVO {

    @Schema(description = "字体ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16620")
    private Long id;

    @Schema(description = "字体别名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "字体别名不能为空")
    private String alias;

    @Schema(description = "字体名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "字体名称不能为空")
    private String name;

    @Schema(description = "选择预览URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotEmpty(message = "选择预览URL不能为空")
    private String choicePreviewUrl;

    @Schema(description = "查看预览URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotEmpty(message = "查看预览URL不能为空")
    private String viewPreviewUrl;

    @Schema(description = "排序号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排序号不能为空")
    private Integer orderNo;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态不能为空")
    private Integer status;

}