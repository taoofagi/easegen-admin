package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenecomponents;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 存储每个场景中的组件信息，包括PPT、数字人等 DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_course_scene_components")
@KeySequence("digitalcourse_course_scene_components_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseSceneComponentsDO extends BaseDO {

    /**
     * 组件ID
     */
    @TableId
    private Long id;
    /**
     * 场景ID，关联digitalcourse_course_scenes表
     */
    private Integer sceneId;
    /**
     * 组件名称
     */
    private String name;
    /**
     * 资源URL
     */
    private String src;
    /**
     * 封面URL
     */
    private String cover;
    /**
     * 组件宽度
     */
    private Integer width;
    /**
     * 组件高度
     */
    private Integer height;
    /**
     * 原始宽度
     */
    private Integer originWidth;
    /**
     * 原始高度
     */
    private Integer originHeight;
    /**
     * 类别
     *
     * 枚举 {@link TODO digitalcourse_course_scene_components_category 对应的类}
     */
    private Integer category;
    /**
     * 深度
     */
    private Integer depth;
    /**
     * 上边距
     */
    private Integer top;
    /**
     * 左边距
     */
    private Integer marginLeft;
    /**
     * 实体ID
     */
    private String entityId;
    /**
     * 实体类型 (0: 其他, 1: 数字人)
     *
     * 枚举 {@link TODO digitalcourse_course_scene_components_entity_type 对应的类}
     */
    private Integer entityType;
    /**
     * 业务ID
     */
    private String businessId;
    /**
     * 状态 (0: 正常, 1: 异常)
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;


    private Integer digitbotType;

    private Integer matting;

    private Boolean marker;

}