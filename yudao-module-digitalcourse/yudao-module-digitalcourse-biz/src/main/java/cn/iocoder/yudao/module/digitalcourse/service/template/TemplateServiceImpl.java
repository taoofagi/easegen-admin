package cn.iocoder.yudao.module.digitalcourse.service.template;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.template.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.template.TemplateDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.template.TemplateMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 模板 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class TemplateServiceImpl implements TemplateService {

    @Resource
    private TemplateMapper templateMapper;

    @Override
    public Long createTemplate(TemplateSaveReqVO createReqVO) {
        // 插入
        TemplateDO template = BeanUtils.toBean(createReqVO, TemplateDO.class);
        templateMapper.insert(template);
        // 返回
        return template.getId();
    }

    @Override
    public void updateTemplate(TemplateSaveReqVO updateReqVO) {
        // 校验存在
        validateTemplateExists(updateReqVO.getId());
        // 更新
        TemplateDO updateObj = BeanUtils.toBean(updateReqVO, TemplateDO.class);
        templateMapper.updateById(updateObj);
    }

    @Override
    public void deleteTemplate(Long id) {
        // 校验存在
        validateTemplateExists(id);
        // 删除
        templateMapper.deleteById(id);
    }

    private void validateTemplateExists(Long id) {
        if (templateMapper.selectById(id) == null) {
            throw exception(TEMPLATE_NOT_EXISTS);
        }
    }

    @Override
    public TemplateDO getTemplate(Long id) {
        return templateMapper.selectById(id);
    }

    @Override
    public PageResult<TemplateDO> getTemplatePage(TemplatePageReqVO pageReqVO) {
        return templateMapper.selectPage(pageReqVO);
    }

}