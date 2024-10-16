package cn.iocoder.yudao.module.digitalcourse.service.coursescenes;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
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
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursesceneaudios.CourseSceneAudiosDO;
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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        AppCourseSceneBackgroundsSaveReqVO sceneBackgrounds = createReqVO.getBackground();
        sceneBackgrounds.setSceneId(courseScenes.getId());
        if (sceneBackgrounds.getStatus() == null ) sceneBackgrounds.setStatus(0);
        backgroundsService.createCourseSceneBackgrounds(sceneBackgrounds);
        //插入组件
        List<AppCourseSceneComponentsSaveReqVO> sceneComponents = createReqVO.getComponents();
        sceneComponents.stream().forEach(e -> {
            e.setSceneId(courseScenes.getId());
            if (e.getStatus() == null ) e.setStatus(0);
        });
        componentsService.batchCreateCourseSceneComponents(sceneComponents);
        //声音模型
        AppCourseSceneVoicesSaveReqVO sceneVoices = createReqVO.getVoice();
        sceneVoices.setSceneId(courseScenes.getId());
        if (sceneVoices.getStatus() == null ) sceneVoices.setStatus(0);
        voicesService.createCourseSceneVoices(sceneVoices);
        //文本
        AppCourseSceneTextsSaveReqVO sceneTexts = createReqVO.getTextDriver();
        sceneTexts.setSceneId(courseScenes.getId());
        if (sceneTexts.getStatus() == null ) sceneTexts.setStatus(0);
        textsService.createCourseSceneTexts(sceneTexts);
        //场景音频
        AppCourseSceneAudiosSaveReqVO sceneAudios = createReqVO.getAudioDriver();
        sceneAudios.setSceneId(courseScenes.getId());
        if (sceneAudios.getStatus() == null ) sceneAudios.setStatus(0);
        audiosService.createCourseSceneAudios(sceneAudios);
        // 返回
        return courseScenes.getId();
    }

    @Override
    public Boolean batchCreateCourseScenes(List<AppCourseScenesSaveReqVO> createReqVO) {
        createReqVO.stream().forEach(e -> {
            try {
                createCourseScenes(e);
            }catch (Exception exception){
                System.out.println(e.getId()+"场景添加失败");
                System.out.println(exception);
                throw exception(COURSES_UPDATE_ERROR);
            }
        });
        return true;
    }

    @Override
    public Boolean batchRemoveCouseScenes(Long id) {
        List<CourseScenesDO> courseScenesDOS = courseScenesMapper.selectList(new QueryWrapperX<CourseScenesDO>().lambda().eq(CourseScenesDO::getCourseId, id));
        Set<Long> scenesIds = courseScenesDOS.stream().map(e -> e.getId()).collect(Collectors.toSet());
        if (scenesIds.isEmpty()) return true;
        audiosService.deleteBySceneId(scenesIds);
        textsService.deleteBySceneId(scenesIds);
        voicesService.deleteBySceneId(scenesIds);
        componentsService.deleteBySceneId(scenesIds);
        backgroundsService.deleteBySceneId(scenesIds);

        courseScenesMapper.delete(new QueryWrapperX<CourseScenesDO>().lambda().in(CourseScenesDO::getId,scenesIds));
        return true;
    }

    @Override
    public List<AppCourseScenesSaveReqVO> selectScenesInfo(Long courseId) {
        List<CourseScenesDO> courseScenesDOS = courseScenesMapper.selectList(new QueryWrapperX<CourseScenesDO>().lambda().eq(CourseScenesDO::getCourseId, courseId));
        List<AppCourseScenesSaveReqVO> bean = BeanUtils.toBean(courseScenesDOS, AppCourseScenesSaveReqVO.class);

        Set<Long> scenesIds = courseScenesDOS.stream().map(e -> e.getId()).collect(Collectors.toSet());
        if (scenesIds.isEmpty()) return null;
        List<AppCourseSceneAudiosSaveReqVO> courseSceneAudiosSaveReqVOS = audiosService.selectAudioByScenesCourseIds(scenesIds);
        List<AppCourseSceneTextsSaveReqVO> appCourseSceneTextsSaveReqVOS = textsService.selectTextByScenesCourseIds(scenesIds);
        List<AppCourseSceneVoicesSaveReqVO> appCourseSceneVoicesSaveReqVOS = voicesService.selectVoiceByScenesCourseIds(scenesIds);
        List<AppCourseSceneComponentsSaveReqVO> appCourseSceneComponentsSaveReqVOS = componentsService.selectComponentByScenesCourseIds(scenesIds);
        List<AppCourseSceneBackgroundsSaveReqVO> appCourseSceneBackgroundsSaveReqVOS = backgroundsService.selectBackgroudByScenesCourseIds(scenesIds);

        bean.stream().forEach(e->{
            Long id = e.getId();
            List<AppCourseSceneAudiosSaveReqVO> audioList = courseSceneAudiosSaveReqVOS.stream().filter(item -> item.getSceneId().longValue() == id.longValue()).toList();
            if (!audioList.isEmpty()) e.setAudioDriver(audioList.stream().findFirst().get());
            List<AppCourseSceneTextsSaveReqVO> textList = appCourseSceneTextsSaveReqVOS.stream().filter(item -> item.getSceneId().longValue() == id.longValue()).toList();
            if (!textList.isEmpty()) e.setTextDriver(textList.stream().findFirst().get());
            List<AppCourseSceneVoicesSaveReqVO> voiceList = appCourseSceneVoicesSaveReqVOS.stream().filter(item -> item.getSceneId().longValue() == id.longValue()).toList();
            if (!voiceList.isEmpty()) e.setVoice(voiceList.stream().findFirst().get());
            List<AppCourseSceneComponentsSaveReqVO> componentsList = appCourseSceneComponentsSaveReqVOS.stream().filter(item -> item.getSceneId().longValue() == id.longValue()).toList();
            e.setComponents(componentsList);
            List<AppCourseSceneBackgroundsSaveReqVO> backgroudList = appCourseSceneBackgroundsSaveReqVOS.stream().filter(item -> item.getSceneId().longValue() == id.longValue()).toList();
            if (!backgroudList.isEmpty()) e.setBackground(backgroudList.stream().findFirst().get());
        });
        return bean;
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