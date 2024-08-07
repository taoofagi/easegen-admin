package cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursemedia;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia.CourseMediaDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 课程媒体 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CourseMediaMapper extends BaseMapperX<CourseMediaDO> {

    default PageResult<CourseMediaDO> selectPage(CourseMediaPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CourseMediaDO>()
                .eqIfPresent(CourseMediaDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(CourseMediaDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CourseMediaDO::getDuration, reqVO.getDuration())
                .betweenIfPresent(CourseMediaDO::getFinishTime, reqVO.getFinishTime())
                .eqIfPresent(CourseMediaDO::getMediaType, reqVO.getMediaType())
                .likeIfPresent(CourseMediaDO::getName, reqVO.getName())
                .eqIfPresent(CourseMediaDO::getPreviewUrl, reqVO.getPreviewUrl())
                .eqIfPresent(CourseMediaDO::getProgress, reqVO.getProgress())
                .eqIfPresent(CourseMediaDO::getCourseId, reqVO.getCourseId())
                .likeIfPresent(CourseMediaDO::getCourseName, reqVO.getCourseName())
                .orderByDesc(CourseMediaDO::getId));
    }

}