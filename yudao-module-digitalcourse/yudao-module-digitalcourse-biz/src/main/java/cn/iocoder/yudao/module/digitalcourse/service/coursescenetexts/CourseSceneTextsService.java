package cn.iocoder.yudao.module.digitalcourse.service.coursescenetexts;


import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo.AppCourseSceneTextsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo.AppCourseSceneTextsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenetexts.CourseSceneTextsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Set;

/**
 * 存储场景中的文本信息，包括文本内容、音调、速度等 Service 接口
 *
 * @author 芋道源码
 */
public interface CourseSceneTextsService {

    /**
     * 创建存储场景中的文本信息，包括文本内容、音调、速度等
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCourseSceneTexts(@Valid AppCourseSceneTextsSaveReqVO createReqVO);
    Boolean createCourseSceneTexts(@Valid List<AppCourseSceneTextsSaveReqVO> createReqVO);

    /**
     * 更新存储场景中的文本信息，包括文本内容、音调、速度等
     *
     * @param updateReqVO 更新信息
     */
    void updateCourseSceneTexts(@Valid AppCourseSceneTextsSaveReqVO updateReqVO);

    /**
     * 删除存储场景中的文本信息，包括文本内容、音调、速度等
     *
     * @param id 编号
     */
    void deleteCourseSceneTexts(Long id);


    void deleteBySceneId(Set<Long> id);

    List<AppCourseSceneTextsSaveReqVO> selectTextByScenesCourseIds(Set<Long> scenesIds);
    /**
     * 获得存储场景中的文本信息，包括文本内容、音调、速度等
     *
     * @param id 编号
     * @return 存储场景中的文本信息，包括文本内容、音调、速度等
     */
    CourseSceneTextsDO getCourseSceneTexts(Long id);

    /**
     * 获得存储场景中的文本信息，包括文本内容、音调、速度等分页
     *
     * @param pageReqVO 分页查询
     * @return 存储场景中的文本信息，包括文本内容、音调、速度等分页
     */
    PageResult<CourseSceneTextsDO> getCourseSceneTextsPage(AppCourseSceneTextsPageReqVO pageReqVO);

}