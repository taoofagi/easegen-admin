package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenevoices;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 存储每个场景中的声音信息 DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_course_scene_voices")
@KeySequence("digitalcourse_course_scene_voices_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseSceneVoicesDO extends BaseDO {

    /**
     * 场景声音ID
     */
    @TableId
    private Long id;
    /**
     * 场景ID，关联digitalcourse_course_scenes表
     */
    private Integer sceneId;
    /**
     * 声音ID，关联digitalcourse_voices表
     */
    private Integer voiceId;

    private String entityId;
    /**
     * 音调（0-100）
     */
    private Integer tonePitch;
    /**
     * 声音类型
     *
     * 枚举 {@link TODO digitalcourse_course_scene_voices_voice_type 对应的类}
     */
    private Integer voiceType;
    /**
     * 语速
     */
    private Double speechRate;
    /**
     * 自定义名称
     */
    private String name;
    /**
     * 状态
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;

}