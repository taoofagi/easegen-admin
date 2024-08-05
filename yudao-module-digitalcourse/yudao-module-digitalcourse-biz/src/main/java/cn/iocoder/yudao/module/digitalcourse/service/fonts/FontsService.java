package cn.iocoder.yudao.module.digitalcourse.service.fonts;

import java.util.*;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.fonts.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.fonts.FontsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import jakarta.validation.Valid;

/**
 * 存储字体的信息，包括字体的别名、预览URL、名称等 Service 接口
 *
 * @author 芋道源码
 */
public interface FontsService {

    /**
     * 创建存储字体的信息，包括字体的别名、预览URL、名称等
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFonts(@Valid FontsSaveReqVO createReqVO);

    /**
     * 更新存储字体的信息，包括字体的别名、预览URL、名称等
     *
     * @param updateReqVO 更新信息
     */
    void updateFonts(@Valid FontsSaveReqVO updateReqVO);

    /**
     * 删除存储字体的信息，包括字体的别名、预览URL、名称等
     *
     * @param id 编号
     */
    void deleteFonts(Long id);

    /**
     * 获得存储字体的信息，包括字体的别名、预览URL、名称等
     *
     * @param id 编号
     * @return 存储字体的信息，包括字体的别名、预览URL、名称等
     */
    FontsDO getFonts(Long id);

    /**
     * 获得存储字体的信息，包括字体的别名、预览URL、名称等分页
     *
     * @param pageReqVO 分页查询
     * @return 存储字体的信息，包括字体的别名、预览URL、名称等分页
     */
    PageResult<FontsDO> getFontsPage(FontsPageReqVO pageReqVO);

}