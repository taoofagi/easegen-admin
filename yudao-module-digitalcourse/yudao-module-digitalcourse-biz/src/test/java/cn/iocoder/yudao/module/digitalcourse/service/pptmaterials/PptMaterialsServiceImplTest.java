package cn.iocoder.yudao.module.digitalcourse.service.pptmaterials;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials.vo.AppPptMaterialsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.pptmaterials.vo.AppPptMaterialsSaveReqVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.pptmaterials.PptMaterialsDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.pptmaterials.PptMaterialsMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link PptMaterialsServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(PptMaterialsServiceImpl.class)
public class PptMaterialsServiceImplTest extends BaseDbUnitTest {

    @Resource
    private PptMaterialsServiceImpl pptMaterialsService;

    @Resource
    private PptMaterialsMapper pptMaterialsMapper;

    @Test
    public void testCreatePptMaterials_success() {
        // 准备参数
        AppPptMaterialsSaveReqVO createReqVO = randomPojo(AppPptMaterialsSaveReqVO.class).setId(null);

        // 调用
        Long pptMaterialsId = pptMaterialsService.createPptMaterials(createReqVO);
        // 断言
        assertNotNull(pptMaterialsId);
        // 校验记录的属性是否正确
        PptMaterialsDO pptMaterials = pptMaterialsMapper.selectById(pptMaterialsId);
        assertPojoEquals(createReqVO, pptMaterials, "id");
    }

    @Test
    public void testUpdatePptMaterials_success() {
        // mock 数据
        PptMaterialsDO dbPptMaterials = randomPojo(PptMaterialsDO.class);
        pptMaterialsMapper.insert(dbPptMaterials);// @Sql: 先插入出一条存在的数据
        // 准备参数
        AppPptMaterialsSaveReqVO updateReqVO = randomPojo(AppPptMaterialsSaveReqVO.class, o -> {
            o.setId(dbPptMaterials.getId()); // 设置更新的 ID
        });

        // 调用
        pptMaterialsService.updatePptMaterials(updateReqVO);
        // 校验是否更新正确
        PptMaterialsDO pptMaterials = pptMaterialsMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, pptMaterials);
    }

    @Test
    public void testUpdatePptMaterials_notExists() {
        // 准备参数
        AppPptMaterialsSaveReqVO updateReqVO = randomPojo(AppPptMaterialsSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> pptMaterialsService.updatePptMaterials(updateReqVO), PPT_MATERIALS_NOT_EXISTS);
    }

    @Test
    public void testDeletePptMaterials_success() {
        // mock 数据
        PptMaterialsDO dbPptMaterials = randomPojo(PptMaterialsDO.class);
        pptMaterialsMapper.insert(dbPptMaterials);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbPptMaterials.getId();

        // 调用
        pptMaterialsService.deletePptMaterials(id);
       // 校验数据不存在了
       assertNull(pptMaterialsMapper.selectById(id));
    }

    @Test
    public void testDeletePptMaterials_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> pptMaterialsService.deletePptMaterials(id), PPT_MATERIALS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetPptMaterialsPage() {
       // mock 数据
       PptMaterialsDO dbPptMaterials = randomPojo(PptMaterialsDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setBackgroundType(null);
           o.setPictureUrl(null);
           o.setOriginalUrl(null);
           o.setWidth(null);
           o.setHeight(null);
           o.setIndexNo(null);
           o.setCreateTime(null);
           o.setStatus(null);
       });
       pptMaterialsMapper.insert(dbPptMaterials);
       // 测试 name 不匹配
       pptMaterialsMapper.insert(cloneIgnoreId(dbPptMaterials, o -> o.setName(null)));
       // 测试 backgroundType 不匹配
       pptMaterialsMapper.insert(cloneIgnoreId(dbPptMaterials, o -> o.setBackgroundType(null)));
       // 测试 pictureUrl 不匹配
       pptMaterialsMapper.insert(cloneIgnoreId(dbPptMaterials, o -> o.setPictureUrl(null)));
       // 测试 originalUrl 不匹配
       pptMaterialsMapper.insert(cloneIgnoreId(dbPptMaterials, o -> o.setOriginalUrl(null)));
       // 测试 width 不匹配
       pptMaterialsMapper.insert(cloneIgnoreId(dbPptMaterials, o -> o.setWidth(null)));
       // 测试 height 不匹配
       pptMaterialsMapper.insert(cloneIgnoreId(dbPptMaterials, o -> o.setHeight(null)));
       // 测试 indexNo 不匹配
       pptMaterialsMapper.insert(cloneIgnoreId(dbPptMaterials, o -> o.setIndexNo(null)));
       // 测试 createTime 不匹配
       pptMaterialsMapper.insert(cloneIgnoreId(dbPptMaterials, o -> o.setCreateTime(null)));
       // 测试 status 不匹配
       pptMaterialsMapper.insert(cloneIgnoreId(dbPptMaterials, o -> o.setStatus(null)));
       // 准备参数
       AppPptMaterialsPageReqVO reqVO = new AppPptMaterialsPageReqVO();
       reqVO.setName(null);
       reqVO.setBackgroundType(null);
       reqVO.setPictureUrl(null);
       reqVO.setOriginalUrl(null);
       reqVO.setWidth(null);
       reqVO.setHeight(null);
       reqVO.setIndexNo(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setStatus(null);

       // 调用
       PageResult<PptMaterialsDO> pageResult = pptMaterialsService.getPptMaterialsPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbPptMaterials, pageResult.getList().get(0));
    }

}