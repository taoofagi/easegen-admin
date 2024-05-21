package cn.iocoder.yudao.module.ai.service.chat;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ai.controller.admin.chat.vo.conversation.AiChatConversationCreateMyReqVO;
import cn.iocoder.yudao.module.ai.controller.admin.chat.vo.conversation.AiChatConversationUpdateMyReqVO;
import cn.iocoder.yudao.module.ai.dal.dataobject.chat.AiChatConversationDO;
import cn.iocoder.yudao.module.ai.dal.dataobject.model.AiChatModelDO;
import cn.iocoder.yudao.module.ai.dal.dataobject.model.AiChatRoleDO;
import cn.iocoder.yudao.module.ai.dal.mysql.chat.AiChatConversationMapper;
import cn.iocoder.yudao.module.ai.service.model.AiChatModelService;
import cn.iocoder.yudao.module.ai.service.model.AiChatRoleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ai.ErrorCodeConstants.CHAT_CONVERSATION_MODEL_ERROR;
import static cn.iocoder.yudao.module.ai.ErrorCodeConstants.CHAT_CONVERSATION_NOT_EXISTS;

/**
 * AI 聊天对话 Service 实现类
 *
 * @author fansili
 */
@Service
@Validated
@Slf4j
public class AiChatConversationServiceImpl implements AiChatConversationService {

    @Resource
    private AiChatConversationMapper chatConversationMapper;

    @Resource
    private AiChatModelService chatModalService;
    @Resource
    private AiChatRoleService chatRoleService;

    @Override
    public Long createChatConversationMy(AiChatConversationCreateMyReqVO createReqVO, Long userId) {
        // 1.1 获得 AiChatRoleDO 聊天角色
        AiChatRoleDO role = createReqVO.getRoleId() != null ? chatRoleService.validateChatRole(createReqVO.getRoleId())
                : chatRoleService.getRequiredDefaultChatRole();
        Assert.notNull(role, "必须找到聊天角色");
        // 1.2 获得 AiChatModelDO 聊天模型
        AiChatModelDO model = role.getModelId() != null ? chatModalService.validateChatModel(role.getModelId())
                : chatModalService.getRequiredDefaultChatModel();
        Assert.notNull(model, "必须找到默认模型");
        validateChatModel(model);

        // 2. 创建 AiChatConversationDO 聊天对话
        String title = createReqVO.getRoleId() == null ? AiChatConversationDO.TITLE_DEFAULT : role.getName();
        AiChatConversationDO conversation = new AiChatConversationDO()
                .setUserId(userId).setTitle(title).setPinned(false)
                .setRoleId(role.getId()).setModelId(model.getId()).setModel(model.getModel()).setSystemMessage(role.getSystemMessage())
                .setTemperature(model.getTemperature()).setMaxTokens(model.getMaxTokens()).setMaxContexts(model.getMaxContexts());
        chatConversationMapper.insert(conversation);
        return conversation.getId();
    }

    @Override
    public void updateChatConversationMy(AiChatConversationUpdateMyReqVO updateReqVO, Long userId) {
        // 1.1 校验对话是否存在
        AiChatConversationDO conversation = validateExists(updateReqVO.getId());
        if (ObjUtil.notEqual(conversation.getUserId(), userId)) {
            throw exception(CHAT_CONVERSATION_NOT_EXISTS);
        }
        // 1.2 校验模型是否存在（修改模型的情况）
        AiChatModelDO model = null;
        if (updateReqVO.getModelId() != null) {
            model = chatModalService.validateChatModel(updateReqVO.getModelId());
        }

        // 2. 更新对话信息
        AiChatConversationDO updateObj = BeanUtils.toBean(updateReqVO, AiChatConversationDO.class);
        if (Boolean.TRUE.equals(updateReqVO.getPinned())) {
            updateObj.setPinnedTime(LocalDateTime.now());
        }
        if (model != null) {
            updateObj.setModel(model.getModel());
        }
        chatConversationMapper.updateById(updateObj);
    }

    @Override
    public List<AiChatConversationDO> getChatConversationListByUserId(Long userId) {
        return chatConversationMapper.selectListByUserId(userId);
    }

    @Override
    public AiChatConversationDO getChatConversation(Long id) {
        return chatConversationMapper.selectById(id);
    }

    @Override
    public void deleteChatConversationMy(Long id, Long userId) {
        // 1. 校验对话是否存在
        AiChatConversationDO conversation = validateExists(id);
        if (ObjUtil.notEqual(conversation.getUserId(), userId)) {
            throw exception(CHAT_CONVERSATION_NOT_EXISTS);
        }

        // 2. 执行删除
        chatConversationMapper.deleteById(id);
    }

    private void validateChatModel(AiChatModelDO model) {
        if (ObjectUtil.isAllNotEmpty(model.getTemperature(), model.getMaxTokens(), model.getMaxContexts())) {
            return;
        }
        throw exception(CHAT_CONVERSATION_MODEL_ERROR);
    }

    public AiChatConversationDO validateExists(Long id) {
        AiChatConversationDO conversation = chatConversationMapper.selectById(id);
        if (conversation == null) {
            throw exception(CHAT_CONVERSATION_NOT_EXISTS);
        }
        return conversation;
    }

    @Override
    public void deleteMyAllExceptPinned(Long loginUserId) {
        chatConversationMapper.delete(
                new LambdaQueryWrapperX<AiChatConversationDO>()
                        .eq(AiChatConversationDO::getUserId, loginUserId)
                        .eq(AiChatConversationDO::getPinned, false)
        );
    }

}
