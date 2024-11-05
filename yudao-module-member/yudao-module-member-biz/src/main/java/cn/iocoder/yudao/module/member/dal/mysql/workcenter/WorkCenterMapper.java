package cn.iocoder.yudao.module.member.dal.mysql.workcenter;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.member.controller.admin.workcenter.vo.WorkCenterPageReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.workcenter.WorkCenterDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 作品中心 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface WorkCenterMapper extends BaseMapperX<WorkCenterDO> {

    default PageResult<WorkCenterDO> selectPage(WorkCenterPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WorkCenterDO>()
                .betweenIfPresent(WorkCenterDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(WorkCenterDO::getIndustry, reqVO.getIndustry())
                .eqIfPresent(WorkCenterDO::getScene, reqVO.getScene())
                .eqIfPresent(WorkCenterDO::getLanguage, reqVO.getLanguage())
                .eqIfPresent(WorkCenterDO::getWorkType, reqVO.getWorkType())
                .eqIfPresent(WorkCenterDO::getWorkDuration, reqVO.getWorkDuration())
                .eqIfPresent(WorkCenterDO::getCoverUrl, reqVO.getCoverUrl())
                .eqIfPresent(WorkCenterDO::getWorkName, reqVO.getWorkName())
                .orderByDesc(WorkCenterDO::getId));
    }

}