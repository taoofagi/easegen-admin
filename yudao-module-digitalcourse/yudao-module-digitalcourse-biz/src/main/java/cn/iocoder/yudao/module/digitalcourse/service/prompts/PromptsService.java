package cn.iocoder.yudao.module.digitalcourse.service.prompts;

import java.util.*;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.prompts.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.prompts.PromptsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import jakarta.validation.Valid;

/**
 * 存储提示词模板的信息，包括提示词的名称、类型、排序等信息 Service 接口
 *
 * @author 芋道源码
 */
public interface PromptsService {

    /**
     * 创建存储提示词模板的信息，包括提示词的名称、类型、排序等信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPrompts(@Valid PromptsSaveReqVO createReqVO);

    /**
     * 更新存储提示词模板的信息，包括提示词的名称、类型、排序等信息
     *
     * @param updateReqVO 更新信息
     */
    void updatePrompts(@Valid PromptsSaveReqVO updateReqVO);

    /**
     * 删除存储提示词模板的信息，包括提示词的名称、类型、排序等信息
     *
     * @param id 编号
     */
    void deletePrompts(Long id);

    /**
     * 获得存储提示词模板的信息，包括提示词的名称、类型、排序等信息
     *
     * @param id 编号
     * @return 存储提示词模板的信息，包括提示词的名称、类型、排序等信息
     */
    PromptsDO getPrompts(Long id);

    /**
     * 获得存储提示词模板的信息，包括提示词的名称、类型、排序等信息分页
     *
     * @param pageReqVO 分页查询
     * @return 存储提示词模板的信息，包括提示词的名称、类型、排序等信息分页
     */
    PageResult<PromptsDO> getPromptsPage(PromptsPageReqVO pageReqVO);

}