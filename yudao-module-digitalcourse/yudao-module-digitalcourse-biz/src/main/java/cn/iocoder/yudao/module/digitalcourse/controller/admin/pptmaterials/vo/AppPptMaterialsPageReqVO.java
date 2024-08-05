package cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "用户 APP - 存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppPptMaterialsPageReqVO extends PageParam {

    @Schema(description = "课件名称", example = "李四")
    private String name;

    @Schema(description = "背景类型", example = "1")
    private Integer backgroundType;

    @Schema(description = "图片URL", example = "https://www.iocoder.cn")
    private String pictureUrl;

    @Schema(description = "原始图片URL", example = "https://www.iocoder.cn")
    private String originalUrl;

    @Schema(description = "宽度")
    private Integer width;

    @Schema(description = "高度")
    private Integer height;

    @Schema(description = "页面索引")
    private Integer indexNo;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "状态", example = "1")
    private Integer status;

}