package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.voices;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 声音管理 DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_voices")
@KeySequence("digitalcourse_voices_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoicesDO extends BaseDO {

    /**
     * 声音ID
     */
    @TableId
    private Long id;
    /**
     * 声音名称
     */
    private String name;
    /**
     * 声音编码
     */
    private String code;
    /**
     * 试听URL
     */
    private String auditionUrl;
    /**
     * 头像URL
     */
    private String avatarUrl;
    /**
     * 语言类型
     *
     * 枚举 {@link TODO digitalcourse_voices_language 对应的类}
     */
    private String language;
    /**
     * 性别
     *
     * 枚举 {@link TODO system_user_sex 对应的类}
     */
    private Integer gender;
    /**
     * 介绍
     */
    private String introduction;
    /**
     * 音质评分
     */
    private Integer quality;
    /**
     * 声音类型 
     *
     * 枚举 {@link TODO digitalcourse_voices_type 对应的类}
     */
    private Integer voiceType;
    /**
     * 状态 (0: 正常, 1: 异常)
     */
    private Integer status;

    private String fixAuditionUrl;

}