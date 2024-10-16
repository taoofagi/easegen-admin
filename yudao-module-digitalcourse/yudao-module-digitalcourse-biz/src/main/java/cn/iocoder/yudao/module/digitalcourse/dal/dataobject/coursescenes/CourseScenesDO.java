package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenes;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 存储课程的场景信息，包括背景、组件、声音等 DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_course_scenes")
@KeySequence("digitalcourse_course_scenes_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseScenesDO extends BaseDO {

    /**
     * 场景ID
     */
    @TableId
    private Long id;
    /**
     * 课程ID，关联digitalcourse_courses表
     */
    private Integer courseId;
    /**
     * 场景顺序号
     */
    private Integer orderNo;
    /**
     * 时长（秒）
     */
    private Integer duration;
    /**
     * 驱动类型
     */
    private Integer driverType;
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

}