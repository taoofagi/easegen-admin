package cn.iocoder.yudao.module.digitalcourse.service.digitalhumans;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.digitalhumans.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.digitalhumans.DigitalHumansDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.digitalhumans.DigitalHumansMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 数字人模型 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DigitalHumansServiceImpl implements DigitalHumansService {

    @Resource
    private DigitalHumansMapper digitalHumansMapper;

    @Override
    public Long createDigitalHumans(DigitalHumansSaveReqVO createReqVO) {
        // 插入
        DigitalHumansDO digitalHumans = BeanUtils.toBean(createReqVO, DigitalHumansDO.class);
        digitalHumansMapper.insert(digitalHumans);
        // 返回
        return digitalHumans.getId();
    }

    @Override
    public void updateDigitalHumans(DigitalHumansSaveReqVO updateReqVO) {
        // 校验存在
        validateDigitalHumansExists(updateReqVO.getId());
        // 更新
        DigitalHumansDO updateObj = BeanUtils.toBean(updateReqVO, DigitalHumansDO.class);
        digitalHumansMapper.updateById(updateObj);
    }

    @Override
    public void deleteDigitalHumans(Long id) {
        // 校验存在
        validateDigitalHumansExists(id);
        // 删除
        digitalHumansMapper.deleteById(id);
    }

    private void validateDigitalHumansExists(Long id) {
        if (digitalHumansMapper.selectById(id) == null) {
            throw exception(DIGITAL_HUMANS_NOT_EXISTS);
        }
    }

    @Override
    public DigitalHumansDO getDigitalHumans(Long id) {
        return digitalHumansMapper.selectById(id);
    }

    @Override
    public PageResult<DigitalHumansDO> getDigitalHumansPage(DigitalHumansPageReqVO pageReqVO) {
        return digitalHumansMapper.selectPage(pageReqVO);
    }

}