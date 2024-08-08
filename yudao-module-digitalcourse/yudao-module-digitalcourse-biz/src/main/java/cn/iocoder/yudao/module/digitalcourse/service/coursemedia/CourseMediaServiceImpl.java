package cn.iocoder.yudao.module.digitalcourse.service.coursemedia;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesUpdateReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia.CourseMediaDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursemedia.CourseMediaMapper;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import java.util.List;

import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.COURSE_MEDIA_NOT_EXISTS;

/**
 * 课程媒体 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CourseMediaServiceImpl implements CourseMediaService {

    @Resource
    private CourseMediaMapper courseMediaMapper;

    @Override
    public Long createCourseMedia(CourseMediaSaveReqVO createReqVO) {
        // 插入
        CourseMediaDO courseMedia = BeanUtils.toBean(createReqVO, CourseMediaDO.class);
        courseMediaMapper.insert(courseMedia);
        // 返回
        return courseMedia.getId();
    }

    @Override
    public void updateCourseMedia(CourseMediaSaveReqVO updateReqVO) {
        // 校验存在
        validateCourseMediaExists(updateReqVO.getId());
        // 更新
        CourseMediaDO updateObj = BeanUtils.toBean(updateReqVO, CourseMediaDO.class);
        courseMediaMapper.updateById(updateObj);
    }

    @Override
    public void deleteCourseMedia(Long id) {
        // 校验存在
        validateCourseMediaExists(id);
        // 删除
        courseMediaMapper.deleteById(id);
    }

    private void validateCourseMediaExists(Long id) {
        if (courseMediaMapper.selectById(id) == null) {
            throw exception(COURSE_MEDIA_NOT_EXISTS);
        }
    }

    @Override
    public CourseMediaDO getCourseMedia(Long id) {
        return courseMediaMapper.selectById(id);
    }

    @Override
    public PageResult<CourseMediaDO> getCourseMediaPage(CourseMediaPageReqVO pageReqVO) {
        return courseMediaMapper.selectPage(pageReqVO);
    }

    @Override
    public CommonResult megerMedia(AppCoursesUpdateReqVO updateReqVO) {
        Long id = updateReqVO.getId();
        CourseMediaDO courseMediaDO = courseMediaMapper.selectOne(new QueryWrapperX<CourseMediaDO>().lambda().eq(CourseMediaDO::getCourseId,id).eq(CourseMediaDO::getStatus,1));
        if (courseMediaDO == null){
            courseMediaDO = new CourseMediaDO();
            courseMediaDO.setCourseId(id);
            courseMediaDO.setStatus(1);
            courseMediaDO.setMediaType(1);
            courseMediaDO.setName(updateReqVO.getName());
            courseMediaDO.setCourseName(updateReqVO.getName());
            courseMediaMapper.insert(courseMediaDO);
        }else{
            return CommonResult.error(500,"不允许重复合成");
        }
        remoteMegerMedia(updateReqVO);
        return CommonResult.success(true);
    }

    /**
     * 远程合并视频
     * @param updateReqVO
     * @return
     */
    @Async
    public Boolean remoteMegerMedia(AppCoursesUpdateReqVO updateReqVO) {

        return true;
    }


    /**
     * 远程查询合并结果（定时任务）
     */

    public void queryRemoteMegerResult(){
        List<CourseMediaDO> courseMediaDOS = courseMediaMapper.selectList(new QueryWrapperX<CourseMediaDO>().lambda().eq(CourseMediaDO::getStatus, 1));
        //调用远程接口，回刷数据


    }

}