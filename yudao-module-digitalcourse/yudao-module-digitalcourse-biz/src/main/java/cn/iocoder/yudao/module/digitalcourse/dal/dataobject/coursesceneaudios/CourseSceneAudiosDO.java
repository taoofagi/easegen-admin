package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursesceneaudios;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等 DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_course_scene_audios")
@KeySequence("digitalcourse_course_scene_audios_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseSceneAudiosDO extends BaseDO {

    /**
     * 场景音频ID
     */
    @TableId
    private Long id;
    /**
     * 场景ID，关联digitalcourse_course_scenes表
     */
    private Integer sceneId;
    /**
     * 音频ID
     */
    private Integer audioId;
    /**
     * 是否使用视频背景音频
     *
     * 枚举 {@link TODO use_video_background_audio 对应的类}
     */
    private Integer useVideoBackgroundAudio;
    /**
     * 状态
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;

    private String audioUrl;
    private String fileName;

}