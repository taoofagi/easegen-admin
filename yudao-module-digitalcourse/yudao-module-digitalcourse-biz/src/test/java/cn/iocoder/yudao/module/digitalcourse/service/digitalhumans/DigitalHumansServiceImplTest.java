package cn.iocoder.yudao.module.digitalcourse.service.digitalhumans;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.digitalhumans.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.digitalhumans.DigitalHumansDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.digitalhumans.DigitalHumansMapper;
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
 * {@link DigitalHumansServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(DigitalHumansServiceImpl.class)
public class DigitalHumansServiceImplTest extends BaseDbUnitTest {

    @Resource
    private DigitalHumansServiceImpl digitalHumansService;

    @Resource
    private DigitalHumansMapper digitalHumansMapper;

    @Test
    public void testCreateDigitalHumans_success() {
        // 准备参数
        DigitalHumansSaveReqVO createReqVO = randomPojo(DigitalHumansSaveReqVO.class).setId(null);

        // 调用
        Long digitalHumansId = digitalHumansService.createDigitalHumans(createReqVO);
        // 断言
        assertNotNull(digitalHumansId);
        // 校验记录的属性是否正确
        DigitalHumansDO digitalHumans = digitalHumansMapper.selectById(digitalHumansId);
        assertPojoEquals(createReqVO, digitalHumans, "id");
    }

    @Test
    public void testUpdateDigitalHumans_success() {
        // mock 数据
        DigitalHumansDO dbDigitalHumans = randomPojo(DigitalHumansDO.class);
        digitalHumansMapper.insert(dbDigitalHumans);// @Sql: 先插入出一条存在的数据
        // 准备参数
        DigitalHumansSaveReqVO updateReqVO = randomPojo(DigitalHumansSaveReqVO.class, o -> {
            o.setId(dbDigitalHumans.getId()); // 设置更新的 ID
        });

        // 调用
        digitalHumansService.updateDigitalHumans(updateReqVO);
        // 校验是否更新正确
        DigitalHumansDO digitalHumans = digitalHumansMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, digitalHumans);
    }

    @Test
    public void testUpdateDigitalHumans_notExists() {
        // 准备参数
        DigitalHumansSaveReqVO updateReqVO = randomPojo(DigitalHumansSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> digitalHumansService.updateDigitalHumans(updateReqVO), DIGITAL_HUMANS_NOT_EXISTS);
    }

    @Test
    public void testDeleteDigitalHumans_success() {
        // mock 数据
        DigitalHumansDO dbDigitalHumans = randomPojo(DigitalHumansDO.class);
        digitalHumansMapper.insert(dbDigitalHumans);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbDigitalHumans.getId();

        // 调用
        digitalHumansService.deleteDigitalHumans(id);
       // 校验数据不存在了
       assertNull(digitalHumansMapper.selectById(id));
    }

    @Test
    public void testDeleteDigitalHumans_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> digitalHumansService.deleteDigitalHumans(id), DIGITAL_HUMANS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetDigitalHumansPage() {
       // mock 数据
       DigitalHumansDO dbDigitalHumans = randomPojo(DigitalHumansDO.class, o -> { // 等会查询到
           o.setGender(null);
           o.setName(null);
           o.setPosture(null);
           o.setType(null);
           o.setUseModel(null);
           o.setStatus(null);
       });
       digitalHumansMapper.insert(dbDigitalHumans);
       // 测试 gender 不匹配
       digitalHumansMapper.insert(cloneIgnoreId(dbDigitalHumans, o -> o.setGender(null)));
       // 测试 name 不匹配
       digitalHumansMapper.insert(cloneIgnoreId(dbDigitalHumans, o -> o.setName(null)));
       // 测试 posture 不匹配
       digitalHumansMapper.insert(cloneIgnoreId(dbDigitalHumans, o -> o.setPosture(null)));
       // 测试 type 不匹配
       digitalHumansMapper.insert(cloneIgnoreId(dbDigitalHumans, o -> o.setType(null)));
       // 测试 useModel 不匹配
       digitalHumansMapper.insert(cloneIgnoreId(dbDigitalHumans, o -> o.setUseModel(null)));
       // 测试 status 不匹配
       digitalHumansMapper.insert(cloneIgnoreId(dbDigitalHumans, o -> o.setStatus(null)));
       // 准备参数
       DigitalHumansPageReqVO reqVO = new DigitalHumansPageReqVO();
       reqVO.setGender(null);
       reqVO.setName(null);
       reqVO.setPosture(null);
       reqVO.setType(null);
       reqVO.setUseModel(null);
       reqVO.setStatus(null);

       // 调用
       PageResult<DigitalHumansDO> pageResult = digitalHumansService.getDigitalHumansPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbDigitalHumans, pageResult.getList().get(0));
    }

}