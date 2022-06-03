package cn.iocoder.yudao.module.mp.controller.admin.accountfanstag.vo;

import lombok.*;

import java.util.*;

import io.swagger.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@ApiModel(value = "管理后台 - 粉丝标签关联 Excel 导出 Request VO", description = "参数和 WxAccountFansTagPageReqVO 是一致的")
@Data
public class WxAccountFansTagExportReqVO {

    @ApiModelProperty(value = "用户标识")
    private String openid;

    @ApiModelProperty(value = "标签ID")
    private String tagId;

    @ApiModelProperty(value = "微信账号ID")
    private String wxAccountId;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ApiModelProperty(value = "开始创建时间")
    private Date beginCreateTime;

    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ApiModelProperty(value = "结束创建时间")
    private Date endCreateTime;

}
