package cn.iocoder.yudao.module.digitalcourse.service.coursescenebackgrounds;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo.AppCourseSceneBackgroundsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenebackgrounds.vo.AppCourseSceneBackgroundsSaveReqVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenebackgrounds.CourseSceneBackgroundsDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenebackgrounds.CourseSceneBackgroundsMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CourseSceneBackgroundsServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(CourseSceneBackgroundsServiceImpl.class)
public class CourseSceneBackgroundsServiceImplTest extends BaseDbUnitTest {

    @Resource
    private CourseSceneBackgroundsServiceImpl courseSceneBackgroundsService;

    @Resource
    private CourseSceneBackgroundsMapper courseSceneBackgroundsMapper;

    @Test
    public void testCreateCourseSceneBackgrounds_success() {
        // 准备参数
        AppCourseSceneBackgroundsSaveReqVO createReqVO = randomPojo(AppCourseSceneBackgroundsSaveReqVO.class).setId(null);

        // 调用
        Long courseSceneBackgroundsId = courseSceneBackgroundsService.createCourseSceneBackgrounds(createReqVO);
        // 断言
        assertNotNull(courseSceneBackgroundsId);
        // 校验记录的属性是否正确
        CourseSceneBackgroundsDO courseSceneBackgrounds = courseSceneBackgroundsMapper.selectById(courseSceneBackgroundsId);
        assertPojoEquals(createReqVO, courseSceneBackgrounds, "id");
    }

    @Test
    public void testUpdateCourseSceneBackgrounds_success() {
        // mock 数据
        CourseSceneBackgroundsDO dbCourseSceneBackgrounds = randomPojo(CourseSceneBackgroundsDO.class);
        courseSceneBackgroundsMapper.insert(dbCourseSceneBackgrounds);// @Sql: 先插入出一条存在的数据
        // 准备参数
        AppCourseSceneBackgroundsSaveReqVO updateReqVO = randomPojo(AppCourseSceneBackgroundsSaveReqVO.class, o -> {
            o.setId(dbCourseSceneBackgrounds.getId()); // 设置更新的 ID
        });

        // 调用
        courseSceneBackgroundsService.updateCourseSceneBackgrounds(updateReqVO);
        // 校验是否更新正确
        CourseSceneBackgroundsDO courseSceneBackgrounds = courseSceneBackgroundsMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, courseSceneBackgrounds);
    }

    @Test
    public void testUpdateCourseSceneBackgrounds_notExists() {
        // 准备参数
        AppCourseSceneBackgroundsSaveReqVO updateReqVO = randomPojo(AppCourseSceneBackgroundsSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> courseSceneBackgroundsService.updateCourseSceneBackgrounds(updateReqVO), COURSE_SCENE_BACKGROUNDS_NOT_EXISTS);
    }

    @Test
    public void testDeleteCourseSceneBackgrounds_success() {
        // mock 数据
        CourseSceneBackgroundsDO dbCourseSceneBackgrounds = randomPojo(CourseSceneBackgroundsDO.class);
        courseSceneBackgroundsMapper.insert(dbCourseSceneBackgrounds);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbCourseSceneBackgrounds.getId();

        // 调用
        courseSceneBackgroundsService.deleteCourseSceneBackgrounds(id);
       // 校验数据不存在了
       assertNull(courseSceneBackgroundsMapper.selectById(id));
    }

    @Test
    public void testDeleteCourseSceneBackgrounds_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> courseSceneBackgroundsService.deleteCourseSceneBackgrounds(id), COURSE_SCENE_BACKGROUNDS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCourseSceneBackgroundsPage() {
       // mock 数据
       CourseSceneBackgroundsDO dbCourseSceneBackgrounds = randomPojo(CourseSceneBackgroundsDO.class, o -> { // 等会查询到
           o.setBackgroundType(null);
           o.setEntityId(null);
           o.setSrc(null);
           o.setCover(null);
           o.setWidth(null);
           o.setHeight(null);
           o.setDepth(null);
           o.setOriginWidth(null);
           o.setOriginHeight(null);
           o.setCreateTime(null);
           o.setStatus(null);
       });
       courseSceneBackgroundsMapper.insert(dbCourseSceneBackgrounds);
       // 测试 backgroundType 不匹配
       courseSceneBackgroundsMapper.insert(cloneIgnoreId(dbCourseSceneBackgrounds, o -> o.setBackgroundType(null)));
       // 测试 entityId 不匹配
       courseSceneBackgroundsMapper.insert(cloneIgnoreId(dbCourseSceneBackgrounds, o -> o.setEntityId(null)));
       // 测试 src 不匹配
       courseSceneBackgroundsMapper.insert(cloneIgnoreId(dbCourseSceneBackgrounds, o -> o.setSrc(null)));
       // 测试 cover 不匹配
       courseSceneBackgroundsMapper.insert(cloneIgnoreId(dbCourseSceneBackgrounds, o -> o.setCover(null)));
       // 测试 width 不匹配
       courseSceneBackgroundsMapper.insert(cloneIgnoreId(dbCourseSceneBackgrounds, o -> o.setWidth(null)));
       // 测试 height 不匹配
       courseSceneBackgroundsMapper.insert(cloneIgnoreId(dbCourseSceneBackgrounds, o -> o.setHeight(null)));
       // 测试 depth 不匹配
       courseSceneBackgroundsMapper.insert(cloneIgnoreId(dbCourseSceneBackgrounds, o -> o.setDepth(null)));
       // 测试 originWidth 不匹配
       courseSceneBackgroundsMapper.insert(cloneIgnoreId(dbCourseSceneBackgrounds, o -> o.setOriginWidth(null)));
       // 测试 originHeight 不匹配
       courseSceneBackgroundsMapper.insert(cloneIgnoreId(dbCourseSceneBackgrounds, o -> o.setOriginHeight(null)));
       // 测试 createTime 不匹配
       courseSceneBackgroundsMapper.insert(cloneIgnoreId(dbCourseSceneBackgrounds, o -> o.setCreateTime(null)));
       // 测试 status 不匹配
       courseSceneBackgroundsMapper.insert(cloneIgnoreId(dbCourseSceneBackgrounds, o -> o.setStatus(null)));
       // 准备参数
       AppCourseSceneBackgroundsPageReqVO reqVO = new AppCourseSceneBackgroundsPageReqVO();
       reqVO.setBackgroundType(null);
       reqVO.setEntityId(null);
       reqVO.setSrc(null);
       reqVO.setCover(null);
       reqVO.setWidth(null);
       reqVO.setHeight(null);
       reqVO.setDepth(null);
       reqVO.setOriginWidth(null);
       reqVO.setOriginHeight(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setStatus(null);

       // 调用
       PageResult<CourseSceneBackgroundsDO> pageResult = courseSceneBackgroundsService.getCourseSceneBackgroundsPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbCourseSceneBackgrounds, pageResult.getList().get(0));
    }

}