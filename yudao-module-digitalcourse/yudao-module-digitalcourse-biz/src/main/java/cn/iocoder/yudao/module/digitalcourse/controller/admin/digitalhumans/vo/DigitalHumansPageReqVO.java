package cn.iocoder.yudao.module.digitalcourse.controller.admin.digitalhumans.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 数字人模型分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DigitalHumansPageReqVO extends PageParam {

    @Schema(description = "性别", example = "男")
    private Integer gender;

    @Schema(description = "数字人名称", example = "张三")
    private String name;

    @Schema(description = "姿势")
    private Integer posture;

    @Schema(description = "类型", example = "1")
    private Integer type;

    @Schema(description = "使用模型类型")
    private String useModel;

    @Schema(description = "状态(0: 正常, 1: 待审核，2：已受理，3：训练中，4：不通过，5：训练失败)", example = "2")
    private Integer status;
    @Schema(description = "创建人", example = "2")
    private String creator;

    private Date expireDate = new Date();

}