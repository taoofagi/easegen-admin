package cn.iocoder.yudao.module.digitalcourse.service.coursescenebackgrounds;


import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo.AppCourseSceneBackgroundsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo.AppCourseSceneBackgroundsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenebackgrounds.CourseSceneBackgroundsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

/**
 * 存储每个场景的背景信息 Service 接口
 *
 * @author 芋道源码
 */
public interface CourseSceneBackgroundsService {

    /**
     * 创建存储每个场景的背景信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCourseSceneBackgrounds(@Valid AppCourseSceneBackgroundsSaveReqVO createReqVO);
    Boolean createCourseSceneBackgrounds(@Valid List<AppCourseSceneBackgroundsSaveReqVO> createReqVO);

    /**
     * 更新存储每个场景的背景信息
     *
     * @param updateReqVO 更新信息
     */
    void updateCourseSceneBackgrounds(@Valid AppCourseSceneBackgroundsSaveReqVO updateReqVO);

    /**
     * 删除存储每个场景的背景信息
     *
     * @param id 编号
     */
    void deleteCourseSceneBackgrounds(Long id);
    void deleteBySceneId(Set<Long> id);
    List<AppCourseSceneBackgroundsSaveReqVO> selectBackgroudByScenesCourseIds(Set<Long> id);
    /**
     * 获得存储每个场景的背景信息
     *
     * @param id 编号
     * @return 存储每个场景的背景信息
     */
    CourseSceneBackgroundsDO getCourseSceneBackgrounds(Long id);

    /**
     * 获得存储每个场景的背景信息分页
     *
     * @param pageReqVO 分页查询
     * @return 存储每个场景的背景信息分页
     */
    PageResult<CourseSceneBackgroundsDO> getCourseSceneBackgroundsPage(AppCourseSceneBackgroundsPageReqVO pageReqVO);

}