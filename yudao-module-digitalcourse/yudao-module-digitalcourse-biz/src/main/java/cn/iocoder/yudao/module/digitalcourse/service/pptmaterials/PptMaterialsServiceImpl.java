package cn.iocoder.yudao.module.digitalcourse.service.pptmaterials;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials.vo.AppPptMaterialsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials.vo.AppPptMaterialsSaveReqVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.pptmaterials.PptMaterialsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.pptmaterials.PptMaterialsMapper;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 存储PPT课件的具体内容信息，包括课件的图片URL、页面索引等 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class PptMaterialsServiceImpl implements PptMaterialsService {

    @Resource
    private PptMaterialsMapper pptMaterialsMapper;

    @Override
    public Long createPptMaterials(AppPptMaterialsSaveReqVO createReqVO) {
        // 插入
        PptMaterialsDO pptMaterials = BeanUtils.toBean(createReqVO, PptMaterialsDO.class);
        pptMaterialsMapper.insert(pptMaterials);
        // 返回
        return pptMaterials.getId();
    }

    @Override
    public void updatePptMaterials(AppPptMaterialsSaveReqVO updateReqVO) {
        // 校验存在
        validatePptMaterialsExists(updateReqVO.getId());
        // 更新
        PptMaterialsDO updateObj = BeanUtils.toBean(updateReqVO, PptMaterialsDO.class);
        pptMaterialsMapper.updateById(updateObj);
    }

    @Override
    public void deletePptMaterials(Long id) {
        // 校验存在
        validatePptMaterialsExists(id);
        // 删除
        pptMaterialsMapper.deleteById(id);
    }

    private void validatePptMaterialsExists(Long id) {
        if (pptMaterialsMapper.selectById(id) == null) {
            throw exception(PPT_MATERIALS_NOT_EXISTS);
        }
    }

    @Override
    public PptMaterialsDO getPptMaterials(Long id) {
        return pptMaterialsMapper.selectById(id);
    }

    @Override
    public PageResult<PptMaterialsDO> getPptMaterialsPage(AppPptMaterialsPageReqVO pageReqVO) {
        return pptMaterialsMapper.selectPage(pageReqVO);
    }

    @Override
    public Boolean batchInsert(List<PptMaterialsDO> list) {
        return pptMaterialsMapper.insertBatch(list);
    }

    @Override
    public List<AppPptMaterialsSaveReqVO> selectListByPptId(Long pptId) {
        List<PptMaterialsDO> pptMaterialsDOS = pptMaterialsMapper.selectList(new QueryWrapper<PptMaterialsDO>().eq("ppt_id", pptId));
        List<AppPptMaterialsSaveReqVO> bean = BeanUtils.toBean(pptMaterialsDOS, AppPptMaterialsSaveReqVO.class);
        return bean;
    }

}