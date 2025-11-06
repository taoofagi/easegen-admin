package cn.iocoder.yudao.module.digitalcourse.service.coursemedia;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaMegerVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesUpdateReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia.CourseMediaDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursemedia.CourseMediaMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.BAD_REQUEST;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.COURSE_MEDIA_NOT_EXISTS;

/**
 * 课程媒体 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class CourseMediaServiceImpl implements CourseMediaService {


    @Resource
    private CourseMediaMapper courseMediaMapper;

    @Resource
    private CourseMediaServiceUtil courseMediaServiceUtil;

    @Resource
    private List<cn.iocoder.yudao.module.digitalcourse.service.coursemedia.provider.VideoSynthesisProvider> providers;

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
        CourseMediaDO courseMediaDO = courseMediaMapper.selectOne(new QueryWrapperX<CourseMediaDO>().lambda().eq(CourseMediaDO::getCourseId,id).in(CourseMediaDO::getStatus,0,1));
        if (courseMediaDO == null){
            courseMediaDO = new CourseMediaDO();
            courseMediaDO.setCourseId(id);
            courseMediaDO.setStatus(0);
            courseMediaDO.setMediaType(1);
            courseMediaDO.setName(updateReqVO.getName());
            courseMediaDO.setCourseName(updateReqVO.getName());
            
            // 设置平台类型（默认2D）
            Integer platformType = updateReqVO.getPlatformType();
            if (platformType == null) {
                platformType = 1; // 默认2D
            }
            courseMediaDO.setPlatformType(platformType);
            
            // 设置3D相关字段（如果使用3D）
            if (platformType == 2) {
                courseMediaDO.setLookName(updateReqVO.getLookName());
                courseMediaDO.setTtsVcnName(updateReqVO.getTtsVcnName());
                courseMediaDO.setStudioName(updateReqVO.getStudioName());
                courseMediaDO.setSubTitle(updateReqVO.getSubTitle() != null ? updateReqVO.getSubTitle() : "on");
                courseMediaDO.setIfAigcMark(updateReqVO.getIfAigcMark() != null ? updateReqVO.getIfAigcMark() : 1);
            }
            
            //将updateReqVO 转换为json字符串
            courseMediaDO.setReqJson(JSON.toJSONString(updateReqVO));
            courseMediaMapper.insert(courseMediaDO);
        }else{
            return CommonResult.error(BAD_REQUEST.getCode(),"已存在合成中视频，不允许重复合成");
        }
        updateReqVO.setCourseMediaId(courseMediaDO.getId());
        
        // 根据平台类型选择对应的Provider
        Integer platformType = courseMediaDO.getPlatformType() != null ? courseMediaDO.getPlatformType() : 1;
        cn.iocoder.yudao.module.digitalcourse.service.coursemedia.provider.VideoSynthesisProvider provider = getProvider(platformType);
        
        // 调用对应的Provider创建任务
        try {
            provider.createSynthesisTask(courseMediaDO, updateReqVO);
            return CommonResult.success(true);
        } catch (Exception e) {
            log.error("创建视频合成任务失败，courseMediaId: {}", courseMediaDO.getId(), e);
            return CommonResult.error(BAD_REQUEST.getCode(), "创建视频合成任务失败: " + e.getMessage());
        }
    }

    /**
     * 获取视频合成提供者
     */
    private cn.iocoder.yudao.module.digitalcourse.service.coursemedia.provider.VideoSynthesisProvider getProvider(Integer platformType) {
        return providers.stream()
                .filter(p -> p.supports(platformType))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("不支持的平台类型: " + platformType));
    }

    @Override
    public CommonResult reMegerMedia(CourseMediaMegerVO updateReqVO) {
        Long id = updateReqVO.getId();
        CourseMediaDO courseMediaDO = courseMediaMapper.selectById(updateReqVO.getId());
        if (courseMediaDO == null){
            return CommonResult.error(BAD_REQUEST.getCode(),"未查询到合成视频记录");
        }
        if (3!=courseMediaDO.getStatus()){
            return CommonResult.error(BAD_REQUEST.getCode(),"只有失败状态视频允许重新合成视频");
        }
        //异步调用数字人视频渲染接口，开始合并
        Boolean success = courseMediaServiceUtil.reMegerMedia(courseMediaDO);
        if(success) {
            return CommonResult.success(true);
        } else {
            return CommonResult.error(BAD_REQUEST.getCode(),"视频重新合成失败");
        }

    }



}