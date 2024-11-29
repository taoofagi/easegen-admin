package cn.iocoder.yudao.module.member.controller.app.docmeeppt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageReqVO {

    @Schema(description = "当前页码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "当前页码不能为空")
    private Integer page;
    @Schema(description = "分页大小", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "分页大小不能为空")
    private Integer size;
}
