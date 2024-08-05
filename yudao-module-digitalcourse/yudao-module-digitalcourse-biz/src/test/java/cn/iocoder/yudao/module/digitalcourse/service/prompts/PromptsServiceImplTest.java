package cn.iocoder.yudao.module.digitalcourse.service.prompts;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;


import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.prompts.vo.*;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.prompts.PromptsDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.prompts.PromptsMapper;
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
 * {@link PromptsServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(PromptsServiceImpl.class)
public class PromptsServiceImplTest extends BaseDbUnitTest {

    @Resource
    private PromptsServiceImpl promptsService;

    @Resource
    private PromptsMapper promptsMapper;

    @Test
    public void testCreatePrompts_success() {
        // 准备参数
        PromptsSaveReqVO createReqVO = randomPojo(PromptsSaveReqVO.class).setId(null);

        // 调用
        Long promptsId = promptsService.createPrompts(createReqVO);
        // 断言
        assertNotNull(promptsId);
        // 校验记录的属性是否正确
        PromptsDO prompts = promptsMapper.selectById(promptsId);
        assertPojoEquals(createReqVO, prompts, "id");
    }

    @Test
    public void testUpdatePrompts_success() {
        // mock 数据
        PromptsDO dbPrompts = randomPojo(PromptsDO.class);
        promptsMapper.insert(dbPrompts);// @Sql: 先插入出一条存在的数据
        // 准备参数
        PromptsSaveReqVO updateReqVO = randomPojo(PromptsSaveReqVO.class, o -> {
            o.setId(dbPrompts.getId()); // 设置更新的 ID
        });

        // 调用
        promptsService.updatePrompts(updateReqVO);
        // 校验是否更新正确
        PromptsDO prompts = promptsMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, prompts);
    }

    @Test
    public void testUpdatePrompts_notExists() {
        // 准备参数
        PromptsSaveReqVO updateReqVO = randomPojo(PromptsSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> promptsService.updatePrompts(updateReqVO), PROMPTS_NOT_EXISTS);
    }

    @Test
    public void testDeletePrompts_success() {
        // mock 数据
        PromptsDO dbPrompts = randomPojo(PromptsDO.class);
        promptsMapper.insert(dbPrompts);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbPrompts.getId();

        // 调用
        promptsService.deletePrompts(id);
       // 校验数据不存在了
       assertNull(promptsMapper.selectById(id));
    }

    @Test
    public void testDeletePrompts_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> promptsService.deletePrompts(id), PROMPTS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetPromptsPage() {
       // mock 数据
       PromptsDO dbPrompts = randomPojo(PromptsDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setContent(null);
           o.setOrder(null);
           o.setType(null);
           o.setCreateTime(null);
           o.setStatus(null);
       });
       promptsMapper.insert(dbPrompts);
       // 测试 name 不匹配
       promptsMapper.insert(cloneIgnoreId(dbPrompts, o -> o.setName(null)));
       // 测试 content 不匹配
       promptsMapper.insert(cloneIgnoreId(dbPrompts, o -> o.setContent(null)));
       // 测试 order 不匹配
       promptsMapper.insert(cloneIgnoreId(dbPrompts, o -> o.setOrder(null)));
       // 测试 type 不匹配
       promptsMapper.insert(cloneIgnoreId(dbPrompts, o -> o.setType(null)));
       // 测试 createTime 不匹配
       promptsMapper.insert(cloneIgnoreId(dbPrompts, o -> o.setCreateTime(null)));
       // 测试 status 不匹配
       promptsMapper.insert(cloneIgnoreId(dbPrompts, o -> o.setStatus(null)));
       // 准备参数
       PromptsPageReqVO reqVO = new PromptsPageReqVO();
       reqVO.setName(null);
       reqVO.setContent(null);
       reqVO.setOrder(null);
       reqVO.setType(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setStatus(null);

       // 调用
       PageResult<PromptsDO> pageResult = promptsService.getPromptsPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbPrompts, pageResult.getList().get(0));
    }

}