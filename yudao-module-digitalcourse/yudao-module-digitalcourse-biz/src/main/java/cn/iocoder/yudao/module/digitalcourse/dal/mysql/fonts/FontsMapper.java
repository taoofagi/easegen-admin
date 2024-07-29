package cn.iocoder.yudao.module.digitalcourse.dal.mysql.fonts;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.fonts.FontsDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.fonts.vo.*;

/**
 * 存储字体的信息，包括字体的别名、预览URL、名称等 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface FontsMapper extends BaseMapperX<FontsDO> {

    default PageResult<FontsDO> selectPage(FontsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FontsDO>()
                .eqIfPresent(FontsDO::getAlias, reqVO.getAlias())
                .likeIfPresent(FontsDO::getName, reqVO.getName())
                .eqIfPresent(FontsDO::getChoicePreviewUrl, reqVO.getChoicePreviewUrl())
                .eqIfPresent(FontsDO::getViewPreviewUrl, reqVO.getViewPreviewUrl())
                .eqIfPresent(FontsDO::getOrderNo, reqVO.getOrderNo())
                .betweenIfPresent(FontsDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(FontsDO::getStatus, reqVO.getStatus())
                .orderByDesc(FontsDO::getId));
    }

}