package cn.iocoder.yudao.module.digitalcourse.service.coursescenevoices;

import jakarta.validation.*;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo.AppCourseSceneVoicesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo.AppCourseSceneVoicesSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenevoices.CourseSceneVoicesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import java.util.List;
import java.util.Set;

/**
 * 存储每个场景中的声音信息 Service 接口
 *
 * @author 芋道源码
 */
public interface CourseSceneVoicesService {

    /**
     * 创建存储每个场景中的声音信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCourseSceneVoices(@Valid AppCourseSceneVoicesSaveReqVO createReqVO);
    Boolean createCourseSceneVoices(@Valid List<AppCourseSceneVoicesSaveReqVO> createReqVO);

    /**
     * 更新存储每个场景中的声音信息
     *
     * @param updateReqVO 更新信息
     */
    void updateCourseSceneVoices(@Valid AppCourseSceneVoicesSaveReqVO updateReqVO);

    /**
     * 删除存储每个场景中的声音信息
     *
     * @param id 编号
     */
    void deleteCourseSceneVoices(Long id);
    void deleteBySceneId(Set<Long> id);
    List<AppCourseSceneVoicesSaveReqVO> selectVoiceByScenesCourseIds(Set<Long> scenesIds);
    /**
     * 获得存储每个场景中的声音信息
     *
     * @param id 编号
     * @return 存储每个场景中的声音信息
     */
    CourseSceneVoicesDO getCourseSceneVoices(Long id);

    /**
     * 获得存储每个场景中的声音信息分页
     *
     * @param pageReqVO 分页查询
     * @return 存储每个场景中的声音信息分页
     */
    PageResult<CourseSceneVoicesDO> getCourseSceneVoicesPage(AppCourseSceneVoicesPageReqVO pageReqVO);

}