package cn.iocoder.yudao.module.digitalcourse.service.pptmaterials;


import cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials.vo.AppPptMaterialsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials.vo.AppPptMaterialsSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.pptmaterials.PptMaterialsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等 Service 接口
 *
 * @author 芋道源码
 */
public interface PptMaterialsService {

    /**
     * 创建存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPptMaterials(@Valid AppPptMaterialsSaveReqVO createReqVO);

    /**
     * 更新存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等
     *
     * @param updateReqVO 更新信息
     */
    void updatePptMaterials(@Valid AppPptMaterialsSaveReqVO updateReqVO);

    /**
     * 删除存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等
     *
     * @param id 编号
     */
    void deletePptMaterials(Long id);

    /**
     * 获得存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等
     *
     * @param id 编号
     * @return 存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等
     */
    PptMaterialsDO getPptMaterials(Long id);

    /**
     * 获得存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等分页
     *
     * @param pageReqVO 分页查询
     * @return 存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等分页
     */
    PageResult<PptMaterialsDO> getPptMaterialsPage(AppPptMaterialsPageReqVO pageReqVO);


    Boolean batchInsert(List<PptMaterialsDO> list);


    List<AppPptMaterialsSaveReqVO> selectListByPptId(Long pptId);

}