package cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenevoices;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo.AppCourseSceneVoicesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenevoices.CourseSceneVoicesDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 存储每个场景中的声音信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CourseSceneVoicesMapper extends BaseMapperX<CourseSceneVoicesDO> {

    default PageResult<CourseSceneVoicesDO> selectPage(AppCourseSceneVoicesPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CourseSceneVoicesDO>()
                .eqIfPresent(CourseSceneVoicesDO::getTonePitch, reqVO.getTonePitch())
                .eqIfPresent(CourseSceneVoicesDO::getVoiceType, reqVO.getVoiceType())
                .eqIfPresent(CourseSceneVoicesDO::getSpeechRate, reqVO.getSpeechRate())
                .likeIfPresent(CourseSceneVoicesDO::getName, reqVO.getName())
                .betweenIfPresent(CourseSceneVoicesDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CourseSceneVoicesDO::getStatus, reqVO.getStatus())
                .orderByDesc(CourseSceneVoicesDO::getId));
    }

}