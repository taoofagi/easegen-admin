package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 课程媒体 DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_course_media")
@KeySequence("digitalcourse_course_media_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseMediaDO extends BaseDO {

    /**
     * 编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 状态 (1、合成中，2、成功，3、失败)
     */
    private Integer status;
    /**
     * 时长
     */
    private Long duration;
    /**
     * 合成时间
     */
    private String finishTime;
    /**
     * 媒体类型
     */
    private Integer mediaType;
    /**
     * 名称
     */
    private String name;
    /**
     * 媒体链接
     */
    private String previewUrl;
    /**
     * 进度（预留）
     */
    private Float progress;
    /**
     * 课程id
     */
    private Long courseId;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 请求报文
     */
    private String reqJson;
    /**
     * 错误原因
     */
    private String errorReason;
    /**
     * 字幕文件
     */
    private String subtitlesUrl;

    /**
     * vtt字幕文件
     */
    private String subtitlesVttUrl;
    /**
     * 字幕样式
     */
    private String subtitlesStyle;

    private String thumbnail;
}