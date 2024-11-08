package cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursesceneaudios;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios.vo.AppCourseSceneAudiosPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursesceneaudios.CourseSceneAudiosDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * 存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CourseSceneAudiosMapper extends BaseMapperX<CourseSceneAudiosDO> {

    default PageResult<CourseSceneAudiosDO> selectPage(AppCourseSceneAudiosPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CourseSceneAudiosDO>()
                .eqIfPresent(CourseSceneAudiosDO::getUseVideoBackgroundAudio, reqVO.getUseVideoBackgroundAudio())
                .betweenIfPresent(CourseSceneAudiosDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CourseSceneAudiosDO::getStatus, reqVO.getStatus())
                .orderByDesc(CourseSceneAudiosDO::getId));
    }

    int physicalDelete(Set<Long> ids);
}