package cn.iocoder.yudao.module.digitalcourse.dal.mysql.prompts;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.prompts.PromptsDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.prompts.vo.*;

/**
 * 存储提示词模板的信息，包括提示词的名称、类型、排序等信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface PromptsMapper extends BaseMapperX<PromptsDO> {

    default PageResult<PromptsDO> selectPage(PromptsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PromptsDO>()
                .likeIfPresent(PromptsDO::getName, reqVO.getName())
                .eqIfPresent(PromptsDO::getContent, reqVO.getContent())
                .eqIfPresent(PromptsDO::getOrder, reqVO.getOrder())
                .eqIfPresent(PromptsDO::getType, reqVO.getType())
                .betweenIfPresent(PromptsDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(PromptsDO::getStatus, reqVO.getStatus())
                .orderByDesc(PromptsDO::getId));
    }

}