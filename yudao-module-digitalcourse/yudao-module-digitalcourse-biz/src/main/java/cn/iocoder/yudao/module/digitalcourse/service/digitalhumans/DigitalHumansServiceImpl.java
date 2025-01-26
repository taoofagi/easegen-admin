package cn.iocoder.yudao.module.digitalcourse.service.digitalhumans;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.digitalhumans.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.digitalhumans.DigitalHumansDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
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

    @Resource
    private DigitalHumansServiceUtil digitalHumansServiceUtil;

    @Override
    public Long createDigitalHumans(DigitalHumansSaveReqVO createReqVO) {
        createReqVO.setCode(UUID.fastUUID().toString());
        // 插入
        DigitalHumansDO digitalHumans = BeanUtils.toBean(createReqVO, DigitalHumansDO.class);
        digitalHumansMapper.insert(digitalHumans);
        // 判断如果是极速模式，自动开始训练
        if (digitalHumans.getUseModel().equals("3")) {
            DigitalHumansSaveReqVO reqVO = BeanUtils.toBean(digitalHumans, DigitalHumansSaveReqVO.class);
            reqVO.setStatus(3);
            reqVO.setFixVideoUrl(digitalHumans.getVideoUrl());
            updateDigitalHumans(reqVO);
        }

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

        //异步训练模型
        if (updateObj.getStatus() == 3){
            digitalHumansServiceUtil.remoteTrain(transferVO(updateObj.getId()));
        }
    }



    private DigitalHumansTrailVO transferVO(Long id) {
        DigitalHumansDO digitalHumans = this.getDigitalHumans(id);
        DigitalHumansTrailVO build = DigitalHumansTrailVO.builder().build();
        if (StrUtil.isBlank(digitalHumans.getFixVideoUrl())) build.setFixVideoUrl(digitalHumans.getVideoUrl());
        if (StrUtil.isBlank(digitalHumans.getFixPictureUrl())) build.setFixPictureUrl(digitalHumans.getPictureUrl());
        BeanUtils.copyProperties(digitalHumans, build);
        build.setAccountId(digitalHumans.getCreator());
        return build;
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
    public Boolean auditing() {
        // 管理员直接返回true
        if (WebFrameworkUtils.getLoginUserId() == 1) return true;
        Integer auditing = digitalHumansMapper.auditing(WebFrameworkUtils.getLoginUserId());
        return auditing == null;
    }

    @Override
    public PageResult<DigitalHumansDO> getDigitalHumansCommonPage(DigitalHumansPageReqVO pageReqVO) {
        return digitalHumansMapper.selectPage(pageReqVO);
    }
    @Override
    public PageResult<DigitalHumansDO> getDigitalHumansPage(DigitalHumansPageReqVO pageReqVO) {
        if(pageReqVO.getType()==1){
            //查询非公共数字人，只能查询自己的，公共数字人，可以查询所有的
            if (WebFrameworkUtils.getLoginUserId() != 1) pageReqVO.setCreator(String.valueOf(WebFrameworkUtils.getLoginUserId()));
        }
        //只查询状态正常的数据
        pageReqVO.setStatus(0);
        return digitalHumansMapper.selectPage(pageReqVO);
    }

}