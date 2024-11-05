package cn.iocoder.yudao.module.member.dal.dataobject.workcenter;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 作品中心 DO
 *
 * @author 管理员
 */
@TableName("member_work_center")
@KeySequence("member_work_center_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkCenterDO extends BaseDO {

    /**
     * 收件地址编号
     */
    @TableId
    private Long id;
    /**
     * 行业
     */
    private String industry;
    /**
     * 场景
     */
    private String scene;
    /**
     * 语种
     */
    private String language;
    /**
     * 作品类型
     */
    private String workType;
    /**
     * 作品时长
     */
    private Long workDuration;
    /**
     * 封面地址
     */
    private String coverUrl;

    private String workName;

    private String workUrl;

}