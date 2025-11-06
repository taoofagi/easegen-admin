package cn.iocoder.yudao.module.digitalcourse.service.coursemedia.provider;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaMegerVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia.CourseMediaDO;
import cn.iocoder.yudao.module.digitalcourse.service.coursemedia.CourseMediaServiceUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Easegen 2D数字人提供者
 * 复用现有的2D数字人合成逻辑
 *
 * @author easegen
 */
@Component
@Slf4j
public class Easegen2DProvider implements VideoSynthesisProvider {

    @Resource
    private CourseMediaServiceUtil courseMediaServiceUtil;

    @Override
    public Integer getPlatformType() {
        return 1; // 2D
    }

    @Override
    public void createSynthesisTask(CourseMediaDO courseMedia, CourseMediaMegerVO mergeVO) {
        log.info("创建2D数字人视频合成任务，courseMediaId: {}", courseMedia.getId());
        // 调用现有的2D逻辑
        courseMediaServiceUtil.remoteMegerMedia(mergeVO);
    }

    @Override
    public void queryTaskStatus(CourseMediaDO courseMedia) {
        log.debug("查询2D数字人视频合成任务状态，courseMediaId: {}", courseMedia.getId());
        // 2D任务状态查询由定时任务统一处理
        // 这里不需要单独实现，因为CourseMediaServiceUtil.queryRemoteMegerResult()会批量查询
    }

    @Override
    public void cancelTask(Long courseMediaId) {
        log.warn("2D数字人视频合成暂不支持取消任务，courseMediaId: {}", courseMediaId);
        // 2D暂不支持取消任务
    }

    @Override
    public boolean supports(Integer platformType) {
        return platformType != null && platformType == 1;
    }
}

