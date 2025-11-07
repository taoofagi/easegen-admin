package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courses;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 存储课程的基本信息，包括课程名称、时长、状态等 DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_courses")
@KeySequence("digitalcourse_courses_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursesDO extends BaseDO {

    /**
     * 课程ID
     */
    @TableId
    private Long id;
    /**
     * 账户ID
     */
    private Integer accountId;
    /**
     * 课程名称
     */
    private String name;
    /**
     * 屏幕比例
     */
    private String aspect;
    /**
     * 时长（秒）
     */
    private Integer duration;
    /**
     * 高度（像素）
     */
    private Integer height;
    /**
     * 宽度（像素）
     */
    private Integer width;
    /**
     * 是否抠图标识
     */
    private Integer matting;
    /**
     * 页面模式
     */
    private Integer pageMode;
    /**
     * 状态
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;
    /**
     * 页面信息
     */
    private String pageInfo;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 字幕样式
     */
    private String subtitlesStyle;
    /**
     * 平台类型：1-2D（easegen），2-3D（魔珐星云）
     */
    private Integer platformType;

}