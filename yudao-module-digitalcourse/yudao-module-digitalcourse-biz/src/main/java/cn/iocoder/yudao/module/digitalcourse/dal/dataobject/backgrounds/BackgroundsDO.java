package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.backgrounds;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 背景信息（PPT背景、板书、插图、字幕等） DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_backgrounds")
@KeySequence("digitalcourse_backgrounds_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackgroundsDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 背景类型
     *
     * 枚举 {@link TODO digitalcourse_backgrounds_type 对应的类}
     */
    private Integer backgroundType;
    /**
     * 背景名称
     */
    private String name;
    /**
     * 原始图片URL
     */
    private String originalUrl;
    /**
     * 图片URL
     */
    private String pictureUrl;
    /**
     * 图片宽度
     */
    private Integer width;
    /**
     * 图片高度
     */
    private Integer height;
    /**
     * 文件大小 
     */
    private Long size;
    /**
     * 时长
     */
    private Integer duration;
    /**
     * 预设标识
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Integer preset;
    /**
     * 状态
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;
    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 口播稿内容
     */
    private String pptRemark;

}