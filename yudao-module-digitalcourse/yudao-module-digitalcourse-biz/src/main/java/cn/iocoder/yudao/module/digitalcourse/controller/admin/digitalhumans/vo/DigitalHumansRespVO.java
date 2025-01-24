package cn.iocoder.yudao.module.digitalcourse.controller.admin.digitalhumans.vo;

import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 数字人模型 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DigitalHumansRespVO implements VO {

    @Schema(description = "课程PPT ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17318")
    @ExcelProperty("课程PPT ID")
    private Long id;

    @Schema(description = "过期状态")
    @ExcelProperty(value = "过期状态", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private String expireStatus;

    @Schema(description = "完成时间")
    @ExcelProperty("完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "性别", requiredMode = Schema.RequiredMode.REQUIRED, example = "男")
    @ExcelProperty(value = "性别", converter = DictConvert.class)
    @DictFormat("system_user_sex") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer gender;

    @Schema(description = "数字人名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("数字人名称")
    private String name;

    @Schema(description = "数字人编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "zhangsan")
    @ExcelProperty("数字人编码")
    private String code;

    @Schema(description = "姿势", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "姿势", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer posture;

    @Schema(description = "快照高度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("快照高度")
    private Integer snapshotHeight;

    @Schema(description = "快照URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @ExcelProperty("快照URL")
    private String snapshotUrl;

    @Schema(description = "快照宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("快照宽度")
    private Integer snapshotWidth;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "类型", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer type;

    @Schema(description = "使用通用模型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "使用通用模型", converter = DictConvert.class)
    @DictFormat("system_user_sex") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer useGeneralModel;

    @Schema(description = "使用模型类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("使用模型类型")
    private String useModel;

    @Schema(description = "图片URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("图片URL")
    private String pictureUrl;
    @Schema(description = "视频URL", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("视频URL")
    private String videoUrl;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @Trans(type = TransType.SIMPLE, targetClassName = "cn.iocoder.yudao.module.system.dal.dataobject.user.AdminUserDO",
            fields = "nickname", ref = "creatorName")
    private String creator;

    private String creatorName;

    @Schema(description = "状态(0: 正常, 1: 待审核，2：已受理，3：训练中，4：不通过，5：训练失败)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "状态(0: 正常, 1: 待审核，2：已受理，3：训练中，4：不通过，5：训练失败)", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;
    //修复图片
    private String fixPictureUrl;
    //修复视频
    private String fixVideoUrl;
    //过期时间
    private Date expireDate;

}