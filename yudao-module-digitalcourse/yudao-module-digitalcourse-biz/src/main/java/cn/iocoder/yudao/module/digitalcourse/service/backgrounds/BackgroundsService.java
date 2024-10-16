package cn.iocoder.yudao.module.digitalcourse.service.backgrounds;

import java.util.*;
import jakarta.validation.*;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.backgrounds.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.backgrounds.BackgroundsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 背景信息（PPT背景、板书、插图、字幕等） Service 接口
 *
 * @author 芋道源码
 */
public interface BackgroundsService {

    /**
     * 创建背景信息（PPT背景、板书、插图、字幕等）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBackgrounds(@Valid BackgroundsSaveReqVO createReqVO);

    /**
     * 更新背景信息（PPT背景、板书、插图、字幕等）
     *
     * @param updateReqVO 更新信息
     */
    void updateBackgrounds(@Valid BackgroundsSaveReqVO updateReqVO);

    /**
     * 删除背景信息（PPT背景、板书、插图、字幕等）
     *
     * @param id 编号
     */
    void deleteBackgrounds(Long id);

    /**
     * 获得背景信息（PPT背景、板书、插图、字幕等）
     *
     * @param id 编号
     * @return 背景信息（PPT背景、板书、插图、字幕等）
     */
    BackgroundsDO getBackgrounds(Long id);

    /**
     * 获得背景信息（PPT背景、板书、插图、字幕等）分页
     *
     * @param pageReqVO 分页查询
     * @return 背景信息（PPT背景、板书、插图、字幕等）分页
     */
    PageResult<BackgroundsDO> getBackgroundsPage(BackgroundsPageReqVO pageReqVO);

}