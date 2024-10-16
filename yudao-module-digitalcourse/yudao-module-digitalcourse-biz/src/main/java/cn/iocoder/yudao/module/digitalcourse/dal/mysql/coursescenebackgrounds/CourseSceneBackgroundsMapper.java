package cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenebackgrounds;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo.AppCourseSceneBackgroundsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenebackgrounds.CourseSceneBackgroundsDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 存储每个场景的背景信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CourseSceneBackgroundsMapper extends BaseMapperX<CourseSceneBackgroundsDO> {

    default PageResult<CourseSceneBackgroundsDO> selectPage(AppCourseSceneBackgroundsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CourseSceneBackgroundsDO>()
                .eqIfPresent(CourseSceneBackgroundsDO::getBackgroundType, reqVO.getBackgroundType())
                .eqIfPresent(CourseSceneBackgroundsDO::getEntityId, reqVO.getEntityId())
                .eqIfPresent(CourseSceneBackgroundsDO::getSrc, reqVO.getSrc())
                .eqIfPresent(CourseSceneBackgroundsDO::getCover, reqVO.getCover())
                .eqIfPresent(CourseSceneBackgroundsDO::getWidth, reqVO.getWidth())
                .eqIfPresent(CourseSceneBackgroundsDO::getHeight, reqVO.getHeight())
                .eqIfPresent(CourseSceneBackgroundsDO::getDepth, reqVO.getDepth())
                .eqIfPresent(CourseSceneBackgroundsDO::getOriginWidth, reqVO.getOriginWidth())
                .eqIfPresent(CourseSceneBackgroundsDO::getOriginHeight, reqVO.getOriginHeight())
                .betweenIfPresent(CourseSceneBackgroundsDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CourseSceneBackgroundsDO::getStatus, reqVO.getStatus())
                .orderByDesc(CourseSceneBackgroundsDO::getId));
    }

}