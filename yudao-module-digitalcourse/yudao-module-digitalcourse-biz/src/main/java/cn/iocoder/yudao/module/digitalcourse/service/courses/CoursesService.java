package cn.iocoder.yudao.module.digitalcourse.service.courses;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesUpdateReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.CourseTextRespVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courses.CoursesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import jakarta.validation.Valid;

/**
 * 存储课程的基本信息，包括课程名称、时长、状态等 Service 接口
 *
 * @author 芋道源码
 */
public interface CoursesService {

    /**
     * 创建存储课程的基本信息，包括课程名称、时长、状态等
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCourses(@Valid AppCoursesSaveReqVO createReqVO);

    /**
     * 更新存储课程的基本信息，包括课程名称、时长、状态等
     *
     * @param updateReqVO 更新信息
     */
    void updateCourses(@Valid AppCoursesUpdateReqVO updateReqVO);

    /**
     * 删除存储课程的基本信息，包括课程名称、时长、状态等
     *
     * @param id 编号
     */
    void deleteCourses(Long id);

    /**
     * 获得存储课程的基本信息，包括课程名称、时长、状态等
     *
     * @param id 编号
     * @return 存储课程的基本信息，包括课程名称、时长、状态等
     */
    AppCoursesUpdateReqVO getCourses(Long id);

    /**
     * 获得存储课程的基本信息，包括课程名称、时长、状态等分页
     *
     * @param pageReqVO 分页查询
     * @return 存储课程的基本信息，包括课程名称、时长、状态等分页
     */
    PageResult<CoursesDO> getCoursesPage(AppCoursesPageReqVO pageReqVO);

    public CourseTextRespVO getCourseText(String courseId, String username, int no);

}