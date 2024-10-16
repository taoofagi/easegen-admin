package cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "用户 APP - 存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AppPptMaterialsRespVO {

    @Schema(description = "PPT课件ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23980")
    @ExcelProperty("PPT课件ID")
    private Long id;

    @Schema(description = "课程PPT ID，关联digitalcourse_course_ppts表", requiredMode = Schema.RequiredMode.REQUIRED, example = "2798")
    @ExcelProperty("课程PPT ID，关联digitalcourse_course_ppts表")
    private Integer pptId;

    @Schema(description = "课件名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("课件名称")
    private String name;

    @Schema(description = "背景类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "背景类型", converter = DictConvert.class)
    @DictFormat("digitalcourse_course_ppt_materials_background_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer backgroundType;

    @Schema(description = "图片URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @ExcelProperty("图片URL")
    private String pictureUrl;

    @Schema(description = "原始图片URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @ExcelProperty("原始图片URL")
    private String originalUrl;

    @Schema(description = "宽度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("宽度")
    private Integer width;

    @Schema(description = "高度", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("高度")
    private Integer height;

    @Schema(description = "页面索引", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("页面索引")
    private Integer indexNo;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("common_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

}