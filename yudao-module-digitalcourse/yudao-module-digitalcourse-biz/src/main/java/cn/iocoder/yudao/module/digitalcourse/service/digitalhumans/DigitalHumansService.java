package cn.iocoder.yudao.module.digitalcourse.service.digitalhumans;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.digitalhumans.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.digitalhumans.DigitalHumansDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 数字人模型 Service 接口
 *
 * @author 芋道源码
 */
public interface DigitalHumansService {

    /**
     * 创建数字人模型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDigitalHumans(@Valid DigitalHumansSaveReqVO createReqVO);

    /**
     * 更新数字人模型
     *
     * @param updateReqVO 更新信息
     */
    void updateDigitalHumans(@Valid DigitalHumansSaveReqVO updateReqVO);

    /**
     * 删除数字人模型
     *
     * @param id 编号
     */
    void deleteDigitalHumans(Long id);

    /**
     * 获得数字人模型
     *
     * @param id 编号
     * @return 数字人模型
     */
    DigitalHumansDO getDigitalHumans(Long id);

    /**
     * 获得数字人模型分页
     *
     * @param pageReqVO 分页查询
     * @return 数字人模型分页
     */
    PageResult<DigitalHumansDO> getDigitalHumansPage(DigitalHumansPageReqVO pageReqVO);

}