package cn.iocoder.yudao.module.digitalcourse.service.coursescenetexts;

import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo.AppCourseSceneTextsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo.AppCourseSceneTextsSaveReqVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenetexts.CourseSceneTextsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenetexts.CourseSceneTextsMapper;

import java.util.List;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 存储场景中的文本信息，包括文本内容、音调、速度等 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CourseSceneTextsServiceImpl implements CourseSceneTextsService {

    @Resource
    private CourseSceneTextsMapper courseSceneTextsMapper;

    @Override
    public Long createCourseSceneTexts(AppCourseSceneTextsSaveReqVO createReqVO) {
        // 插入
        CourseSceneTextsDO courseSceneTexts = BeanUtils.toBean(createReqVO, CourseSceneTextsDO.class);
        courseSceneTextsMapper.insert(courseSceneTexts);
        // 返回
        return courseSceneTexts.getId();
    }
    @Override
    public Boolean createCourseSceneTexts(List<AppCourseSceneTextsSaveReqVO> createReqVO) {
        // 插入
        List<CourseSceneTextsDO> courseSceneTexts = BeanUtils.toBean(createReqVO, CourseSceneTextsDO.class);
        return courseSceneTextsMapper.insertBatch(courseSceneTexts);
    }

    @Override
    public void updateCourseSceneTexts(AppCourseSceneTextsSaveReqVO updateReqVO) {
        // 校验存在
        validateCourseSceneTextsExists(updateReqVO.getId());
        // 更新
        CourseSceneTextsDO updateObj = BeanUtils.toBean(updateReqVO, CourseSceneTextsDO.class);
        courseSceneTextsMapper.updateById(updateObj);
    }

    @Override
    public void deleteCourseSceneTexts(Long id) {
        // 校验存在
        validateCourseSceneTextsExists(id);
        // 删除
        courseSceneTextsMapper.deleteById(id);
    }

    @Override
    public void deleteBySceneId(Set<Long> id) {
        courseSceneTextsMapper.physicalDelete(id);
    }

    @Override
    public List<AppCourseSceneTextsSaveReqVO> selectTextByScenesCourseIds(Set<Long> scenesIds) {
        List<CourseSceneTextsDO> courseSceneTextsDOS = courseSceneTextsMapper.selectList(new QueryWrapperX<CourseSceneTextsDO>().lambda().in(CourseSceneTextsDO::getSceneId, scenesIds));
        List<AppCourseSceneTextsSaveReqVO> bean = BeanUtils.toBean(courseSceneTextsDOS, AppCourseSceneTextsSaveReqVO.class);
        return bean;
    }

    private void validateCourseSceneTextsExists(Long id) {
        if (courseSceneTextsMapper.selectById(id) == null) {
            throw exception(COURSE_SCENE_TEXTS_NOT_EXISTS);
        }
    }

    @Override
    public CourseSceneTextsDO getCourseSceneTexts(Long id) {
        return courseSceneTextsMapper.selectById(id);
    }

    @Override
    public PageResult<CourseSceneTextsDO> getCourseSceneTextsPage(AppCourseSceneTextsPageReqVO pageReqVO) {
        return courseSceneTextsMapper.selectPage(pageReqVO);
    }

}