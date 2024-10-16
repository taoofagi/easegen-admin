package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.digitalhumans;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 数字人模型 DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_digital_humans")
@KeySequence("digitalcourse_digital_humans_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalHumansDO extends BaseDO {

    /**
     * 课程PPT ID
     */
    @TableId
    private Long id;
    /**
     * 账户ID
     */
    private Integer accountId;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 过期状态
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private String expireStatus;
    /**
     * 完成时间
     */
    private LocalDateTime finishTime;
    /**
     * 性别
     *
     * 枚举 {@link TODO system_user_sex 对应的类}
     */
    private Integer gender;
    /**
     * 抠图标识
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Integer matting;
    /**
     * 模型列表
     */
    private String models;
    /**
     * 数字人名称
     */
    private String name;
    /**
     * 数字人编码
     */
    private String code;
    /**
     * 图片URL
     */
    private String pictureUrl;
    /**
     * 姿势
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Integer posture;
    /**
     * 快照高度
     */
    private Integer snapshotHeight;
    /**
     * 快照URL
     */
    private String snapshotUrl;
    /**
     * 快照宽度
     */
    private Integer snapshotWidth;
    /**
     * 提交ID
     */
    private String submitId;
    /**
     * 类型
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Integer type;
    /**
     * 使用通用模型
     *
     * 枚举 {@link TODO system_user_sex 对应的类}
     */
    private Integer useGeneralModel;
    /**
     * 使用模型类型
     */
    private String useModel;
    /**
     * 状态
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;

}