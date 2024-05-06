package cn.iocoder.yudao.module.ai.controller;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.ai.service.AiChatConversationService;
import cn.iocoder.yudao.module.ai.vo.AiChatConversationCreateRoleReq;
import cn.iocoder.yudao.module.ai.vo.AiChatConversationCreateUserReq;
import cn.iocoder.yudao.module.ai.vo.AiChatConversationListReq;
import cn.iocoder.yudao.module.ai.vo.AiChatConversationRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ia 模块
 *
 * @author fansili
 * @time 2024/4/13 17:44
 * @since 1.0
 */
@Tag(name = "A2-聊天-对话")
@RestController
@RequestMapping("/ai/chat/conversation")
@Slf4j
@AllArgsConstructor
public class AiChatConversationController {

    private final AiChatConversationService chatConversationService;

    @Operation(summary = "创建 - 对话普通对话")
    @PutMapping("/createConversation")
    public CommonResult<AiChatConversationRes> createConversation(@RequestBody @Validated AiChatConversationCreateUserReq req) {
        return CommonResult.success(chatConversationService.createConversation(req));
    }

    @Operation(summary = "创建 - 对话角色对话")
    @PutMapping("/createRoleConversation")
    public CommonResult<AiChatConversationRes> createRoleConversation(@RequestBody @Validated AiChatConversationCreateRoleReq req) {
        return CommonResult.success(chatConversationService.createRoleConversation(req));
    }

    @Operation(summary = "获取 - 获取对话")
    @GetMapping("/{id}")
    public CommonResult<AiChatConversationRes> getConversation(@PathVariable("id") Long id) {
        return CommonResult.success(chatConversationService.getConversation(id));
    }

    @Operation(summary = "获取 - 获取对话list")
    @GetMapping("/list")
    public CommonResult<List<AiChatConversationRes>> listConversation(@ModelAttribute @Validated AiChatConversationListReq req) {
        return CommonResult.success(chatConversationService.listConversation(req));
    }

    @Operation(summary = "更新 - 更新模型")
    @PostMapping("/{id}/modal")
    public CommonResult<Void> updateModal(@PathVariable("id") Long id,
                                          @RequestParam("modalId") Long modalId) {
        chatConversationService.updateModal(id, modalId);
        return CommonResult.success(null);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public CommonResult<Void> delete(@PathVariable("id") Long id) {
        chatConversationService.delete(id);
        return CommonResult.success(null);
    }
}
