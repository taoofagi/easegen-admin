package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenetexts;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 存储场景中的文本信息，包括文本内容、音调、速度等 DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_course_scene_texts")
@KeySequence("digitalcourse_course_scene_texts_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseSceneTextsDO extends BaseDO {

    /**
     * 场景文本ID
     */
    @TableId
    private Long id;
    /**
     * 场景ID，关联digitalcourse_course_scenes表
     */
    private Integer sceneId;
    /**
     * 音调
     */
    private Integer pitch;
    /**
     * 速度
     */
    private Double speed;
    /**
     * 音量
     */
    private Double volume;
    /**
     * 智能速度
     */
    private Double smartSpeed;
    /**
     * 语速
     */
    private Double speechRate;
    /**
     * 文本内容（JSON格式）
     */
    private String textJson;
    /**
     * 状态
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;

}