package cn.iocoder.yudao.module.digitalcourse.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.digitalcourse.service.coursemedia.CourseMediaService;
import cn.iocoder.yudao.module.digitalcourse.service.coursemedia.CourseMediaServiceUtil;
import cn.iocoder.yudao.module.digitalcourse.service.digitalhumans.DigitalHumansServiceUtil;
import cn.iocoder.yudao.module.digitalcourse.service.voices.VoicesServiceUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class CourseMediaMegerJob implements JobHandler {

    @Resource
    private CourseMediaServiceUtil courseMediaServiceUtil;
    @Resource
    private DigitalHumansServiceUtil digitalHumansServiceUtil;
    @Resource
    private VoicesServiceUtil voicesServiceUtil;
    @Override
    @TenantJob
    public String execute(String param) throws Exception {
        //合成视频回刷状态
        courseMediaServiceUtil.queryRemoteMegerResult();
        //训练数字人模型回刷状态
        digitalHumansServiceUtil.queryRemoteTrainResult();
        //训练声音模型回刷状态
        voicesServiceUtil.queryRemoteTrainResult();
        return "";
    }
}
