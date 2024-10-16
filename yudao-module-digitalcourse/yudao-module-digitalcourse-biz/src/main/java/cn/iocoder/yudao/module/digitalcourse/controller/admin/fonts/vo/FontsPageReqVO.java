package cn.iocoder.yudao.module.digitalcourse.controller.admin.fonts.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 存储字体的信息，包括字体的别名、预览URL、名称等分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FontsPageReqVO extends PageParam {

    @Schema(description = "字体别名")
    private String alias;

    @Schema(description = "字体名称", example = "李四")
    private String name;

    @Schema(description = "选择预览URL", example = "https://www.iocoder.cn")
    private String choicePreviewUrl;

    @Schema(description = "查看预览URL", example = "https://www.iocoder.cn")
    private String viewPreviewUrl;

    @Schema(description = "排序号")
    private Integer orderNo;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "状态", example = "2")
    private Integer status;

}