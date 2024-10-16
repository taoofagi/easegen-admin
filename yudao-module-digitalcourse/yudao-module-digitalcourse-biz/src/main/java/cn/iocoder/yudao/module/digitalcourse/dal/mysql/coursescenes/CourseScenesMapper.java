package cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenes;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo.AppCourseScenesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenes.CourseScenesDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 存储课程的场景信息，包括背景、组件、声音等 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CourseScenesMapper extends BaseMapperX<CourseScenesDO> {

    default PageResult<CourseScenesDO> selectPage(AppCourseScenesPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CourseScenesDO>()
                .eqIfPresent(CourseScenesDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(CourseScenesDO::getDuration, reqVO.getDuration())
                .eqIfPresent(CourseScenesDO::getDriverType, reqVO.getDriverType())
                .eqIfPresent(CourseScenesDO::getBusinessId, reqVO.getBusinessId())
                .betweenIfPresent(CourseScenesDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CourseScenesDO::getStatus, reqVO.getStatus())
                .orderByDesc(CourseScenesDO::getId));
    }

}