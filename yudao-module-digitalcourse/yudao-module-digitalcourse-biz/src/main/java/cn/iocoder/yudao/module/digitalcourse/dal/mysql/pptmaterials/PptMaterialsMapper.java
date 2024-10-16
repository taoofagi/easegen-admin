package cn.iocoder.yudao.module.digitalcourse.dal.mysql.pptmaterials;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials.vo.AppPptMaterialsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.pptmaterials.PptMaterialsDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface PptMaterialsMapper extends BaseMapperX<PptMaterialsDO> {

    default PageResult<PptMaterialsDO> selectPage(AppPptMaterialsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PptMaterialsDO>()
                .likeIfPresent(PptMaterialsDO::getName, reqVO.getName())
                .eqIfPresent(PptMaterialsDO::getBackgroundType, reqVO.getBackgroundType())
                .eqIfPresent(PptMaterialsDO::getPictureUrl, reqVO.getPictureUrl())
                .eqIfPresent(PptMaterialsDO::getOriginalUrl, reqVO.getOriginalUrl())
                .eqIfPresent(PptMaterialsDO::getWidth, reqVO.getWidth())
                .eqIfPresent(PptMaterialsDO::getHeight, reqVO.getHeight())
                .eqIfPresent(PptMaterialsDO::getIndexNo, reqVO.getIndexNo())
                .betweenIfPresent(PptMaterialsDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(PptMaterialsDO::getStatus, reqVO.getStatus())
                .orderByDesc(PptMaterialsDO::getId));
    }

}