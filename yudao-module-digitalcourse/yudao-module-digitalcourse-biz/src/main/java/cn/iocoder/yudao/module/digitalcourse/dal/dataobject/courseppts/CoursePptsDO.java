package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courseppts;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 存储课程的PPT信息，包括文件名、文件大小、类型等 DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_course_ppts")
@KeySequence("digitalcourse_course_ppts_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursePptsDO extends BaseDO {

    /**
     * 课程PPT ID
     */
    @TableId
    private Long id;
    /**
     * 课程ID，关联digitalcourse_courses表
     */
    private Integer courseId;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 文件大小（字节）
     */
    private Long size;
    /**
     * 文件MD5校验值
     */
    private String md5;
    /**
     * 文档类型
     */
    private Integer docType;
    /**
     * 扩展信息
     */
    private String extInfo;
    /**
     * 文件路径
     */
    private String url;
    /**
     * 解析类型
     */
    private Integer resolveType;
    /**
     * 状态 (0: 正常, 1: 异常)
     */
    private Integer status;
    /**
     * PPT总页数
     */
    private Integer pageSize;

}