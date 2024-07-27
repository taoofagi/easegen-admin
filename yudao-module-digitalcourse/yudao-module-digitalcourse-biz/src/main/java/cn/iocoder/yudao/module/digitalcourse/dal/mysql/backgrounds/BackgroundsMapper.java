package cn.iocoder.yudao.module.digitalcourse.dal.mysql.backgrounds;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.backgrounds.BackgroundsDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.backgrounds.vo.*;

/**
 * 背景信息（PPT背景、板书、插图、字幕等） Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface BackgroundsMapper extends BaseMapperX<BackgroundsDO> {

    default PageResult<BackgroundsDO> selectPage(BackgroundsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BackgroundsDO>()
                .eqIfPresent(BackgroundsDO::getBackgroundType, reqVO.getBackgroundType())
                .likeIfPresent(BackgroundsDO::getName, reqVO.getName())
                .eqIfPresent(BackgroundsDO::getPreset, reqVO.getPreset())
                .eqIfPresent(BackgroundsDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BackgroundsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BackgroundsDO::getId));
    }

}