package cn.iocoder.yudao.module.bpm.service.task;import cn.hutool.core.collection.CollUtil;import cn.hutool.core.util.ArrayUtil;import cn.hutool.core.util.StrUtil;import cn.iocoder.yudao.framework.common.pojo.PageResult;import cn.iocoder.yudao.framework.common.util.date.DateUtils;import cn.iocoder.yudao.framework.common.util.object.PageUtils;import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;import cn.iocoder.yudao.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceCancelReqVO;import cn.iocoder.yudao.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceCreateReqVO;import cn.iocoder.yudao.module.bpm.controller.admin.task.vo.instance.BpmProcessInstancePageReqVO;import cn.iocoder.yudao.module.bpm.convert.task.BpmProcessInstanceConvert;import cn.iocoder.yudao.module.bpm.enums.task.BpmProcessInstanceStatusEnum;import cn.iocoder.yudao.module.bpm.enums.task.BpmReasonEnum;import cn.iocoder.yudao.module.bpm.framework.flowable.core.candidate.strategy.BpmTaskCandidateStartUserSelectStrategy;import cn.iocoder.yudao.module.bpm.framework.flowable.core.enums.BpmConstants;import cn.iocoder.yudao.module.bpm.framework.flowable.core.event.BpmProcessInstanceEventPublisher;import cn.iocoder.yudao.module.bpm.framework.flowable.core.util.FlowableUtils;import cn.iocoder.yudao.module.bpm.service.definition.BpmProcessDefinitionService;import cn.iocoder.yudao.module.bpm.service.message.BpmMessageService;import cn.iocoder.yudao.module.system.api.user.AdminUserApi;import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;import jakarta.annotation.Resource;import jakarta.validation.Valid;import lombok.extern.slf4j.Slf4j;import org.flowable.bpmn.model.BpmnModel;import org.flowable.bpmn.model.UserTask;import org.flowable.engine.HistoryService;import org.flowable.engine.RuntimeService;import org.flowable.engine.history.HistoricProcessInstance;import org.flowable.engine.history.HistoricProcessInstanceQuery;import org.flowable.engine.repository.ProcessDefinition;import org.flowable.engine.runtime.ProcessInstance;import org.springframework.context.annotation.Lazy;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import org.springframework.validation.annotation.Validated;import java.util.*;import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;import static cn.iocoder.yudao.module.bpm.enums.ErrorCodeConstants.*;/** * 流程实例 Service 实现类 * * ProcessDefinition & ProcessInstance & Execution & Task 的关系： *  1. <a href="https://blog.csdn.net/bobozai86/article/details/105210414" /> * * HistoricProcessInstance & ProcessInstance 的关系： *  1. <a href=" https://my.oschina.net/843294669/blog/71902" /> * * 简单来说，前者 = 历史 + 运行中的流程实例，后者仅是运行中的流程实例 * * @author 芋道源码 */@Service@Validated@Slf4jpublic class BpmProcessInstanceServiceImpl implements BpmProcessInstanceService {    @Resource    private RuntimeService runtimeService;    @Resource    private HistoryService historyService;    @Resource    private BpmProcessDefinitionService processDefinitionService;    @Resource    @Lazy // 避免循环依赖    private BpmTaskService taskService;    @Resource    private BpmMessageService messageService;    @Resource    private AdminUserApi adminUserApi;    @Resource    private BpmProcessInstanceEventPublisher processInstanceEventPublisher;    // ========== Query 查询相关方法 ==========    @Override    public ProcessInstance getProcessInstance(String id) {        return runtimeService.createProcessInstanceQuery()                .includeProcessVariables()                .processInstanceId(id)                .singleResult();    }    @Override    public List<ProcessInstance> getProcessInstances(Set<String> ids) {        return runtimeService.createProcessInstanceQuery().processInstanceIds(ids).list();    }    @Override    public HistoricProcessInstance getHistoricProcessInstance(String id) {        return historyService.createHistoricProcessInstanceQuery().processInstanceId(id).includeProcessVariables().singleResult();    }    @Override    public List<HistoricProcessInstance> getHistoricProcessInstances(Set<String> ids) {        return historyService.createHistoricProcessInstanceQuery().processInstanceIds(ids).list();    }    @Override    public PageResult<HistoricProcessInstance> getProcessInstancePage(Long userId,                                                                      BpmProcessInstancePageReqVO pageReqVO) {        // 通过 BpmProcessInstanceExtDO 表，先查询到对应的分页        HistoricProcessInstanceQuery processInstanceQuery = historyService.createHistoricProcessInstanceQuery()                .includeProcessVariables()                .processInstanceTenantId(FlowableUtils.getTenantId())                .orderByProcessInstanceStartTime().desc();        if (userId != null) { // 【我的流程】菜单时，需要传递该字段            processInstanceQuery.startedBy(String.valueOf(userId));        }  else if (pageReqVO.getStartUserId() != null) { // 【管理流程】菜单时，才会传递该字段            processInstanceQuery.startedBy(String.valueOf(pageReqVO.getStartUserId()));        }        if (StrUtil.isNotEmpty(pageReqVO.getName())) {            processInstanceQuery.processInstanceNameLike("%" + pageReqVO.getName() + "%");        }        if (StrUtil.isNotEmpty(pageReqVO.getProcessDefinitionId())) {            processInstanceQuery.processDefinitionId("%" + pageReqVO.getProcessDefinitionId() + "%");        }        if (StrUtil.isNotEmpty(pageReqVO.getCategory())) {            processInstanceQuery.processDefinitionCategory(pageReqVO.getCategory());        }        if (pageReqVO.getStatus() != null) {            processInstanceQuery.variableValueEquals(BpmConstants.PROCESS_INSTANCE_VARIABLE_STATUS, pageReqVO.getStatus());        }        if (ArrayUtil.isNotEmpty(pageReqVO.getCreateTime())) {            processInstanceQuery.startedAfter(DateUtils.of(pageReqVO.getCreateTime()[0]));            processInstanceQuery.startedBefore(DateUtils.of(pageReqVO.getCreateTime()[1]));        }        // 查询数量        long processInstanceCount = processInstanceQuery.count();        if (processInstanceCount == 0) {            return PageResult.empty(processInstanceCount);        }        // 查询列表        List<HistoricProcessInstance> processInstanceList = processInstanceQuery.listPage(PageUtils.getStart(pageReqVO), pageReqVO.getPageSize());        return new PageResult<>(processInstanceList, processInstanceCount);    }    // ========== Update 写入相关方法 ==========    @Override    @Transactional(rollbackFor = Exception.class)    public String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqVO createReqVO) {        // 获得流程定义        ProcessDefinition definition = processDefinitionService.getProcessDefinition(createReqVO.getProcessDefinitionId());        // 发起流程        return createProcessInstance0(userId, definition, createReqVO.getVariables(), null,                createReqVO.getStartUserSelectAssignees());    }    @Override    public String createProcessInstance(Long userId, @Valid BpmProcessInstanceCreateReqDTO createReqDTO) {        // 获得流程定义        ProcessDefinition definition = processDefinitionService.getActiveProcessDefinition(createReqDTO.getProcessDefinitionKey());        // 发起流程        return createProcessInstance0(userId, definition, createReqDTO.getVariables(), createReqDTO.getBusinessKey(),                createReqDTO.getStartUserSelectAssignees());    }    private String createProcessInstance0(Long userId, ProcessDefinition definition,                                          Map<String, Object> variables, String businessKey,                                          Map<String, List<Long>> startUserSelectAssignees) {        // 1.1 校验流程定义        if (definition == null) {            throw exception(PROCESS_DEFINITION_NOT_EXISTS);        }        if (definition.isSuspended()) {            throw exception(PROCESS_DEFINITION_IS_SUSPENDED);        }        // 1.2 校验发起人自选审批人        validateStartUserSelectAssignees(definition, startUserSelectAssignees);        // 2. 创建流程实例        if (variables == null) {            variables = new HashMap<>();        }        FlowableUtils.filterProcessInstanceFormVariable(variables); // 过滤一下，避免 ProcessInstance 系统级的变量被占用        variables.put(BpmConstants.PROCESS_INSTANCE_VARIABLE_STATUS, // 流程实例状态：审批中                BpmProcessInstanceStatusEnum.RUNNING.getStatus());        if (CollUtil.isNotEmpty(startUserSelectAssignees)) {            variables.put(BpmConstants.PROCESS_INSTANCE_VARIABLE_START_USER_SELECT_ASSIGNEES, startUserSelectAssignees);        }        ProcessInstance instance = runtimeService.createProcessInstanceBuilder()                .processDefinitionId(definition.getId())                .businessKey(businessKey)                .name(definition.getName().trim())                .variables(variables)                .start();        return instance.getId();    }    private void validateStartUserSelectAssignees(ProcessDefinition definition, Map<String, List<Long>> startUserSelectAssignees) {        // 1. 获得发起人自选审批人的 UserTask 列表        BpmnModel bpmnModel = processDefinitionService.getProcessDefinitionBpmnModel(definition.getId());        List<UserTask> userTaskList = BpmTaskCandidateStartUserSelectStrategy.getStartUserSelectUserTaskList(bpmnModel);        if (CollUtil.isEmpty(userTaskList)) {            return;        }        // 2. 校验发起人自选审批人的 UserTask 是否都配置了        userTaskList.forEach(userTask -> {            List<Long> assignees = startUserSelectAssignees != null ? startUserSelectAssignees.get(userTask.getId()) : null;            if (CollUtil.isEmpty(assignees)) {                throw exception(PROCESS_INSTANCE_START_USER_SELECT_ASSIGNEES_NOT_CONFIG, userTask.getName());            }            Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(assignees);            assignees.forEach(assignee -> {                if (userMap.get(assignee) == null) {                    throw exception(PROCESS_INSTANCE_START_USER_SELECT_ASSIGNEES_NOT_EXISTS, userTask.getName(), assignee);                }            });        });    }    @Override    public void cancelProcessInstanceByStartUser(Long userId, @Valid BpmProcessInstanceCancelReqVO cancelReqVO) {        // 1.1 校验流程实例存在        ProcessInstance instance = getProcessInstance(cancelReqVO.getId());        if (instance == null) {            throw exception(PROCESS_INSTANCE_CANCEL_FAIL_NOT_EXISTS);        }        // 1.2 只能取消自己的        if (!Objects.equals(instance.getStartUserId(), String.valueOf(userId))) {            throw exception(PROCESS_INSTANCE_CANCEL_FAIL_NOT_SELF);        }        // 2. 取消流程        updateProcessInstanceCancel(cancelReqVO.getId(),                BpmReasonEnum.CANCEL_PROCESS_INSTANCE_BY_START_USER.format(cancelReqVO.getReason()));    }    @Override    public void cancelProcessInstanceByAdmin(Long userId, BpmProcessInstanceCancelReqVO cancelReqVO) {        // 1.1 校验流程实例存在        ProcessInstance instance = getProcessInstance(cancelReqVO.getId());        if (instance == null) {            throw exception(PROCESS_INSTANCE_CANCEL_FAIL_NOT_EXISTS);        }        // 2. 取消流程        AdminUserRespDTO user = adminUserApi.getUser(userId);        updateProcessInstanceCancel(cancelReqVO.getId(),                BpmReasonEnum.CANCEL_PROCESS_INSTANCE_BY_ADMIN.format(user.getNickname(), cancelReqVO.getReason()));    }    private void updateProcessInstanceCancel(String id, String reason) {        // 1. 更新流程实例 status        runtimeService.setVariable(id, BpmConstants.PROCESS_INSTANCE_VARIABLE_STATUS,                BpmProcessInstanceStatusEnum.CANCEL.getStatus());        runtimeService.setVariable(id, BpmConstants.PROCESS_INSTANCE_VARIABLE_REASON, reason);        // 2. 结束流程        taskService.moveTaskToEnd(id);    }    @Override    public void updateProcessInstanceReject(ProcessInstance processInstance, String reason) {        runtimeService.setVariable(processInstance.getProcessInstanceId(), BpmConstants.PROCESS_INSTANCE_VARIABLE_STATUS,                BpmProcessInstanceStatusEnum.REJECT.getStatus());        runtimeService.setVariable(processInstance.getProcessInstanceId(), BpmConstants.PROCESS_INSTANCE_VARIABLE_REASON,                BpmReasonEnum.REJECT_TASK.format(reason));    }    // ========== Event 事件相关方法 ==========    @Override    public void processProcessInstanceCompleted(ProcessInstance instance) {        // 1.1 获取当前状态        Integer status = (Integer) instance.getProcessVariables().get(BpmConstants.PROCESS_INSTANCE_VARIABLE_STATUS);        String reason = (String) instance.getProcessVariables().get(BpmConstants.PROCESS_INSTANCE_VARIABLE_REASON);        // 1.2 当流程状态还是审批状态中，说明审批通过了，则变更下它的状态        // 为什么这么处理？因为流程完成，并且完成了，说明审批通过了        if (Objects.equals(status, BpmProcessInstanceStatusEnum.RUNNING.getStatus())) {            status = BpmProcessInstanceStatusEnum.APPROVE.getStatus();            runtimeService.setVariable(instance.getId(), BpmConstants.PROCESS_INSTANCE_VARIABLE_STATUS, status);        }        // 2. 发送对应的消息通知        if (Objects.equals(status, BpmProcessInstanceStatusEnum.APPROVE.getStatus())) {            messageService.sendMessageWhenProcessInstanceApprove(BpmProcessInstanceConvert.INSTANCE.buildProcessInstanceApproveMessage(instance));        } else if (Objects.equals(status, BpmProcessInstanceStatusEnum.REJECT.getStatus())) {            messageService.sendMessageWhenProcessInstanceReject(                    BpmProcessInstanceConvert.INSTANCE.buildProcessInstanceRejectMessage(instance, reason));        }        // 3. 发送流程实例的状态事件        processInstanceEventPublisher.sendProcessInstanceResultEvent(                BpmProcessInstanceConvert.INSTANCE.buildProcessInstanceStatusEvent(this, instance, status));    }}