package cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenetexts;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo.AppCourseSceneTextsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenetexts.CourseSceneTextsDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * 存储场景中的文本信息，包括文本内容、音调、速度等 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CourseSceneTextsMapper extends BaseMapperX<CourseSceneTextsDO> {

    default PageResult<CourseSceneTextsDO> selectPage(AppCourseSceneTextsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CourseSceneTextsDO>()
                .eqIfPresent(CourseSceneTextsDO::getPitch, reqVO.getPitch())
                .eqIfPresent(CourseSceneTextsDO::getSpeed, reqVO.getSpeed())
                .eqIfPresent(CourseSceneTextsDO::getVolume, reqVO.getVolume())
                .eqIfPresent(CourseSceneTextsDO::getSmartSpeed, reqVO.getSmartSpeed())
                .eqIfPresent(CourseSceneTextsDO::getSpeechRate, reqVO.getSpeechRate())
                .eqIfPresent(CourseSceneTextsDO::getTextJson, reqVO.getTextJson())
                .betweenIfPresent(CourseSceneTextsDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(CourseSceneTextsDO::getStatus, reqVO.getStatus())
                .orderByDesc(CourseSceneTextsDO::getId));
    }
    int physicalDelete(Set<Long> ids);

}