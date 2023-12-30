package cn.iocoder.yudao.framework.operatelog.core.service;

import cn.iocoder.yudao.framework.common.util.monitor.TracerUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import cn.iocoder.yudao.module.system.api.logger.OperateLogApi;
import cn.iocoder.yudao.module.system.api.logger.dto.OperateLogV2CreateReqDTO;
import com.mzt.logapi.beans.LogRecord;
import com.mzt.logapi.service.ILogRecordService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * 操作日志 ILogRecordService 实现类
 *
 * 基于 {@link OperateLogApi} 实现，记录操作日志
 *
 * @author HUIHUI
 */
@Slf4j
public class ILogRecordServiceImpl implements ILogRecordService {

    @Resource
    private OperateLogApi operateLogApi;
    
    @Override
    public void record(LogRecord logRecord) {
        OperateLogV2CreateReqDTO reqDTO = new OperateLogV2CreateReqDTO();
        // 补全通用字段
        reqDTO.setTraceId(TracerUtils.getTraceId());
        // 补充用户信息
        fillUserFields(reqDTO);
        // 补全模块信息
        fillModuleFields(reqDTO, logRecord);
        // 补全请求信息
        fillRequestFields(reqDTO);
        // 异步记录日志
        operateLogApi.createOperateLogV2(reqDTO);
        // TODO 测试结束删除或搞个开关
        log.info("操作日志 ===> {}", reqDTO);
    }

    private static void fillUserFields(OperateLogV2CreateReqDTO reqDTO) {
        reqDTO.setUserId(WebFrameworkUtils.getLoginUserId());
        reqDTO.setUserType(WebFrameworkUtils.getLoginUserType());
    }

    public static void fillModuleFields(OperateLogV2CreateReqDTO reqDTO, LogRecord logRecord) {
        reqDTO.setType(logRecord.getType()); // 大模块类型如 crm 客户
        reqDTO.setSubType(logRecord.getSubType());// 操作名称如 转移客户
        reqDTO.setBizId(Long.parseLong(logRecord.getBizNo())); // 操作模块业务编号
        reqDTO.setAction(logRecord.getAction());// 例如说，修改编号为 1 的用户信息，将性别从男改成女，将姓名从芋道改成源码。
        reqDTO.setExtra(logRecord.getExtra()); // 拓展字段，有些复杂的业务，需要记录一些字段 ( JSON 格式 )，例如说，记录订单编号，{ orderId: "1"}
    }

    private static void fillRequestFields(OperateLogV2CreateReqDTO reqDTO) {
        // 获得 Request 对象
        HttpServletRequest request = ServletUtils.getRequest();
        if (request == null) {
            return;
        }
        // 补全请求信息
        reqDTO.setRequestMethod(request.getMethod());
        reqDTO.setRequestUrl(request.getRequestURI());
        reqDTO.setUserIp(ServletUtils.getClientIP(request));
        reqDTO.setUserAgent(ServletUtils.getUserAgent(request));
    }

    @Override
    public List<LogRecord> queryLog(String bizNo, String type) {
        return Collections.emptyList();
    }

    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType) {
        return Collections.emptyList();
    }

}
