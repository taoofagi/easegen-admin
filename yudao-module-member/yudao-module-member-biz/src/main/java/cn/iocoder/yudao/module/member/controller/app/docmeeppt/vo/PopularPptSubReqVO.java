package cn.iocoder.yudao.module.member.controller.app.docmeeppt.vo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopularPptSubReqVO {

    /**
     * 模板类型（必传）：1系统模板、4用户自定义模板
     */
    @Schema(description = "模板类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "模板类型不能为空")
    private Integer type;
    /**
     * 类目筛选 ['年终总结', '教育培训', '医学医疗', '商业计划书', '企业介绍', '毕业答辩', '营销推广', '晚会表彰', '个人简历']
     */
    private List<String> category;
    /**
     * 风格筛选 ['扁平简约', '商务科技', '文艺清新', '卡通手绘', '中国风', '创意时尚', '创意趣味']
     */
    private List<String> style;
    /**
     * 主题颜色筛选 ['#FA920A', '#589AFD', '#7664FA', '#65E5EC', '#61D328', '#F5FD59', '#E05757', '#8F5A0B', '#FFFFFF', '#000000']
     */
    private List<String> themeColor;

}
