package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.fonts;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 存储字体的信息，包括字体的别名、预览URL、名称等 DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_fonts")
@KeySequence("digitalcourse_fonts_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FontsDO extends BaseDO {

    /**
     * 字体ID
     */
    @TableId
    private Long id;
    /**
     * 字体别名
     */
    private String alias;
    /**
     * 字体名称
     */
    private String name;
    /**
     * 选择预览URL
     */
    private String choicePreviewUrl;
    /**
     * 查看预览URL
     */
    private String viewPreviewUrl;
    /**
     * 排序号
     */
    private Integer orderNo;
    /**
     * 状态
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;

}