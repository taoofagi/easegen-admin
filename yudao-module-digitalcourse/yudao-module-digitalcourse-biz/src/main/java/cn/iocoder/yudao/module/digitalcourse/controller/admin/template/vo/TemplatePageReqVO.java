package cn.iocoder.yudao.module.digitalcourse.controller.admin.template.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 模板分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TemplatePageReqVO extends PageParam {

    @Schema(description = "0 不显示，1显示")
    private Integer showBackground;

    @Schema(description = "0 不显示，1显示")
    private Integer showDigitalHuman;

    @Schema(description = "0 不显示，1显示")
    private Integer showPpt;

    @Schema(description = "ppt宽")
    private BigDecimal pptW;

    @Schema(description = "ppt高")
    private BigDecimal pptH;

    @Schema(description = "ppt距离顶部位置")
    private BigDecimal pptX;

    @Schema(description = "ppt距离左侧位置")
    private BigDecimal pptY;

    @Schema(description = "数字人宽")
    private BigDecimal humanW;

    @Schema(description = "数字人高")
    private BigDecimal humanH;

    @Schema(description = "数字人距离顶部位置")
    private BigDecimal humanX;

    @Schema(description = "数字人距离左侧位置")
    private BigDecimal humanY;

    @Schema(description = "背景图片")
    private String bgImage;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    /**
     * 效果图
     */
    @Schema(description = "效果图")
    private String previewImage;

}