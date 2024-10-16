package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenebackgrounds;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 存储每个场景的背景信息 DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_course_scene_backgrounds")
@KeySequence("digitalcourse_course_scene_backgrounds_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseSceneBackgroundsDO extends BaseDO {

    /**
     * 场景背景ID
     */
    @TableId
    private Long id;
    /**
     * 场景ID，关联digitalcourse_course_scenes表
     */
    private Integer sceneId;
    /**
     * 背景类型
     *
     * 枚举 {@link TODO digitalcourse_backgrounds_type 对应的类}
     */
    private Integer backgroundType;
    /**
     * 背景实体ID
     */
    private Integer entityId;
    /**
     * 背景资源URL
     */
    private String src;
    /**
     * 背景封面URL
     */
    private String cover;
    /**
     * 背景宽度
     */
    private Integer width;
    /**
     * 背景高度
     */
    private Integer height;
    /**
     * 深度
     */
    private Integer depth;
    /**
     * 原始宽度
     */
    private Integer originWidth;
    /**
     * 原始高度
     */
    private Integer originHeight;
    /**
     * 状态
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;

    private String pptRemark;

    private String color;

}