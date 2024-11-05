package cn.iocoder.yudao.module.digitalcourse.dal.mysql.template;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.template.TemplateDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.template.vo.*;

/**
 * 模板 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface TemplateMapper extends BaseMapperX<TemplateDO> {

    default PageResult<TemplateDO> selectPage(TemplatePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TemplateDO>()
                .eqIfPresent(TemplateDO::getShowBackground, reqVO.getShowBackground())
                .eqIfPresent(TemplateDO::getShowDigitalHuman, reqVO.getShowDigitalHuman())
                .eqIfPresent(TemplateDO::getShowPpt, reqVO.getShowPpt())
                .eqIfPresent(TemplateDO::getPptW, reqVO.getPptW())
                .eqIfPresent(TemplateDO::getPptH, reqVO.getPptH())
                .eqIfPresent(TemplateDO::getPptX, reqVO.getPptX())
                .eqIfPresent(TemplateDO::getPptY, reqVO.getPptY())
                .eqIfPresent(TemplateDO::getHumanW, reqVO.getHumanW())
                .eqIfPresent(TemplateDO::getHumanH, reqVO.getHumanH())
                .eqIfPresent(TemplateDO::getHumanX, reqVO.getHumanX())
                .eqIfPresent(TemplateDO::getHumanY, reqVO.getHumanY())
                .eqIfPresent(TemplateDO::getBgImage, reqVO.getBgImage())
                .betweenIfPresent(TemplateDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TemplateDO::getId));
    }

}