package cn.iocoder.yudao.module.ai.controller.app.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * chat 角色列表
 *
 * @fansili
 * @since v1.0
 */
@Data
@Accessors(chain = true)
public class ChatRoleAddReq extends PageParam {

    @Schema(description = "查询")
    private String search;
}
