package cn.iocoder.yudao.module.digitalcourse.service.coursescenes;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.backgrounds.vo.BackgroundsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios.vo.AppCourseSceneAudiosSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo.AppCourseSceneBackgroundsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenecomponents.vo.AppCourseSceneComponentsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo.AppCourseScenesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo.AppCourseScenesSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo.AppCourseSceneTextsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo.AppCourseSceneVoicesSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.voices.vo.VoicesSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.backgrounds.BackgroundsDO;
import cn.iocoder.yudao.module.digitalcourse.service.backgrounds.BackgroundsService;
import cn.iocoder.yudao.module.digitalcourse.service.coursesceneaudios.CourseSceneAudiosService;
import cn.iocoder.yudao.module.digitalcourse.service.coursescenebackgrounds.CourseSceneBackgroundsService;
import cn.iocoder.yudao.module.digitalcourse.service.coursescenecomponents.CourseSceneComponentsService;
import cn.iocoder.yudao.module.digitalcourse.service.coursescenetexts.CourseSceneTextsService;
import cn.iocoder.yudao.module.digitalcourse.service.coursescenevoices.CourseSceneVoicesService;
import cn.iocoder.yudao.module.digitalcourse.service.voices.VoicesService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenes.CourseScenesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
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

    @Resource
    private CourseSceneBackgroundsService backgroundsService;

    @Resource
    private CourseSceneComponentsService componentsService;
    @Resource
    private CourseSceneVoicesService voicesService;
    @Resource
    private CourseSceneTextsService textsService;
    @Resource
    private CourseSceneAudiosService audiosService;

    @Override
    @Transactional
    public Long createCourseScenes(AppCourseScenesSaveReqVO createReqVO) {
        // 插入
        CourseScenesDO courseScenes = BeanUtils.toBean(createReqVO, CourseScenesDO.class);
        courseScenesMapper.insert(courseScenes);

        //插入背景
        AppCourseSceneBackgroundsSaveReqVO sceneBackgrounds = createReqVO.getSceneBackgrounds();
        sceneBackgrounds.setSceneId(courseScenes.getId());
        backgroundsService.createCourseSceneBackgrounds(sceneBackgrounds);
        //插入组件
        AppCourseSceneComponentsSaveReqVO sceneComponents = createReqVO.getSceneComponents();
        sceneComponents.setSceneId(courseScenes.getId());
        componentsService.createCourseSceneComponents(sceneComponents);
        //声音
        AppCourseSceneVoicesSaveReqVO sceneVoices = createReqVO.getSceneVoices();
        sceneVoices.setSceneId(courseScenes.getId());
        voicesService.createCourseSceneVoices(sceneVoices);
        //文本
        AppCourseSceneTextsSaveReqVO sceneTexts = createReqVO.getSceneTexts();
        sceneTexts.setSceneId(courseScenes.getId());
        textsService.createCourseSceneTexts(sceneTexts);
        //场景音频
        AppCourseSceneAudiosSaveReqVO sceneAudios = createReqVO.getSceneAudios();
        sceneAudios.setSceneId(courseScenes.getId());
        audiosService.createCourseSceneAudios(sceneAudios);
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