package cn.iocoder.yudao.module.digitalcourse.service.voices;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.voices.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.voices.VoicesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.voices.VoicesMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 声音管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class VoicesServiceImpl implements VoicesService {

    @Resource
    private VoicesMapper voicesMapper;

    @Override
    public Long createVoices(VoicesSaveReqVO createReqVO) {
        // 插入
        VoicesDO voices = BeanUtils.toBean(createReqVO, VoicesDO.class);
        voicesMapper.insert(voices);
        // 返回
        return voices.getId();
    }

    @Override
    public void updateVoices(VoicesSaveReqVO updateReqVO) {
        // 校验存在
        validateVoicesExists(updateReqVO.getId());
        // 更新
        VoicesDO updateObj = BeanUtils.toBean(updateReqVO, VoicesDO.class);
        voicesMapper.updateById(updateObj);
    }

    @Override
    public void deleteVoices(Long id) {
        // 校验存在
        validateVoicesExists(id);
        // 删除
        voicesMapper.deleteById(id);
    }

    private void validateVoicesExists(Long id) {
        if (voicesMapper.selectById(id) == null) {
            throw exception(VOICES_NOT_EXISTS);
        }
    }

    @Override
    public VoicesDO getVoices(Long id) {
        return voicesMapper.selectById(id);
    }

    @Override
    public PageResult<VoicesDO> getVoicesPage(VoicesPageReqVO pageReqVO) {
        return voicesMapper.selectPage(pageReqVO);
    }

}