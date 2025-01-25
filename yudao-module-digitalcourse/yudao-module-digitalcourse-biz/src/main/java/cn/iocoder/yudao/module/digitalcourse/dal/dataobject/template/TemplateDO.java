package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.template;

import lombok.*;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 模板 DO
 *
 * @author 管理员
 */
@TableName("digitalcourse_template")
@KeySequence("digitalcourse_template_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 0 不显示，1显示
     */
    private Integer showBackground;
    /**
     * 0 不显示，1显示
     */
    private Integer showDigitalHuman;
    /**
     * 0 不显示，1显示
     */
    private Integer showPpt;
    /**
     * ppt宽
     */
    private BigDecimal pptW;
    /**
     * ppt高
     */
    private BigDecimal pptH;
    /**
     * ppt距离顶部位置
     */
    private BigDecimal pptX;
    /**
     * ppt距离左侧位置
     */
    private BigDecimal pptY;
    /**
     * 数字人宽
     */
    private BigDecimal humanW;
    /**
     * 数字人高
     */
    private BigDecimal humanH;
    /**
     * 数字人距离顶部位置
     */
    private BigDecimal humanX;
    /**
     * 数字人距离左侧位置
     */
    private BigDecimal humanY;
    /**
     * 背景图片
     */
    private String bgImage;

    /**
     * 效果图
     */
    private String previewImage;
    /**
     * 模板名称
     */
    private String templateName;

    // 模板尺寸
    private String templateSize;

}