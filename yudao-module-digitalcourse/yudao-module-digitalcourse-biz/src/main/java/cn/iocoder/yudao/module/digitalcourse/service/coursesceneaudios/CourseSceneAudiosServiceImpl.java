package cn.iocoder.yudao.module.digitalcourse.service.coursesceneaudios;

import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios.vo.AppCourseSceneAudiosPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios.vo.AppCourseSceneAudiosSaveReqVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursesceneaudios.CourseSceneAudiosDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursesceneaudios.CourseSceneAudiosMapper;

import java.util.List;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CourseSceneAudiosServiceImpl implements CourseSceneAudiosService {

    @Resource
    private CourseSceneAudiosMapper courseSceneAudiosMapper;

    @Override
    public Long createCourseSceneAudios(AppCourseSceneAudiosSaveReqVO createReqVO) {
        // 插入
        CourseSceneAudiosDO courseSceneAudios = BeanUtils.toBean(createReqVO, CourseSceneAudiosDO.class);
        courseSceneAudiosMapper.insert(courseSceneAudios);
        // 返回
        return courseSceneAudios.getId();
    }

    @Override
    public void updateCourseSceneAudios(AppCourseSceneAudiosSaveReqVO updateReqVO) {
        // 校验存在
        validateCourseSceneAudiosExists(updateReqVO.getId());
        // 更新
        CourseSceneAudiosDO updateObj = BeanUtils.toBean(updateReqVO, CourseSceneAudiosDO.class);
        courseSceneAudiosMapper.updateById(updateObj);
    }

    @Override
    public void deleteCourseSceneAudios(Long id) {
        // 校验存在
        validateCourseSceneAudiosExists(id);
        // 删除
        courseSceneAudiosMapper.deleteById(id);
    }

    @Override
    public void deleteBySceneId(Set<Long> id) {
        courseSceneAudiosMapper.delete(new QueryWrapperX<CourseSceneAudiosDO>().lambda().in(CourseSceneAudiosDO::getSceneId, id));
    }

    @Override
    public List<AppCourseSceneAudiosSaveReqVO> selectAudioByScenesCourseIds(Set<Long> scenesIds) {
        List<CourseSceneAudiosDO> courseSceneAudiosDOS = courseSceneAudiosMapper.selectList(new QueryWrapperX<CourseSceneAudiosDO>().lambda().in(CourseSceneAudiosDO::getSceneId, scenesIds));
        List<AppCourseSceneAudiosSaveReqVO> bean = BeanUtils.toBean(courseSceneAudiosDOS, AppCourseSceneAudiosSaveReqVO.class);
        return bean;
    }

    private void validateCourseSceneAudiosExists(Long id) {
        if (courseSceneAudiosMapper.selectById(id) == null) {
            throw exception(COURSE_SCENE_AUDIOS_NOT_EXISTS);
        }
    }

    @Override
    public CourseSceneAudiosDO getCourseSceneAudios(Long id) {
        return courseSceneAudiosMapper.selectById(id);
    }

    @Override
    public PageResult<CourseSceneAudiosDO> getCourseSceneAudiosPage(AppCourseSceneAudiosPageReqVO pageReqVO) {
        return courseSceneAudiosMapper.selectPage(pageReqVO);
    }

}