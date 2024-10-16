package cn.iocoder.yudao.module.digitalcourse.service.coursescenecomponents;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenecomponents.vo.AppCourseSceneComponentsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenecomponents.vo.AppCourseSceneComponentsSaveReqVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenecomponents.CourseSceneComponentsDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenecomponents.CourseSceneComponentsMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CourseSceneComponentsServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(CourseSceneComponentsServiceImpl.class)
public class CourseSceneComponentsServiceImplTest extends BaseDbUnitTest {

    @Resource
    private CourseSceneComponentsServiceImpl courseSceneComponentsService;

    @Resource
    private CourseSceneComponentsMapper courseSceneComponentsMapper;

    @Test
    public void testCreateCourseSceneComponents_success() {
        // 准备参数
        AppCourseSceneComponentsSaveReqVO createReqVO = randomPojo(AppCourseSceneComponentsSaveReqVO.class).setId(null);

        // 调用
        Long courseSceneComponentsId = courseSceneComponentsService.createCourseSceneComponents(createReqVO);
        // 断言
        assertNotNull(courseSceneComponentsId);
        // 校验记录的属性是否正确
        CourseSceneComponentsDO courseSceneComponents = courseSceneComponentsMapper.selectById(courseSceneComponentsId);
        assertPojoEquals(createReqVO, courseSceneComponents, "id");
    }

    @Test
    public void testUpdateCourseSceneComponents_success() {
        // mock 数据
        CourseSceneComponentsDO dbCourseSceneComponents = randomPojo(CourseSceneComponentsDO.class);
        courseSceneComponentsMapper.insert(dbCourseSceneComponents);// @Sql: 先插入出一条存在的数据
        // 准备参数
        AppCourseSceneComponentsSaveReqVO updateReqVO = randomPojo(AppCourseSceneComponentsSaveReqVO.class, o -> {
            o.setId(dbCourseSceneComponents.getId()); // 设置更新的 ID
        });

        // 调用
        courseSceneComponentsService.updateCourseSceneComponents(updateReqVO);
        // 校验是否更新正确
        CourseSceneComponentsDO courseSceneComponents = courseSceneComponentsMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, courseSceneComponents);
    }

    @Test
    public void testUpdateCourseSceneComponents_notExists() {
        // 准备参数
        AppCourseSceneComponentsSaveReqVO updateReqVO = randomPojo(AppCourseSceneComponentsSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> courseSceneComponentsService.updateCourseSceneComponents(updateReqVO), COURSE_SCENE_COMPONENTS_NOT_EXISTS);
    }

    @Test
    public void testDeleteCourseSceneComponents_success() {
        // mock 数据
        CourseSceneComponentsDO dbCourseSceneComponents = randomPojo(CourseSceneComponentsDO.class);
        courseSceneComponentsMapper.insert(dbCourseSceneComponents);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbCourseSceneComponents.getId();

        // 调用
        courseSceneComponentsService.deleteCourseSceneComponents(id);
       // 校验数据不存在了
       assertNull(courseSceneComponentsMapper.selectById(id));
    }

    @Test
    public void testDeleteCourseSceneComponents_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> courseSceneComponentsService.deleteCourseSceneComponents(id), COURSE_SCENE_COMPONENTS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCourseSceneComponentsPage() {
       // mock 数据
       CourseSceneComponentsDO dbCourseSceneComponents = randomPojo(CourseSceneComponentsDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setSrc(null);
           o.setCover(null);
           o.setWidth(null);
           o.setHeight(null);
           o.setOriginWidth(null);
           o.setOriginHeight(null);
           o.setCategory(null);
           o.setDepth(null);
           o.setTop(null);
           o.setMarginLeft(null);
           o.setEntityId(null);
           o.setEntityType(null);
           o.setBusinessId(null);
           o.setCreateTime(null);
           o.setStatus(null);
       });
       courseSceneComponentsMapper.insert(dbCourseSceneComponents);
       // 测试 name 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setName(null)));
       // 测试 src 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setSrc(null)));
       // 测试 cover 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setCover(null)));
       // 测试 width 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setWidth(null)));
       // 测试 height 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setHeight(null)));
       // 测试 originWidth 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setOriginWidth(null)));
       // 测试 originHeight 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setOriginHeight(null)));
       // 测试 category 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setCategory(null)));
       // 测试 depth 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setDepth(null)));
       // 测试 top 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setTop(null)));
       // 测试 left 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setMarginLeft(null)));
       // 测试 entityId 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setEntityId(null)));
       // 测试 entityType 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setEntityType(null)));
       // 测试 businessId 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setBusinessId(null)));
       // 测试 createTime 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setCreateTime(null)));
       // 测试 status 不匹配
       courseSceneComponentsMapper.insert(cloneIgnoreId(dbCourseSceneComponents, o -> o.setStatus(null)));
       // 准备参数
       AppCourseSceneComponentsPageReqVO reqVO = new AppCourseSceneComponentsPageReqVO();
       reqVO.setName(null);
       reqVO.setSrc(null);
       reqVO.setCover(null);
       reqVO.setWidth(null);
       reqVO.setHeight(null);
       reqVO.setOriginWidth(null);
       reqVO.setOriginHeight(null);
       reqVO.setCategory(null);
       reqVO.setDepth(null);
       reqVO.setTop(null);
       reqVO.setMarginLeft(null);
       reqVO.setEntityId(null);
       reqVO.setEntityType(null);
       reqVO.setBusinessId(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setStatus(null);

       // 调用
       PageResult<CourseSceneComponentsDO> pageResult = courseSceneComponentsService.getCourseSceneComponentsPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbCourseSceneComponents, pageResult.getList().get(0));
    }

}