package cn.iocoder.yudao.module.digitalcourse.service.fonts;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.fonts.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.fonts.FontsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.fonts.FontsMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;

/**
 * 存储字体的信息，包括字体的别名、预览URL、名称等 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class FontsServiceImpl implements FontsService {

    @Resource
    private FontsMapper fontsMapper;

    @Override
    public Long createFonts(FontsSaveReqVO createReqVO) {
        // 插入
        FontsDO fonts = BeanUtils.toBean(createReqVO, FontsDO.class);
        fontsMapper.insert(fonts);
        // 返回
        return fonts.getId();
    }

    @Override
    public void updateFonts(FontsSaveReqVO updateReqVO) {
        // 校验存在
        validateFontsExists(updateReqVO.getId());
        // 更新
        FontsDO updateObj = BeanUtils.toBean(updateReqVO, FontsDO.class);
        fontsMapper.updateById(updateObj);
    }

    @Override
    public void deleteFonts(Long id) {
        // 校验存在
        validateFontsExists(id);
        // 删除
        fontsMapper.deleteById(id);
    }

    private void validateFontsExists(Long id) {
        if (fontsMapper.selectById(id) == null) {
            throw exception(FONTS_NOT_EXISTS);
        }
    }

    @Override
    public FontsDO getFonts(Long id) {
        return fontsMapper.selectById(id);
    }

    @Override
    public PageResult<FontsDO> getFontsPage(FontsPageReqVO pageReqVO) {
        return fontsMapper.selectPage(pageReqVO);
    }

}