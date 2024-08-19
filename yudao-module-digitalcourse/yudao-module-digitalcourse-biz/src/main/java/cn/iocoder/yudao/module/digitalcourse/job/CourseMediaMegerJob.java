package cn.iocoder.yudao.module.digitalcourse.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.digitalcourse.service.coursemedia.CourseMediaService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class CourseMediaMegerJob implements JobHandler {

    @Resource
    private CourseMediaService courseMediaService;
    @Override
    @TenantJob
    public String execute(String param) throws Exception {
        courseMediaService.queryRemoteMegerResult();
        return "";
    }
}
