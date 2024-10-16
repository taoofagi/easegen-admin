package cn.iocoder.yudao.module.digitalcourse.service.backgrounds;

import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.backgrounds.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.backgrounds.BackgroundsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.backgrounds.BackgroundsMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 背景信息（PPT背景、板书、插图、字幕等） Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class BackgroundsServiceImpl implements BackgroundsService {

    @Resource
    private BackgroundsMapper backgroundsMapper;

    @Override
    public Long createBackgrounds(BackgroundsSaveReqVO createReqVO) {
        // 插入
        BackgroundsDO backgrounds = BeanUtils.toBean(createReqVO, BackgroundsDO.class);
        backgroundsMapper.insert(backgrounds);
        // 返回
        return backgrounds.getId();
    }

    @Override
    public void updateBackgrounds(BackgroundsSaveReqVO updateReqVO) {
        // 校验存在
        validateBackgroundsExists(updateReqVO.getId());
        // 更新
        BackgroundsDO updateObj = BeanUtils.toBean(updateReqVO, BackgroundsDO.class);
        backgroundsMapper.updateById(updateObj);
    }

    @Override
    public void deleteBackgrounds(Long id) {
        // 校验存在
        validateBackgroundsExists(id);
        // 删除
        backgroundsMapper.deleteById(id);
    }

    private void validateBackgroundsExists(Long id) {
        if (backgroundsMapper.selectById(id) == null) {
            throw exception(BACKGROUNDS_NOT_EXISTS);
        }
    }

    @Override
    public BackgroundsDO getBackgrounds(Long id) {
        return backgroundsMapper.selectById(id);
    }

    @Override
    public PageResult<BackgroundsDO> getBackgroundsPage(BackgroundsPageReqVO pageReqVO) {
        return backgroundsMapper.selectPage(pageReqVO);
    }

}