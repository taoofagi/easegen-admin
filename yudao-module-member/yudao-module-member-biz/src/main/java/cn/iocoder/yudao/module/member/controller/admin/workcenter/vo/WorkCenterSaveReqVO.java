package cn.iocoder.yudao.module.member.controller.admin.workcenter.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "管理后台 - 作品中心新增/修改 Request VO")
@Data
public class WorkCenterSaveReqVO {

    @Schema(description = "收件地址编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "21042")
    private Long id;

    @Schema(description = "行业")
    private String industry;

    @Schema(description = "场景")
    private String scene;

    @Schema(description = "语种")
    private String language;

    @Schema(description = "作品类型", example = "2")
    private String workType;

    @Schema(description = "作品时长")
    private Long workDuration;

    @Schema(description = "封面地址", example = "https://www.iocoder.cn")
    private String coverUrl;

    @Schema(description = "作品名称", example = "作品1")
    private String workName;

    private String workUrl;


}