package cn.iocoder.yudao.module.digitalcourse.service.coursescenevoices;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo.AppCourseSceneVoicesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo.AppCourseSceneVoicesSaveReqVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenevoices.CourseSceneVoicesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenevoices.CourseSceneVoicesMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 存储每个场景中的声音信息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CourseSceneVoicesServiceImpl implements CourseSceneVoicesService {

    @Resource
    private CourseSceneVoicesMapper courseSceneVoicesMapper;

    @Override
    public Long createCourseSceneVoices(AppCourseSceneVoicesSaveReqVO createReqVO) {
        // 插入
        CourseSceneVoicesDO courseSceneVoices = BeanUtils.toBean(createReqVO, CourseSceneVoicesDO.class);
        courseSceneVoicesMapper.insert(courseSceneVoices);
        // 返回
        return courseSceneVoices.getId();
    }

    @Override
    public void updateCourseSceneVoices(AppCourseSceneVoicesSaveReqVO updateReqVO) {
        // 校验存在
        validateCourseSceneVoicesExists(updateReqVO.getId());
        // 更新
        CourseSceneVoicesDO updateObj = BeanUtils.toBean(updateReqVO, CourseSceneVoicesDO.class);
        courseSceneVoicesMapper.updateById(updateObj);
    }

    @Override
    public void deleteCourseSceneVoices(Long id) {
        // 校验存在
        validateCourseSceneVoicesExists(id);
        // 删除
        courseSceneVoicesMapper.deleteById(id);
    }

    private void validateCourseSceneVoicesExists(Long id) {
        if (courseSceneVoicesMapper.selectById(id) == null) {
            throw exception(COURSE_SCENE_VOICES_NOT_EXISTS);
        }
    }

    @Override
    public CourseSceneVoicesDO getCourseSceneVoices(Long id) {
        return courseSceneVoicesMapper.selectById(id);
    }

    @Override
    public PageResult<CourseSceneVoicesDO> getCourseSceneVoicesPage(AppCourseSceneVoicesPageReqVO pageReqVO) {
        return courseSceneVoicesMapper.selectPage(pageReqVO);
    }

}