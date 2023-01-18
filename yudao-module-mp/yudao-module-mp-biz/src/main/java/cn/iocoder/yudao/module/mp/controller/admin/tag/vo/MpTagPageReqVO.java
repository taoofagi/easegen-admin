package cn.iocoder.yudao.module.mp.controller.admin.tag.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@ApiModel("管理后台 - 公众号标签分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MpTagPageReqVO extends PageParam {

    @ApiModelProperty(value = "公众号账号的编号", required = true, example = "2048")
    @NotEmpty(message = "公众号账号的编号不能为空")
    private Long accountId;

    @ApiModelProperty(value = "标签名", example = "哈哈", notes = "模糊匹配")
    private String name;

}
