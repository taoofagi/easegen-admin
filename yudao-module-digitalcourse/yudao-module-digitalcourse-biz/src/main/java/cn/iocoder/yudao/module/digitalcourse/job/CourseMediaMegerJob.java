package cn.iocoder.yudao.module.digitalcourse.job;

import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia.CourseMediaDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursemedia.CourseMediaMapper;
import cn.iocoder.yudao.module.digitalcourse.service.coursemedia.CourseMediaServiceUtil;
import cn.iocoder.yudao.module.digitalcourse.service.coursemedia.provider.VideoSynthesisProvider;
import cn.iocoder.yudao.module.digitalcourse.service.digitalhumans.DigitalHumansServiceUtil;
import cn.iocoder.yudao.module.digitalcourse.service.voices.VoicesServiceUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 课程媒体合并任务定时Job
 * 支持2D和3D数字人视频合成任务状态查询
 *
 * @author easegen
 */
@Component
@Slf4j
public class CourseMediaMegerJob implements JobHandler {

    @Resource
    private CourseMediaServiceUtil courseMediaServiceUtil;
    @Resource
    private DigitalHumansServiceUtil digitalHumansServiceUtil;
    @Resource
    private VoicesServiceUtil voicesServiceUtil;
    @Resource
    private CourseMediaMapper courseMediaMapper;
    @Resource
    private List<VideoSynthesisProvider> providers;

    @Override
    @TenantJob
    public String execute(String param) throws Exception {
        // 1. 合成视频回刷状态（2D模式，批量查询）
        courseMediaServiceUtil.queryRemoteMegerResult();

        // 2. 查询3D数字人视频合成任务状态（单独查询）
        query3DTaskStatus();

        // 3. 训练数字人模型回刷状态
        digitalHumansServiceUtil.queryRemoteTrainResult();

        // 4. 训练声音模型回刷状态
        voicesServiceUtil.queryRemoteTrainResult();

        return "";
    }

    /**
     * 查询3D数字人视频合成任务状态
     */
    private void query3DTaskStatus() {
        try {
            // 查询所有合成中的3D任务（status=1，platformType=2）
            List<CourseMediaDO> tasks = courseMediaMapper.selectList(
                    new QueryWrapperX<CourseMediaDO>()
                            .lambda()
                            .eq(CourseMediaDO::getStatus, 1)
                            .eq(CourseMediaDO::getPlatformType, 2)
            );

            if (tasks == null || tasks.isEmpty()) {
                return;
            }

            log.debug("查询3D数字人视频合成任务状态，任务数量: {}", tasks.size());

            // 获取3D Provider
            VideoSynthesisProvider provider = providers.stream()
                    .filter(p -> p.supports(2))
                    .findFirst()
                    .orElse(null);

            if (provider == null) {
                log.warn("未找到3D数字人视频合成Provider");
                return;
            }

            // 逐个查询任务状态
            for (CourseMediaDO task : tasks) {
                try {
                    provider.queryTaskStatus(task);
                } catch (Exception e) {
                    log.error("查询3D数字人视频合成任务状态失败，courseMediaId: {}", task.getId(), e);
                }
            }

        } catch (Exception e) {
            log.error("查询3D数字人视频合成任务状态失败", e);
        }
    }
}
