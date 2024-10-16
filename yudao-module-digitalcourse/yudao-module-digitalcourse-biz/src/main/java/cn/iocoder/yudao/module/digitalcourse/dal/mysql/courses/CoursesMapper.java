package cn.iocoder.yudao.module.digitalcourse.dal.mysql.courses;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courses.CoursesDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 存储课程的基本信息，包括课程名称、时长、状态等 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CoursesMapper extends BaseMapperX<CoursesDO> {

    default PageResult<CoursesDO> selectPage(AppCoursesPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CoursesDO>()
                .likeIfPresent(CoursesDO::getName, reqVO.getName())
                .eqIfPresent(CoursesDO::getAspect, reqVO.getAspect())
                .eqIfPresent(CoursesDO::getDuration, reqVO.getDuration())
                .eqIfPresent(CoursesDO::getHeight, reqVO.getHeight())
                .eqIfPresent(CoursesDO::getWidth, reqVO.getWidth())
                .eqIfPresent(CoursesDO::getMatting, reqVO.getMatting())
                .eqIfPresent(CoursesDO::getPageMode, reqVO.getPageMode())
                .eqIfPresent(CoursesDO::getStatus, reqVO.getStatus())
                .eqIfPresent(CoursesDO::getPageInfo, reqVO.getPageInfo())
                .eqIfPresent(CoursesDO::getSubtitlesStyle, reqVO.getSubtitlesStyle())
                .betweenIfPresent(CoursesDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CoursesDO::getId));
    }

}