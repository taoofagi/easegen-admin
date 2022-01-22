package cn.iocoder.yudao.adminserver.modules.bpm.service.task.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.adminserver.modules.bpm.controller.task.vo.instance.*;
import cn.iocoder.yudao.adminserver.modules.bpm.convert.message.BpmMessageConvert;
import cn.iocoder.yudao.adminserver.modules.bpm.convert.task.BpmProcessInstanceConvert;
import cn.iocoder.yudao.adminserver.modules.bpm.dal.dataobject.definition.BpmProcessDefinitionExtDO;
import cn.iocoder.yudao.adminserver.modules.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import cn.iocoder.yudao.adminserver.modules.bpm.dal.mysql.task.BpmProcessInstanceExtMapper;
import cn.iocoder.yudao.adminserver.modules.bpm.enums.task.BpmProcessInstanceDeleteReasonEnum;
import cn.iocoder.yudao.adminserver.modules.bpm.enums.task.BpmProcessInstanceResultEnum;
import cn.iocoder.yudao.adminserver.modules.bpm.enums.task.BpmProcessInstanceStatusEnum;
import cn.iocoder.yudao.adminserver.modules.bpm.service.definition.BpmProcessDefinitionService;
import cn.iocoder.yudao.adminserver.modules.bpm.service.message.BpmMessageService;
import cn.iocoder.yudao.adminserver.modules.bpm.service.task.BpmProcessInstanceService;
import cn.iocoder.yudao.adminserver.modules.bpm.service.task.BpmTaskService;
import cn.iocoder.yudao.adminserver.modules.bpm.service.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.adminserver.modules.system.dal.dataobject.dept.SysDeptDO;
import cn.iocoder.yudao.adminserver.modules.system.service.dept.SysDeptService;
import cn.iocoder.yudao.adminserver.modules.system.service.user.SysUserService;
import cn.iocoder.yudao.coreservice.modules.system.dal.dataobject.user.SysUserDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;

import static cn.iocoder.yudao.adminserver.modules.bpm.enums.BpmErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

