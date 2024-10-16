package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.prompts;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 存储提示词模板的信息，包括提示词的名称、类型、排序等信息 DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_prompts")
@KeySequence("digitalcourse_prompts_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptsDO extends BaseDO {

    /**
     * 提示词模板ID
     */
    @TableId
    private Long id;
    /**
     * 提示词名称
     */
    private String name;
    /**
     * 提示词内容
     */
    private String content;
    /**
     * 排序
     */
    private Integer order;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 状态
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;

}