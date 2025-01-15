package cn.iocoder.yudao.module.digitalcourse.controller.admin.digitalhumans.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 数字人模型新增/修改 Request VO")
@Data
public class DigitalHumansSaveReqVO {

    @Schema(description = "课程PPT ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17318")
    private Long id;

    @Schema(description = "过期状态")
    private String expireStatus;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "抠图标识")
    private Integer matting;

    @Schema(description = "数字人名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "数字人名称不能为空")
    private String name;

    @Schema(description = "数字人编码")
    private String code;

    @Schema(description = "图片URL", example = "https://www.iocoder.cn")
    private String pictureUrl;
    @Schema(description = "视频URL",  example = "https://www.iocoder.cn")
    private String videoUrl;

    @Schema(description = "姿势", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "姿势不能为空")
    private Integer posture;

    @Schema(description = "快照高度", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer snapshotHeight;

    @Schema(description = "快照URL", requiredMode = Schema.RequiredMode.REQUIRED)
    private String snapshotUrl;

    @Schema(description = "快照宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer snapshotWidth;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @Schema(description = "使用通用模型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer useGeneralModel;

    @Schema(description = "使用模型类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String useModel;

    @Schema(description = "状态(0: 正常, 1: 待审核，2：已受理，3：训练中，4：不通过，5：训练失败)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态不能为空")
    private Integer status;

    //修复图片
    private String fixPictureUrl;
    //修复视频
    private String fixVideoUrl;

}