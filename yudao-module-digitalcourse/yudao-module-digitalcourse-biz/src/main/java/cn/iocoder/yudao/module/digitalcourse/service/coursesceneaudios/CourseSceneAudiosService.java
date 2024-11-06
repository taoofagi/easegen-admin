package cn.iocoder.yudao.module.digitalcourse.service.coursesceneaudios;


import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios.vo.AppCourseSceneAudiosPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios.vo.AppCourseSceneAudiosSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursesceneaudios.CourseSceneAudiosDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

/**
 * 存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等 Service 接口
 *
 * @author 芋道源码
 */
public interface CourseSceneAudiosService {

    /**
     * 创建存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCourseSceneAudios(@Valid AppCourseSceneAudiosSaveReqVO createReqVO);
    Boolean createCourseSceneAudios(@Valid List<AppCourseSceneAudiosSaveReqVO> createReqVO);

    /**
     * 更新存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等
     *
     * @param updateReqVO 更新信息
     */
    void updateCourseSceneAudios(@Valid AppCourseSceneAudiosSaveReqVO updateReqVO);

    /**
     * 删除存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等
     *
     * @param id 编号
     */
    void deleteCourseSceneAudios(Long id);
    void deleteBySceneId(Set<Long> ids);

    List<AppCourseSceneAudiosSaveReqVO> selectAudioByScenesCourseIds(Set<Long> scenesIds);

    /**
     * 获得存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等
     *
     * @param id 编号
     * @return 存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等
     */
    CourseSceneAudiosDO getCourseSceneAudios(Long id);

    /**
     * 获得存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等分页
     *
     * @param pageReqVO 分页查询
     * @return 存储场景中的音频信息，包括音频ID和使用视频背景音频的标志等分页
     */
    PageResult<CourseSceneAudiosDO> getCourseSceneAudiosPage(AppCourseSceneAudiosPageReqVO pageReqVO);

}