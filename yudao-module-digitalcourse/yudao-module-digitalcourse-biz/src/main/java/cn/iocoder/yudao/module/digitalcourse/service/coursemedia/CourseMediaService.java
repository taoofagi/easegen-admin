package cn.iocoder.yudao.module.digitalcourse.service.coursemedia;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaMegerVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesUpdateReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia.CourseMediaDO;
import jakarta.validation.Valid;

/**
 * 课程媒体 Service 接口
 *
 * @author 芋道源码
 */
public interface CourseMediaService {

    /**
     * 创建课程媒体
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCourseMedia(@Valid CourseMediaSaveReqVO createReqVO);

    /**
     * 更新课程媒体
     *
     * @param updateReqVO 更新信息
     */
    void updateCourseMedia(@Valid CourseMediaSaveReqVO updateReqVO);

    /**
     * 删除课程媒体
     *
     * @param id 编号
     */
    void deleteCourseMedia(Long id);

    /**
     * 获得课程媒体
     *
     * @param id 编号
     * @return 课程媒体
     */
    CourseMediaDO getCourseMedia(Long id);

    /**
     * 获得课程媒体分页
     *
     * @param pageReqVO 分页查询
     * @return 课程媒体分页
     */
    PageResult<CourseMediaDO> getCourseMediaPage(CourseMediaPageReqVO pageReqVO);


    CommonResult megerMedia(CourseMediaMegerVO updateReqVO);

    CommonResult reMegerMedia(CourseMediaMegerVO updateReqVO);


}