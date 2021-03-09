package cn.iocoder.dashboard.modules.system.job.auth;

import cn.iocoder.dashboard.framework.quartz.core.handler.JobHandler;
import cn.iocoder.dashboard.modules.system.service.auth.SysUserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 用户 Session 超时 Job
 *
 * @author 芋道源码
 */
@Component
@Slf4j
public class SysUserSessionTimeoutJob implements JobHandler {

    @Resource
    SysUserSessionService sysUserSessionService;

    @Override
    public String execute(String param) throws Exception {
        return String.valueOf(sysUserSessionService.clearSessionTimeout());
    }

}
