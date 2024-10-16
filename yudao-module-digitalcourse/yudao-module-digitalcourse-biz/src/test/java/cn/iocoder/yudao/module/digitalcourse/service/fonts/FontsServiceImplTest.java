package cn.iocoder.yudao.module.digitalcourse.service.fonts;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;


import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.fonts.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.fonts.FontsDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.fonts.FontsMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link FontsServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(FontsServiceImpl.class)
public class FontsServiceImplTest extends BaseDbUnitTest {

    @Resource
    private FontsServiceImpl fontsService;

    @Resource
    private FontsMapper fontsMapper;

    @Test
    public void testCreateFonts_success() {
        // 准备参数
        FontsSaveReqVO createReqVO = randomPojo(FontsSaveReqVO.class).setId(null);

        // 调用
        Long fontsId = fontsService.createFonts(createReqVO);
        // 断言
        assertNotNull(fontsId);
        // 校验记录的属性是否正确
        FontsDO fonts = fontsMapper.selectById(fontsId);
        assertPojoEquals(createReqVO, fonts, "id");
    }

    @Test
    public void testUpdateFonts_success() {
        // mock 数据
        FontsDO dbFonts = randomPojo(FontsDO.class);
        fontsMapper.insert(dbFonts);// @Sql: 先插入出一条存在的数据
        // 准备参数
        FontsSaveReqVO updateReqVO = randomPojo(FontsSaveReqVO.class, o -> {
            o.setId(dbFonts.getId()); // 设置更新的 ID
        });

        // 调用
        fontsService.updateFonts(updateReqVO);
        // 校验是否更新正确
        FontsDO fonts = fontsMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, fonts);
    }

    @Test
    public void testUpdateFonts_notExists() {
        // 准备参数
        FontsSaveReqVO updateReqVO = randomPojo(FontsSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> fontsService.updateFonts(updateReqVO), FONTS_NOT_EXISTS);
    }

    @Test
    public void testDeleteFonts_success() {
        // mock 数据
        FontsDO dbFonts = randomPojo(FontsDO.class);
        fontsMapper.insert(dbFonts);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbFonts.getId();

        // 调用
        fontsService.deleteFonts(id);
       // 校验数据不存在了
       assertNull(fontsMapper.selectById(id));
    }

    @Test
    public void testDeleteFonts_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> fontsService.deleteFonts(id), FONTS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetFontsPage() {
       // mock 数据
       FontsDO dbFonts = randomPojo(FontsDO.class, o -> { // 等会查询到
           o.setAlias(null);
           o.setName(null);
           o.setChoicePreviewUrl(null);
           o.setViewPreviewUrl(null);
           o.setOrderNo(null);
           o.setCreateTime(null);
           o.setStatus(null);
       });
       fontsMapper.insert(dbFonts);
       // 测试 alias 不匹配
       fontsMapper.insert(cloneIgnoreId(dbFonts, o -> o.setAlias(null)));
       // 测试 name 不匹配
       fontsMapper.insert(cloneIgnoreId(dbFonts, o -> o.setName(null)));
       // 测试 choicePreviewUrl 不匹配
       fontsMapper.insert(cloneIgnoreId(dbFonts, o -> o.setChoicePreviewUrl(null)));
       // 测试 viewPreviewUrl 不匹配
       fontsMapper.insert(cloneIgnoreId(dbFonts, o -> o.setViewPreviewUrl(null)));
       // 测试 orderNo 不匹配
       fontsMapper.insert(cloneIgnoreId(dbFonts, o -> o.setOrderNo(null)));
       // 测试 createTime 不匹配
       fontsMapper.insert(cloneIgnoreId(dbFonts, o -> o.setCreateTime(null)));
       // 测试 status 不匹配
       fontsMapper.insert(cloneIgnoreId(dbFonts, o -> o.setStatus(null)));
       // 准备参数
       FontsPageReqVO reqVO = new FontsPageReqVO();
       reqVO.setAlias(null);
       reqVO.setName(null);
       reqVO.setChoicePreviewUrl(null);
       reqVO.setViewPreviewUrl(null);
       reqVO.setOrderNo(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setStatus(null);

       // 调用
       PageResult<FontsDO> pageResult = fontsService.getFontsPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbFonts, pageResult.getList().get(0));
    }

}