package cn.iocoder.yudao.module.digitalcourse.service.voices;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import jakarta.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.voices.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.voices.VoicesDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.voices.VoicesMapper;
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
 * {@link VoicesServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(VoicesServiceImpl.class)
public class VoicesServiceImplTest extends BaseDbUnitTest {

    @Resource
    private VoicesServiceImpl voicesService;

    @Resource
    private VoicesMapper voicesMapper;

    @Test
    public void testCreateVoices_success() {
        // 准备参数
        VoicesSaveReqVO createReqVO = randomPojo(VoicesSaveReqVO.class).setId(null);

        // 调用
        Long voicesId = voicesService.createVoices(createReqVO);
        // 断言
        assertNotNull(voicesId);
        // 校验记录的属性是否正确
        VoicesDO voices = voicesMapper.selectById(voicesId);
        assertPojoEquals(createReqVO, voices, "id");
    }

    @Test
    public void testUpdateVoices_success() {
        // mock 数据
        VoicesDO dbVoices = randomPojo(VoicesDO.class);
        voicesMapper.insert(dbVoices);// @Sql: 先插入出一条存在的数据
        // 准备参数
        VoicesSaveReqVO updateReqVO = randomPojo(VoicesSaveReqVO.class, o -> {
            o.setId(dbVoices.getId()); // 设置更新的 ID
        });

        // 调用
        voicesService.updateVoices(updateReqVO);
        // 校验是否更新正确
        VoicesDO voices = voicesMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, voices);
    }

    @Test
    public void testUpdateVoices_notExists() {
        // 准备参数
        VoicesSaveReqVO updateReqVO = randomPojo(VoicesSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> voicesService.updateVoices(updateReqVO), VOICES_NOT_EXISTS);
    }

    @Test
    public void testDeleteVoices_success() {
        // mock 数据
        VoicesDO dbVoices = randomPojo(VoicesDO.class);
        voicesMapper.insert(dbVoices);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbVoices.getId();

        // 调用
        voicesService.deleteVoices(id);
       // 校验数据不存在了
       assertNull(voicesMapper.selectById(id));
    }

    @Test
    public void testDeleteVoices_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> voicesService.deleteVoices(id), VOICES_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetVoicesPage() {
       // mock 数据
       VoicesDO dbVoices = randomPojo(VoicesDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setLanguage(null);
           o.setGender(null);
           o.setQuality(null);
           o.setVoiceType(null);
           o.setCreateTime(null);
           o.setStatus(null);
       });
       voicesMapper.insert(dbVoices);
       // 测试 name 不匹配
       voicesMapper.insert(cloneIgnoreId(dbVoices, o -> o.setName(null)));
       // 测试 language 不匹配
       voicesMapper.insert(cloneIgnoreId(dbVoices, o -> o.setLanguage(null)));
       // 测试 gender 不匹配
       voicesMapper.insert(cloneIgnoreId(dbVoices, o -> o.setGender(null)));
       // 测试 quality 不匹配
       voicesMapper.insert(cloneIgnoreId(dbVoices, o -> o.setQuality(null)));
       // 测试 voiceType 不匹配
       voicesMapper.insert(cloneIgnoreId(dbVoices, o -> o.setVoiceType(null)));
       // 测试 createTime 不匹配
       voicesMapper.insert(cloneIgnoreId(dbVoices, o -> o.setCreateTime(null)));
       // 测试 status 不匹配
       voicesMapper.insert(cloneIgnoreId(dbVoices, o -> o.setStatus(null)));
       // 准备参数
       VoicesPageReqVO reqVO = new VoicesPageReqVO();
       reqVO.setName(null);
       reqVO.setLanguage(null);
       reqVO.setGender(null);
       reqVO.setQuality(null);
       reqVO.setVoiceType(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setStatus(null);

       // 调用
       PageResult<VoicesDO> pageResult = voicesService.getVoicesPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbVoices, pageResult.getList().get(0));
    }

}