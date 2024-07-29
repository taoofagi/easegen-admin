package cn.iocoder.yudao.module.digitalcourse.service.coursescenetexts;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.digitalcourse.controller.app.coursescenetexts.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenetexts.CourseSceneTextsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenetexts.CourseSceneTextsMapper;

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