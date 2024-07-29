package cn.iocoder.yudao.module.digitalcourse.service.coursescenes;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.digitalcourse.controller.app.coursescenes.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenes.CourseScenesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenes.CourseScenesMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 存储课程的场景信息，包括背景、组件、声音等 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CourseScenesServiceImpl implements CourseScenesService {

    @Resource
    private CourseScenesMapper courseScenesMapper;

    @Override
    public Long createCourseScenes(AppCourseScenesSaveReqVO createReqVO) {
        // 插入
        CourseScenesDO courseScenes = BeanUtils.toBean(createReqVO, CourseScenesDO.class);
        courseScenesMapper.insert(courseScenes);
        // 返回
        return courseScenes.getId();
    }

    @Override
    public void updateCourseScenes(AppCourseScenesSaveReqVO updateReqVO) {
        // 校验存在
        validateCourseScenesExists(updateReqVO.getId());
        // 更新
        CourseScenesDO updateObj = BeanUtils.toBean(updateReqVO, CourseScenesDO.class);
        courseScenesMapper.updateById(updateObj);
    }

    @Override
    public void deleteCourseScenes(Long id) {
        // 校验存在
        validateCourseScenesExists(id);
        // 删除
        courseScenesMapper.deleteById(id);
    }

    private void validateCourseScenesExists(Long id) {
        if (courseScenesMapper.selectById(id) == null) {
            throw exception(COURSE_SCENES_NOT_EXISTS);
        }
    }

    @Override
    public CourseScenesDO getCourseScenes(Long id) {
        return courseScenesMapper.selectById(id);
    }

    @Override
    public PageResult<CourseScenesDO> getCourseScenesPage(AppCourseScenesPageReqVO pageReqVO) {
        return courseScenesMapper.selectPage(pageReqVO);
    }

}