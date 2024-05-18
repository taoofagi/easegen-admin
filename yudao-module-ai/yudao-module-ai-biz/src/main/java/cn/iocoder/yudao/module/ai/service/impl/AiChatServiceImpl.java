package cn.iocoder.yudao.module.ai.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.ai.core.enums.AiPlatformEnum;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ai.controller.admin.chat.vo.message.AiChatMessageSendRespVO;
import cn.iocoder.yudao.module.ai.dal.dataobject.chat.AiChatConversationDO;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.ChatOptionsBuilder;
import org.springframework.ai.chat.prompt.Prompt;
import cn.iocoder.yudao.module.ai.config.AiChatClientFactory;
import cn.iocoder.yudao.module.ai.controller.admin.chat.vo.message.AiChatMessageRespVO;
import cn.iocoder.yudao.module.ai.controller.admin.chat.vo.message.AiChatMessageSendReqVO;
import cn.iocoder.yudao.module.ai.convert.AiChatMessageConvert;
import cn.iocoder.yudao.module.ai.dal.dataobject.chat.AiChatMessageDO;
import cn.iocoder.yudao.module.ai.dal.dataobject.model.AiChatModelDO;
import cn.iocoder.yudao.module.ai.dal.mysql.AiChatMessageMapper;
import cn.iocoder.yudao.module.ai.service.chat.AiChatConversationService;
import cn.iocoder.yudao.module.ai.service.model.AiChatRoleService;
import cn.iocoder.yudao.module.ai.service.AiChatService;
import cn.iocoder.yudao.module.ai.service.model.AiChatModelService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.ai.ErrorCodeConstants.CHAT_CONVERSATION_NOT_EXISTS;

