package cn.iocoder.yudao.module.digitalcourse.service.prompts;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.prompts.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.prompts.PromptsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.prompts.PromptsMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 存储提示词模板的信息，包括提示词的名称、类型、排序等信息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class PromptsServiceImpl implements PromptsService {

    @Resource
    private PromptsMapper promptsMapper;

    @Override
    public Long createPrompts(PromptsSaveReqVO createReqVO) {
        // 插入
        PromptsDO prompts = BeanUtils.toBean(createReqVO, PromptsDO.class);
        promptsMapper.insert(prompts);
        // 返回
        return prompts.getId();
    }

    @Override
    public void updatePrompts(PromptsSaveReqVO updateReqVO) {
        // 校验存在
        validatePromptsExists(updateReqVO.getId());
        // 更新
        PromptsDO updateObj = BeanUtils.toBean(updateReqVO, PromptsDO.class);
        promptsMapper.updateById(updateObj);
    }

    @Override
    public void deletePrompts(Long id) {
        // 校验存在
        validatePromptsExists(id);
        // 删除
        promptsMapper.deleteById(id);
    }

    private void validatePromptsExists(Long id) {
        if (promptsMapper.selectById(id) == null) {
            throw exception(PROMPTS_NOT_EXISTS);
        }
    }

    @Override
    public PromptsDO getPrompts(Long id) {
        return promptsMapper.selectById(id);
    }

    @Override
    public PageResult<PromptsDO> getPromptsPage(PromptsPageReqVO pageReqVO) {
        return promptsMapper.selectPage(pageReqVO);
    }

}