package cn.iocoder.yudao.module.digitalcourse.service.coursescenecomponents;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenecomponents.vo.AppCourseSceneComponentsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenecomponents.vo.AppCourseSceneComponentsSaveReqVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenecomponents.CourseSceneComponentsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenecomponents.CourseSceneComponentsMapper;

import java.util.List;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 存储每个场景中的组件信息，包括PPT、数字人等 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CourseSceneComponentsServiceImpl implements CourseSceneComponentsService {

    @Resource
    private CourseSceneComponentsMapper courseSceneComponentsMapper;

    @Override
    public Long createCourseSceneComponents(AppCourseSceneComponentsSaveReqVO createReqVO) {
        // 插入
        CourseSceneComponentsDO courseSceneComponents = BeanUtils.toBean(createReqVO, CourseSceneComponentsDO.class);
        courseSceneComponentsMapper.insert(courseSceneComponents);
        // 返回
        return courseSceneComponents.getId();
    }

    @Override
    public Boolean batchCreateCourseSceneComponents(List<AppCourseSceneComponentsSaveReqVO> createReqVO) {
        List<CourseSceneComponentsDO> bean = BeanUtils.toBean(createReqVO, CourseSceneComponentsDO.class);
        return courseSceneComponentsMapper.insertBatch(bean);
    }

    @Override
    public void updateCourseSceneComponents(AppCourseSceneComponentsSaveReqVO updateReqVO) {
        // 校验存在
        validateCourseSceneComponentsExists(updateReqVO.getId());
        // 更新
        CourseSceneComponentsDO updateObj = BeanUtils.toBean(updateReqVO, CourseSceneComponentsDO.class);
        courseSceneComponentsMapper.updateById(updateObj);
    }

    @Override
    public void deleteCourseSceneComponents(Long id) {
        // 校验存在
        validateCourseSceneComponentsExists(id);
        // 删除
        courseSceneComponentsMapper.deleteById(id);
    }

    @Override
    public void deleteBySceneId(Set<Long> id) {
        courseSceneComponentsMapper.physicalDelete(id);
    }

    @Override
    public List<AppCourseSceneComponentsSaveReqVO> selectComponentByScenesCourseIds(Set<Long> scenesCourseIds) {
        List<CourseSceneComponentsDO> courseSceneComponentsDOS = courseSceneComponentsMapper.selectList(new QueryWrapper<CourseSceneComponentsDO>().lambda().in(CourseSceneComponentsDO::getSceneId, scenesCourseIds));
        List<AppCourseSceneComponentsSaveReqVO> bean = BeanUtils.toBean(courseSceneComponentsDOS, AppCourseSceneComponentsSaveReqVO.class);
        return bean;
    }

    private void validateCourseSceneComponentsExists(Long id) {
        if (courseSceneComponentsMapper.selectById(id) == null) {
            throw exception(COURSE_SCENE_COMPONENTS_NOT_EXISTS);
        }
    }

    @Override
    public CourseSceneComponentsDO getCourseSceneComponents(Long id) {
        return courseSceneComponentsMapper.selectById(id);
    }

    @Override
    public PageResult<CourseSceneComponentsDO> getCourseSceneComponentsPage(AppCourseSceneComponentsPageReqVO pageReqVO) {
        return courseSceneComponentsMapper.selectPage(pageReqVO);
    }

}