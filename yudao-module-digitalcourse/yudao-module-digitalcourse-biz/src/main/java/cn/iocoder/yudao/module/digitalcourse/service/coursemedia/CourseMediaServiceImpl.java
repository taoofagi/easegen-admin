package cn.iocoder.yudao.module.digitalcourse.service.coursemedia;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaMegerVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesUpdateReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia.CourseMediaDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursemedia.CourseMediaMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

import java.math.BigInteger;
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

    private static final String REMOTE_BASE_URL = "http://digitalcourse.taoofagi.com:7860";

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
    public CommonResult megerMedia(CourseMediaMegerVO updateReqVO) {
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
        updateReqVO.setCourseMediaId(courseMediaDO.getId());
        remoteMegerMedia(updateReqVO);
        return CommonResult.success(true);
    }

    /**
     * 远程合并视频
     * @param updateReqVO
     * @return
     */
    @Async
    public void remoteMegerMedia(CourseMediaMegerVO updateReqVO) {
        HttpResponse execute = HttpRequest.post(REMOTE_BASE_URL + "/api/mergemedia").header("X-API-Key", "taoofagi").body(JSON.toJSONString(updateReqVO)).execute();
        String body = execute.body();
    }


    /**
     * 远程查询合并结果（定时任务）
     */

    public void queryRemoteMegerResult(){
        List<CourseMediaDO> courseMediaDOS = courseMediaMapper.selectList(new QueryWrapperX<CourseMediaDO>().lambda().eq(CourseMediaDO::getStatus, 1));
        //调用远程接口，回刷数据
        courseMediaDOS.stream().forEach(e -> {
            String result = HttpRequest.get(REMOTE_BASE_URL + "/api/mergemedia/result").header("X-API-Key", "taoofagi").form("courseMediaIds",e.getId()).execute().body();
            if (JSON.isValidObject(result)){
                JSONObject jsonObject = JSON.parseObject(result);
                BigInteger status = jsonObject.getBigInteger("status");
                if (status != null){
//                    合并状态，0：草稿，1：合成中，2：合成成功，3：合成失败
                    if (status.intValue() == 0) return;
                    if (status.intValue() == 1) return;
                    if (status.intValue() == 2 || status.intValue() == 3) {
                        e.setStatus(status.intValue());
                    }
                    courseMediaMapper.updateById(e);
                }
            }
        });

    }

}