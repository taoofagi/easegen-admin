package cn.iocoder.yudao.module.member.service.workcenter;

import java.io.IOException;
import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.member.controller.admin.workcenter.vo.*;
import cn.iocoder.yudao.module.member.dal.dataobject.workcenter.WorkCenterDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 作品中心 Service 接口
 *
 * @author 管理员
 */
public interface WorkCenterService {

    /**
     * 创建作品中心
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWorkCenter(@Valid WorkCenterSaveReqVO createReqVO);
    Map uploadWorkCenter(MultipartFile file) throws IOException;

    /**
     * 更新作品中心
     *
     * @param updateReqVO 更新信息
     */
    void updateWorkCenter(@Valid WorkCenterSaveReqVO updateReqVO);

    /**
     * 删除作品中心
     *
     * @param id 编号
     */
    void deleteWorkCenter(Long id);

    /**
     * 获得作品中心
     *
     * @param id 编号
     * @return 作品中心
     */
    WorkCenterDO getWorkCenter(Long id);

    /**
     * 获得作品中心分页
     *
     * @param pageReqVO 分页查询
     * @return 作品中心分页
     */
    PageResult<WorkCenterDO> getWorkCenterPage(WorkCenterPageReqVO pageReqVO);

}