/**
 * 聊天 service
 *
 * @author fansili
 * @time 2024/4/14 15:55
 * @since 1.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final AiChatClientFactory chatClientFactory;

    private final AiChatMessageMapper chatMessageMapper;

    private final AiChatConversationService chatConversationService;
    private final AiChatModelService chatModalService;
    private final AiChatRoleService chatRoleService;

    @Transactional(rollbackFor = Exception.class)
    public AiChatMessageRespVO chat(AiChatMessageSendReqVO req) {
         return null; // TODO 芋艿：一起改
//        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
//        // 查询对话
//        AiChatConversationDO conversation = chatConversationService.validateExists(req.getConversationId());
//        // 获取对话模型
//        AiChatModelDO chatModel = chatModalService.validateChatModel(conversation.getModelId());
//        // 获取角色信息
//        AiChatRoleDO chatRoleDO = conversation.getRoleId() != null ? chatRoleService.validateChatRole(conversation.getRoleId()) : null;
//        // 获取 client 类型
//        AiPlatformEnum platformEnum = AiPlatformEnum.validatePlatform(chatModel.getPlatform());
//        // 保存 chat message
//        createChatMessage(conversation.getId(), MessageType.USER, loginUserId, conversation.getRoleId(),
//                chatModel.getModel(), chatModel.getId(), req.getContent());
//        String content = null;
//        int tokens = 0;
//        try {
//            // 创建 chat 需要的 Prompt
//            Prompt prompt = new Prompt(req.getContent());
//            // TODO @芋艿 @范 看要不要支持这些
////            req.setTopK(req.getTopK());
////            req.setTopP(req.getTopP());
////            req.setTemperature(req.getTemperature());
//            // 发送 call 调用
//            ChatClient chatClient = chatClientFactory.getChatClient(platformEnum);
//            ChatResponse call = chatClient.call(prompt);
//            content = call.getResult().getOutput().getContent();
//            // 更新 conversation
//        } catch (Exception e) {
//            content = ExceptionUtil.getMessage(e);
//        } finally {
//            // 保存 chat message
//            createChatMessage(conversation.getId(), MessageType.SYSTEM, loginUserId, conversation.getRoleId(),
//                    chatModel.getModel(), chatModel.getId(), content);
//        }
//        return new AiChatMessageRespVO().setContent(content);
    }

    @Override
    public Flux<AiChatMessageSendRespVO> sendChatMessageStream(AiChatMessageSendReqVO sendReqVO, Long userId) {
        // 1.1 校验对话存在
        AiChatConversationDO conversation = chatConversationService.validateExists(sendReqVO.getConversationId());
        if (ObjUtil.notEqual(conversation.getUserId(), userId)) {
            throw exception(CHAT_CONVERSATION_NOT_EXISTS); // TODO 芋艿：异常情况的对接；
        }
        List<AiChatMessageDO> historyMessages = chatMessageMapper.selectByConversationId(conversation.getId());
        // 1.2 校验模型
        AiChatModelDO model = chatModalService.validateChatModel(conversation.getModelId());
        AiPlatformEnum platform = AiPlatformEnum.validatePlatform(model.getPlatform());
        StreamingChatClient chatClient = chatClientFactory.getStreamingChatClient(platform);

        // 2. 插入 user 发送消息
        AiChatMessageDO userMessage = createChatMessage(conversation.getId(), model,
                userId, conversation.getRoleId(), MessageType.USER, sendReqVO.getContent());

        // 3.1 插入 assistant 接收消息
        AiChatMessageDO assistantMessage = createChatMessage(conversation.getId(), model,
                userId, conversation.getRoleId(), MessageType.ASSISTANT, "");

        // 3.2 创建 chat 需要的 Prompt
        // TODO 消息上下文
        Prompt prompt = buildPrompt(conversation, historyMessages, sendReqVO);
        Flux<ChatResponse> streamResponse = chatClient.stream(prompt);

        // 3.3 流式返回
        StringBuffer contentBuffer = new StringBuffer();
        return streamResponse.map(response -> {
            String newContent = response.getResult().getOutput().getContent();
            contentBuffer.append(newContent);
            // 响应结果
            return new AiChatMessageSendRespVO().setSend(BeanUtils.toBean(userMessage, AiChatMessageSendRespVO.Message.class))
                    .setReceive(BeanUtils.toBean(userMessage, AiChatMessageSendRespVO.Message.class).setContent(newContent));
        }).doOnComplete(() -> {
            chatMessageMapper.updateById(new AiChatMessageDO().setId(assistantMessage.getId()).setContent(contentBuffer.toString()));
        }).doOnError(throwable -> {
            log.error("[sendChatMessageStream][userId({}) sendReqVO({}) 发生异常]", userId, sendReqVO, throwable);
            chatMessageMapper.updateById(new AiChatMessageDO().setId(assistantMessage.getId()).setContent(throwable.getMessage()));
        });
    }

    private Prompt buildPrompt(AiChatConversationDO conversation, List<AiChatMessageDO> messages, AiChatMessageSendReqVO sendReqVO) {
        // TODO 芋艿：1）保留 n 个上下文；2）每一轮 token 数量
//        if (conversation.getMaxContexts() != null && messages.size() > conversation.getMaxContexts()) {
//
//        }
        // 1. 构建 Prompt Message 列表
        List<Message> chatMessages = new ArrayList<>();
        // 1.1 system context 角色设定
        chatMessages.add(new SystemMessage(conversation.getSystemMessage()));
        // 1.2 history message 历史消息
        messages.forEach(message -> chatMessages.add(new ChatMessage(message.getType().toUpperCase(), message.getContent())));
        // 1.3 user message 新发送消息
        chatMessages.add(new UserMessage(sendReqVO.getContent()));

        // 2. 构建 ChatOptions 对象 TODO 芋艿：临时注释掉；等文心一言兼容了；
//        ChatOptions chatOptions = ChatOptionsBuilder.builder().withTemperature(conversation.getTemperature().floatValue()).build();
        return new Prompt(chatMessages, null);
    }

    private AiChatMessageDO createChatMessage(Long conversationId, AiChatModelDO model,
                                              Long userId, Long roleId,
                                              MessageType messageType, String content) {
        AiChatMessageDO message = new AiChatMessageDO()
                .setConversationId(conversationId).setModel(model.getModel()).setModelId(model.getId())
                .setUserId(userId).setRoleId(roleId)
                .setType(messageType.getValue()).setContent(content);
        message.setCreateTime(LocalDateTime.now());
        chatMessageMapper.insert(message);
        return message;
    }

    @Override
    public List<AiChatMessageRespVO> getMessageListByConversationId(Long conversationId) {
        // 校验对话是否存在
        chatConversationService.validateExists(conversationId);
        // 获取对话所有 message
        List<AiChatMessageDO> aiChatMessageDOList = chatMessageMapper.selectByConversationId(conversationId);
        // 获取模型信息
        Set<Long> modalIds = aiChatMessageDOList.stream().map(AiChatMessageDO::getModelId).collect(Collectors.toSet());
        List<AiChatModelDO> modalList = chatModalService.getModalByIds(modalIds);
        Map<Long, AiChatModelDO> modalIdMap = modalList.stream().collect(Collectors.toMap(AiChatModelDO::getId, o -> o));
        // 转换 AiChatMessageRespVO
        List<AiChatMessageRespVO> aiChatMessageRespList = AiChatMessageConvert.INSTANCE.convertAiChatMessageRespVOList(aiChatMessageDOList);
        // 设置用户头像 和 模型头像 todo @芋艿 这里需要转换 用户头像、模型头像
        return aiChatMessageRespList.stream().map(item -> {
            if (modalIdMap.containsKey(item.getModelId())) {
//                modalIdMap.get(item.getModelId());
//                item.setModelImage()
            }
            return item;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteMessage(Long id) {
        return chatMessageMapper.deleteById(id) > 0;
    }

}
