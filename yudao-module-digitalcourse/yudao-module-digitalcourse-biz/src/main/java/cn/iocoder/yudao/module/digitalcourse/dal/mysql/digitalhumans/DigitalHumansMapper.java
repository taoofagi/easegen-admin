package cn.iocoder.yudao.module.digitalcourse.dal.mysql.digitalhumans;

import java.util.*;
import java.util.function.Consumer;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.digitalhumans.DigitalHumansDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.digitalhumans.vo.*;
import org.apache.ibatis.annotations.Param;

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
                .eqIfPresent(DigitalHumansDO::getCreator, reqVO.getCreator())
                .and(new Consumer<LambdaQueryWrapper<DigitalHumansDO>>() {
                    @Override
                    public void accept(LambdaQueryWrapper<DigitalHumansDO> digitalHumansDOLambdaQueryWrapper) {
                        digitalHumansDOLambdaQueryWrapper.gt(DigitalHumansDO::getExpireDate, reqVO.getExpireDate()).or().isNull(DigitalHumansDO::getExpireDate);
                    }
                })
                .orderByDesc(DigitalHumansDO::getId));
    }

    Integer auditing(@Param("creator") Long creator);

}