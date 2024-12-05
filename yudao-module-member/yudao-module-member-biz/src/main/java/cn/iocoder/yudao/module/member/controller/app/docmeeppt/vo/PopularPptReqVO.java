package cn.iocoder.yudao.module.member.controller.app.docmeeppt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "热门ppt模板请求参数")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopularPptReqVO {

    private Integer page;
    private Integer size;
    private PopularPptSubReqVO filters;

}
