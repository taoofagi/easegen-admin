package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.pptmaterials;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等 DO
 *
 * @author 芋道源码
 */
@TableName("digitalcourse_ppt_materials")
@KeySequence("digitalcourse_ppt_materials_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PptMaterialsDO extends BaseDO {

    /**
     * PPT课件ID
     */
    @TableId
    private Long id;
    /**
     * 课程PPT ID，关联digitalcourse_course_ppts表
     */
    private Long pptId;
    /**
     * 课件名称
     */
    private String name;
    /**
     * 背景类型
     *
     * 枚举 {@link TODO digitalcourse_course_ppt_materials_background_type 对应的类}
     */
    private Integer backgroundType;
    /**
     * 图片URL
     */
    private String pictureUrl;
    /**
     * 原始图片URL
     */
    private String originalUrl;
    /**
     * 宽度
     */
    private Integer width;
    /**
     * 高度
     */
    private Integer height;
    /**
     * 页面索引
     */
    private Integer indexNo;
    /**
     * 状态
     *
     * 枚举 {@link TODO common_status 对应的类}
     */
    private Integer status;

    private String pptRemark;

}