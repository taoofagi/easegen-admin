package cn.iocoder.yudao.module.digitalcourse.service.backgrounds;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.backgrounds.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.backgrounds.BackgroundsDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.backgrounds.BackgroundsMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import jakarta.annotation.Resource;
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
 * {@link BackgroundsServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(BackgroundsServiceImpl.class)
public class BackgroundsServiceImplTest extends BaseDbUnitTest {

    @Resource
    private BackgroundsServiceImpl backgroundsService;

    @Resource
    private BackgroundsMapper backgroundsMapper;

    @Test
    public void testCreateBackgrounds_success() {
        // 准备参数
        BackgroundsSaveReqVO createReqVO = randomPojo(BackgroundsSaveReqVO.class).setId(null);

        // 调用
        Long backgroundsId = backgroundsService.createBackgrounds(createReqVO);
        // 断言
        assertNotNull(backgroundsId);
        // 校验记录的属性是否正确
        BackgroundsDO backgrounds = backgroundsMapper.selectById(backgroundsId);
        assertPojoEquals(createReqVO, backgrounds, "id");
    }

    @Test
    public void testUpdateBackgrounds_success() {
        // mock 数据
        BackgroundsDO dbBackgrounds = randomPojo(BackgroundsDO.class);
        backgroundsMapper.insert(dbBackgrounds);// @Sql: 先插入出一条存在的数据
        // 准备参数
        BackgroundsSaveReqVO updateReqVO = randomPojo(BackgroundsSaveReqVO.class, o -> {
            o.setId(dbBackgrounds.getId()); // 设置更新的 ID
        });

        // 调用
        backgroundsService.updateBackgrounds(updateReqVO);
        // 校验是否更新正确
        BackgroundsDO backgrounds = backgroundsMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, backgrounds);
    }

    @Test
    public void testUpdateBackgrounds_notExists() {
        // 准备参数
        BackgroundsSaveReqVO updateReqVO = randomPojo(BackgroundsSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> backgroundsService.updateBackgrounds(updateReqVO), BACKGROUNDS_NOT_EXISTS);
    }

    @Test
    public void testDeleteBackgrounds_success() {
        // mock 数据
        BackgroundsDO dbBackgrounds = randomPojo(BackgroundsDO.class);
        backgroundsMapper.insert(dbBackgrounds);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbBackgrounds.getId();

        // 调用
        backgroundsService.deleteBackgrounds(id);
       // 校验数据不存在了
       assertNull(backgroundsMapper.selectById(id));
    }

    @Test
    public void testDeleteBackgrounds_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> backgroundsService.deleteBackgrounds(id), BACKGROUNDS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetBackgroundsPage() {
       // mock 数据
       BackgroundsDO dbBackgrounds = randomPojo(BackgroundsDO.class, o -> { // 等会查询到
           o.setBackgroundType(null);
           o.setName(null);
           o.setPreset(null);
           o.setStatus(null);
           o.setCreateTime(null);
       });
       backgroundsMapper.insert(dbBackgrounds);
       // 测试 backgroundType 不匹配
       backgroundsMapper.insert(cloneIgnoreId(dbBackgrounds, o -> o.setBackgroundType(null)));
       // 测试 name 不匹配
       backgroundsMapper.insert(cloneIgnoreId(dbBackgrounds, o -> o.setName(null)));
       // 测试 preset 不匹配
       backgroundsMapper.insert(cloneIgnoreId(dbBackgrounds, o -> o.setPreset(null)));
       // 测试 status 不匹配
       backgroundsMapper.insert(cloneIgnoreId(dbBackgrounds, o -> o.setStatus(null)));
       // 测试 createTime 不匹配
       backgroundsMapper.insert(cloneIgnoreId(dbBackgrounds, o -> o.setCreateTime(null)));
       // 准备参数
       BackgroundsPageReqVO reqVO = new BackgroundsPageReqVO();
       reqVO.setBackgroundType(null);
       reqVO.setName(null);
       reqVO.setPreset(null);
       reqVO.setStatus(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<BackgroundsDO> pageResult = backgroundsService.getBackgroundsPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbBackgrounds, pageResult.getList().get(0));
    }

}