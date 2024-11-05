package cn.iocoder.yudao.module.member.controller.admin.workcenter.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 作品中心 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WorkCenterRespVO {

    @Schema(description = "收件地址编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "21042")
    @ExcelProperty("收件地址编号")
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "作品名称", example = "作品1")
    @ExcelProperty("作品名称")
    private String workName;

    @Schema(description = "行业")
    @ExcelProperty("行业")
    private Integer industry;

    @Schema(description = "场景")
    @ExcelProperty("场景")
    private Integer scene;

    @Schema(description = "语种")
    @ExcelProperty("语种")
    private Integer language;

    @Schema(description = "作品类型", example = "2")
    @ExcelProperty("作品类型")
    private String workType;

    @Schema(description = "作品时长")
    @ExcelProperty("作品时长")
    private Long workDuration;

    @Schema(description = "封面地址", example = "https://www.iocoder.cn")
    @ExcelProperty("封面地址")
    private String coverUrl;

    private String workUrl;



}