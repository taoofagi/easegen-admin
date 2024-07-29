package cn.iocoder.yudao.module.digitalcourse.service.courses;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.digitalcourse.controller.app.courses.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courses.CoursesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.courses.CoursesMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 存储课程的基本信息，包括课程名称、时长、状态等 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CoursesServiceImpl implements CoursesService {

    @Resource
    private CoursesMapper coursesMapper;

    @Override
    public Long createCourses(AppCoursesSaveReqVO createReqVO) {
        // 插入
        CoursesDO courses = BeanUtils.toBean(createReqVO, CoursesDO.class);
        coursesMapper.insert(courses);
        // 返回
        return courses.getId();
    }

    @Override
    public void updateCourses(AppCoursesSaveReqVO updateReqVO) {
        // 校验存在
        validateCoursesExists(updateReqVO.getId());
        // 更新
        CoursesDO updateObj = BeanUtils.toBean(updateReqVO, CoursesDO.class);
        coursesMapper.updateById(updateObj);
    }

    @Override
    public void deleteCourses(Long id) {
        // 校验存在
        validateCoursesExists(id);
        // 删除
        coursesMapper.deleteById(id);
    }

    private void validateCoursesExists(Long id) {
        if (coursesMapper.selectById(id) == null) {
            throw exception(COURSES_NOT_EXISTS);
        }
    }

    @Override
    public CoursesDO getCourses(Long id) {
        return coursesMapper.selectById(id);
    }

    @Override
    public PageResult<CoursesDO> getCoursesPage(AppCoursesPageReqVO pageReqVO) {
        return coursesMapper.selectPage(pageReqVO);
    }

}