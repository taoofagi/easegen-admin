package cn.iocoder.yudao.module.digitalcourse.service.coursescenebackgrounds;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo.AppCourseSceneBackgroundsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo.AppCourseSceneBackgroundsSaveReqVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenebackgrounds.CourseSceneBackgroundsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenebackgrounds.CourseSceneBackgroundsMapper;

import java.util.List;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 存储每个场景的背景信息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CourseSceneBackgroundsServiceImpl implements CourseSceneBackgroundsService {

    @Resource
    private CourseSceneBackgroundsMapper courseSceneBackgroundsMapper;

    @Override
    public Long createCourseSceneBackgrounds(AppCourseSceneBackgroundsSaveReqVO createReqVO) {
        // 插入
        CourseSceneBackgroundsDO courseSceneBackgrounds = BeanUtils.toBean(createReqVO, CourseSceneBackgroundsDO.class);
        courseSceneBackgroundsMapper.insert(courseSceneBackgrounds);
        // 返回
        return courseSceneBackgrounds.getId();
    }

    @Override
    public void updateCourseSceneBackgrounds(AppCourseSceneBackgroundsSaveReqVO updateReqVO) {
        // 校验存在
        validateCourseSceneBackgroundsExists(updateReqVO.getId());
        // 更新
        CourseSceneBackgroundsDO updateObj = BeanUtils.toBean(updateReqVO, CourseSceneBackgroundsDO.class);
        courseSceneBackgroundsMapper.updateById(updateObj);
    }

    @Override
    public void deleteCourseSceneBackgrounds(Long id) {
        // 校验存在
        validateCourseSceneBackgroundsExists(id);
        // 删除
        courseSceneBackgroundsMapper.deleteById(id);
    }

    @Override
    public void deleteBySceneId(Set<Long> id) {
        courseSceneBackgroundsMapper.delete(new QueryWrapper<CourseSceneBackgroundsDO>().lambda().in(CourseSceneBackgroundsDO::getSceneId,id));
    }

    @Override
    public List<AppCourseSceneBackgroundsSaveReqVO> selectBackgroudByScenesCourseIds(Set<Long> scenesCourseIds) {
        List<CourseSceneBackgroundsDO> courseSceneBackgroundsDOS = courseSceneBackgroundsMapper.selectList(new QueryWrapper<CourseSceneBackgroundsDO>().lambda().in(CourseSceneBackgroundsDO::getSceneId, scenesCourseIds));
        List<AppCourseSceneBackgroundsSaveReqVO> bean = BeanUtils.toBean(courseSceneBackgroundsDOS, AppCourseSceneBackgroundsSaveReqVO.class);
        return bean;
    }

    private void validateCourseSceneBackgroundsExists(Long id) {
        if (courseSceneBackgroundsMapper.selectById(id) == null) {
            throw exception(COURSE_SCENE_BACKGROUNDS_NOT_EXISTS);
        }
    }

    @Override
    public CourseSceneBackgroundsDO getCourseSceneBackgrounds(Long id) {
        return courseSceneBackgroundsMapper.selectById(id);
    }

    @Override
    public PageResult<CourseSceneBackgroundsDO> getCourseSceneBackgroundsPage(AppCourseSceneBackgroundsPageReqVO pageReqVO) {
        return courseSceneBackgroundsMapper.selectPage(pageReqVO);
    }

}