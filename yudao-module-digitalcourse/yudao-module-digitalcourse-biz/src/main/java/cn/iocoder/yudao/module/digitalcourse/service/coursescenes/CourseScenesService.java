package cn.iocoder.yudao.module.digitalcourse.service.coursescenes;


import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo.AppCourseScenesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo.AppCourseScenesSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenes.CourseScenesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 存储课程的场景信息，包括背景、组件、声音等 Service 接口
 *
 * @author 芋道源码
 */
public interface CourseScenesService {

    /**
     * 创建存储课程的场景信息，包括背景、组件、声音等
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCourseScenes(@Valid AppCourseScenesSaveReqVO createReqVO);

    Boolean batchCreateCourseScenes(@Valid List<AppCourseScenesSaveReqVO> createReqVO);

    /**
     * 根据课程id删除场景信息
     *
     */
    Boolean batchRemoveCouseScenes(@Valid Long id);

    List<AppCourseScenesSaveReqVO> selectScenesInfo(Long couseId);
    /**
     * 更新存储课程的场景信息，包括背景、组件、声音等
     *
     * @param updateReqVO 更新信息
     */
    void updateCourseScenes(@Valid AppCourseScenesSaveReqVO updateReqVO);

    /**
     * 删除存储课程的场景信息，包括背景、组件、声音等
     *
     * @param id 编号
     */
    void deleteCourseScenes(Long id);

    /**
     * 获得存储课程的场景信息，包括背景、组件、声音等
     *
     * @param id 编号
     * @return 存储课程的场景信息，包括背景、组件、声音等
     */
    CourseScenesDO getCourseScenes(Long id);

    /**
     * 获得存储课程的场景信息，包括背景、组件、声音等分页
     *
     * @param pageReqVO 分页查询
     * @return 存储课程的场景信息，包括背景、组件、声音等分页
     */
    PageResult<CourseScenesDO> getCourseScenesPage(AppCourseScenesPageReqVO pageReqVO);

}