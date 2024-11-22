package cn.iocoder.yudao.module.digitalcourse.service.voices;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.voices.AuditionDO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.voices.TTSDTO;
import jakarta.validation.*;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.voices.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.voices.VoicesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 声音管理 Service 接口
 *
 * @author 芋道源码
 */
public interface VoicesService {

    /**
     * 创建声音管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createVoices(@Valid VoicesSaveReqVO createReqVO);

    /**
     * 更新声音管理
     *
     * @param updateReqVO 更新信息
     */
    void updateVoices(@Valid VoicesSaveReqVO updateReqVO);

    /**
     * 删除声音管理
     *
     * @param id 编号
     */
    void deleteVoices(Long id);

    /**
     * 获得声音管理
     *
     * @param id 编号
     * @return 声音管理
     */
    VoicesDO getVoices(Long id);

    /**
     * 获得声音管理分页
     *
     * @param pageReqVO 分页查询
     * @return 声音管理分页
     */
    PageResult<VoicesDO> getVoicesPage(VoicesPageReqVO pageReqVO);
    PageResult<VoicesDO> getVoicesCommonPage(VoicesPageReqVO pageReqVO);

    String audition(AuditionDO auditionDO);

    Boolean auditing();
}