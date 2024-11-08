package cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenecomponents;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenecomponents.vo.AppCourseSceneComponentsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenecomponents.CourseSceneComponentsDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * 存储每个场景中的组件信息，包括PPT、数字人等 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CourseSceneComponentsMapper extends BaseMapperX<CourseSceneComponentsDO> {

    default PageResult<CourseSceneComponentsDO> selectPage(AppCourseSceneComponentsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CourseSceneComponentsDO>()
                .likeIfPresent(CourseSceneComponentsDO::getName, reqVO.getName())
                .eqIfPresent(CourseSceneComponentsDO::getSrc, reqVO.getSrc())
                .eqIfPresent(CourseSceneComponentsDO::getCover, reqVO.getCover())
                .eqIfPresent(CourseSceneComponentsDO::getWidth, reqVO.getWidth())
                .eqIfPresent(CourseSceneComponentsDO::getHeight, reqVO.getHeight())
                .eqIfPresent(CourseSceneComponentsDO::getOriginWidth, reqVO.getOriginWidth())
                .eqIfPresent(CourseSceneComponentsDO::getOriginHeight, reqVO.getOriginHeight())
                .eqIfPresent(CourseSceneComponentsDO::getCategory, reqVO.getCategory())
                .eqIfPresent(CourseSceneComponentsDO::getDepth, reqVO.getDepth())
                .eqIfPresent(CourseSceneComponentsDO::getTop, reqVO.getTop())
                .eqIfPresent(CourseSceneComponentsDO::getMarginLeft, reqVO.getMarginLeft())
                .eqIfPresent(CourseSceneComponentsDO::getEntityId, reqVO.getEntityId())
                .eqIfPresent(CourseSceneComponentsDO::getEntityType, reqVO.getEntityType())
                .eqIfPresent(CourseSceneComponentsDO::getBusinessId, reqVO.getBusinessId())
                .betweenIfPresent(CourseSceneComponentsDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CourseSceneComponentsDO::getStatus, reqVO.getStatus())
                .orderByDesc(CourseSceneComponentsDO::getId));
    }

    int physicalDelete(Set<Long> ids);
}