/**
 * 流程实例 Service 实现类
 *
 * ProcessDefinition & ProcessInstance & Execution & Task 的关系：
 *     1. https://blog.csdn.net/bobozai86/article/details/105210414
 *
 * HistoricProcessInstance & ProcessInstance 的关系：
 *     1.https://my.oschina.net/843294669/blog/719024
 * 简单来说，前者 = 历史 + 运行中的流程实例，后者仅是运行中的流程实例
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class BpmProcessInstanceServiceImpl implements BpmProcessInstanceService {

    @Resource
    private RuntimeService runtimeService;
    @Resource
    private HistoryService historyService;

    @Resource
    private SysUserService userService;
    @Resource
    private SysDeptService deptService;
    @Resource
    @Lazy // 解决循环依赖
    private BpmTaskService taskService;
    @Resource
    private BpmProcessDefinitionService processDefinitionService;
    @Resource
    private BpmMessageService messageService;

    @Resource
    private BpmProcessInstanceExtMapper processInstanceExtMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcessInstance(Long userId, BpmProcessInstanceCreateReqVO createReqVO) {
        // 获得流程定义
        ProcessDefinition definition = processDefinitionService.getProcessDefinition(createReqVO.getProcessDefinitionId());
        // 发起流程
        return createProcessInstance0(userId, definition, createReqVO.getVariables(), null);
    }

    @Override
    public String createProcessInstance(Long userId, BpmProcessInstanceCreateReqDTO createReqDTO) {
        // 获得流程定义
        ProcessDefinition definition = processDefinitionService.getActiveProcessDefinition(createReqDTO.getProcessDefinitionKey());
        // 发起流程
        return createProcessInstance0(userId, definition, createReqDTO.getVariables(), createReqDTO.getBusinessKey());
    }

    private String createProcessInstance0(Long userId, ProcessDefinition definition,
                                          Map<String, Object> variables, String businessKey) {
        // 校验流程定义
        if (definition == null) {
            throw exception(PROCESS_DEFINITION_NOT_EXISTS);
        }
        if (definition.isSuspended()) {
            throw exception(PROCESS_DEFINITION_IS_SUSPENDED);
        }

        // 创建流程实例
        ProcessInstance instance = runtimeService.startProcessInstanceById(definition.getId(), businessKey, variables);
        // 设置流程名字
        runtimeService.setProcessInstanceName(instance.getId(), definition.getName());

        // 补全流程实例的拓展表
        processInstanceExtMapper.updateByProcessInstanceId(new BpmProcessInstanceExtDO().setProcessInstanceId(instance.getId())
                .setFormVariables(variables));

        // 添加初始的评论 TODO 芋艿：在思考下
//        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
//        if (task != null) {
//            SysUserDO user = userService.getUser(userId);
//            Assert.notNull(user, "用户({})不存在", userId);
//            String type = "normal";
//            taskService.addComment(task.getId(), instance.getProcessInstanceId(), type,
//                    String.format("%s 发起流程申请", user.getNickname()));
//        }
        return instance.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelProcessInstance(Long userId, BpmProcessInstanceCancelReqVO cancelReqVO) {
        // 校验流程实例存在
        ProcessInstance instance = getProcessInstance(cancelReqVO.getId());
        if (instance == null) {
            throw exception(PROCESS_INSTANCE_CANCEL_FAIL_NOT_EXISTS);
        }
        // 只能取消自己的
        if (!Objects.equals(instance.getStartUserId(), String.valueOf(userId))) {
            throw exception(PROCESS_INSTANCE_CANCEL_FAIL_NOT_SELF);
        }

        // 通过删除流程实例，实现流程实例的取消
        runtimeService.deleteProcessInstance(cancelReqVO.getId(), cancelReqVO.getReason());
    }

    @Override
    public void deleteProcessInstance(String id, String reason) {
        runtimeService.deleteProcessInstance(id, reason);
    }

    @Override
    public PageResult<BpmProcessInstancePageItemRespVO> getMyProcessInstancePage(Long userId,
                                                                                 BpmProcessInstanceMyPageReqVO pageReqVO) {
        // 通过 BpmProcessInstanceExtDO 表，先查询到对应的分页
        PageResult<BpmProcessInstanceExtDO> pageResult = processInstanceExtMapper.selectPage(userId, pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return new PageResult<>(pageResult.getTotal());
        }

        // 获得流程 Task Map
        List<String> processInstanceIds = convertList(pageResult.getList(), BpmProcessInstanceExtDO::getProcessInstanceId);
        Map<String, List<Task>> taskMap = taskService.getTaskMapByProcessInstanceIds(processInstanceIds);
        // 转换返回
        return BpmProcessInstanceConvert.INSTANCE.convertPage(pageResult, taskMap);
    }

    @Override
    public BpmProcessInstanceRespVO getProcessInstanceVO(String id) {
        // 获得流程实例
        HistoricProcessInstance processInstance = getHistoricProcessInstance(id);
        if (processInstance == null) {
            return null;
        }
        BpmProcessInstanceExtDO processInstanceExt = processInstanceExtMapper.selectByProcessInstanceId(id);
        Assert.notNull(processInstanceExt, "流程实例拓展({}) 不存在", id);

        // 获得流程定义
        ProcessDefinition processDefinition = processDefinitionService.getProcessDefinition(
                processInstance.getProcessDefinitionId());
        Assert.notNull(processDefinition, "流程定义({}) 不存在", processInstance.getProcessDefinitionId());
        BpmProcessDefinitionExtDO processDefinitionExt = processDefinitionService.getProcessDefinitionExt(
                processInstance.getProcessDefinitionId());
        Assert.notNull(processDefinitionExt, "流程定义拓展({}) 不存在", id);
        String bpmnXml = processDefinitionService.getProcessDefinitionBpmnXML(processInstance.getProcessDefinitionId());

        // 获得 User
        SysUserDO startUser = userService.getUser(NumberUtils.parseLong(processInstance.getStartUserId()));
        SysDeptDO dept = null;
        if (startUser != null) {
            dept = deptService.getDept(startUser.getDeptId());
        }

        // 拼接结果
        return BpmProcessInstanceConvert.INSTANCE.convert2(processInstance, processInstanceExt,
                processDefinition, processDefinitionExt, bpmnXml, startUser, dept);
    }

    @Override
    public List<ProcessInstance> getProcessInstances(Set<String> ids) {
        return runtimeService.createProcessInstanceQuery().processInstanceIds(ids).list();
    }

    @Override
    public ProcessInstance getProcessInstance(String id) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    /**
     * 获得历史的流程实例
     *
     * @param id 流程实例的编号
     * @return 历史的流程实例
     */
    @Override
    public HistoricProcessInstance getHistoricProcessInstance(String id) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceId(id).singleResult();
    }

    @Override
    public List<HistoricProcessInstance> getHistoricProcessInstances(Set<String> ids) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceIds(ids).list();
    }

    @Override
    public void createProcessInstanceExt(org.activiti.api.process.model.ProcessInstance instance) {
        // 获得流程定义
        ProcessDefinition definition = processDefinitionService.getProcessDefinition2(instance.getProcessDefinitionId());
        // 插入 BpmProcessInstanceExtDO 对象
        BpmProcessInstanceExtDO instanceExtDO = BpmProcessInstanceConvert.INSTANCE.convert(instance)
                .setCategory(definition.getCategory())
                .setStatus(BpmProcessInstanceStatusEnum.RUNNING.getStatus())
                .setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        processInstanceExtMapper.insert(instanceExtDO);
    }

    @Override
    public void updateProcessInstanceExt(org.activiti.api.process.model.ProcessInstance instance) {
        BpmProcessInstanceExtDO instanceExtDO = BpmProcessInstanceConvert.INSTANCE.convert(instance);
        processInstanceExtMapper.updateByProcessInstanceId(instanceExtDO);
    }

    @Override
    public void updateProcessInstanceExtCancel(org.activiti.api.process.model.ProcessInstance instance, String reason) {
        // 判断是否为 Reject 不通过。如果是，则不进行更新
        if (BpmProcessInstanceDeleteReasonEnum.isRejectReason(reason)) {
            return;
        }

        // 更新拓展表
        BpmProcessInstanceExtDO instanceExtDO = BpmProcessInstanceConvert.INSTANCE.convert(instance)
                .setEndTime(new Date()) // 由于 ProcessInstance 里没有办法拿到 endTime，所以这里设置
                .setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus())
                .setResult(BpmProcessInstanceResultEnum.CANCEL.getResult());
        processInstanceExtMapper.updateByProcessInstanceId(instanceExtDO);
    }

    @Override
    public void updateProcessInstanceExtComplete(org.activiti.api.process.model.ProcessInstance instance) {
        BpmProcessInstanceExtDO instanceExtDO = BpmProcessInstanceConvert.INSTANCE.convert(instance)
                .setEndTime(new Date()) // 由于 ProcessInstance 里没有办法拿到 endTime，所以这里设置
                .setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus())
                .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult()); // 如果正常完全，说明审批通过
        processInstanceExtMapper.updateByProcessInstanceId(instanceExtDO);

        // 发送流程被通过的消息
        messageService.sendMessageWhenProcessInstanceApprove(BpmMessageConvert.INSTANCE.convert(instance));
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProcessInstanceExtReject(String id, String comment) {
        ProcessInstance processInstance = getProcessInstance(id);
        // 删除流程实例，以实现驳回任务时，取消整个审批流程
        deleteProcessInstance(id, StrUtil.format(BpmProcessInstanceDeleteReasonEnum.REJECT_TASK.format(comment)));

        // 更新 status + result
        // 注意，不能和上面的逻辑更换位置。因为 deleteProcessInstance 会触发流程的取消，进而调用 updateProcessInstanceExtCancel 方法，
        // 设置 result 为 BpmProcessInstanceStatusEnum.CANCEL，显然和 result 不一定是一致的
        processInstanceExtMapper.updateByProcessInstanceId(new BpmProcessInstanceExtDO().setProcessInstanceId(id)
                .setStatus(BpmProcessInstanceStatusEnum.FINISH.getStatus())
                .setResult(BpmProcessInstanceResultEnum.REJECT.getResult()));

        // 发送流程被不通过的消息
        messageService.sendMessageWhenProcessInstanceReject(BpmMessageConvert.INSTANCE.convert(processInstance, comment));
    }

}
