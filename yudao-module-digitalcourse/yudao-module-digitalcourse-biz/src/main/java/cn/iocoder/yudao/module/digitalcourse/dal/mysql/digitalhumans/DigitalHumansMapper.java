package cn.iocoder.yudao.module.digitalcourse.dal.mysql.digitalhumans;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.digitalhumans.DigitalHumansDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.digitalhumans.vo.*;

/**
 * 数字人模型 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DigitalHumansMapper extends BaseMapperX<DigitalHumansDO> {

    default PageResult<DigitalHumansDO> selectPage(DigitalHumansPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DigitalHumansDO>()
                .eqIfPresent(DigitalHumansDO::getGender, reqVO.getGender())
                .likeIfPresent(DigitalHumansDO::getName, reqVO.getName())
                .eqIfPresent(DigitalHumansDO::getPosture, reqVO.getPosture())
                .eqIfPresent(DigitalHumansDO::getType, reqVO.getType())
                .eqIfPresent(DigitalHumansDO::getUseModel, reqVO.getUseModel())
                .eqIfPresent(DigitalHumansDO::getStatus, reqVO.getStatus())
                .orderByDesc(DigitalHumansDO::getId));
    }

}