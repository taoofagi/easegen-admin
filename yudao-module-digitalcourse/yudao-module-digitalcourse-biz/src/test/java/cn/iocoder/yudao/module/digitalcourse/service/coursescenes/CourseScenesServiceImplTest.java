package cn.iocoder.yudao.module.digitalcourse.service.coursescenes;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo.AppCourseScenesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo.AppCourseScenesSaveReqVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenes.CourseScenesDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenes.CourseScenesMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CourseScenesServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(CourseScenesServiceImpl.class)
public class CourseScenesServiceImplTest extends BaseDbUnitTest {

    @Resource
    private CourseScenesServiceImpl courseScenesService;

    @Resource
    private CourseScenesMapper courseScenesMapper;

    @Test
    public void testCreateCourseScenes_success() {
        // 准备参数
        AppCourseScenesSaveReqVO createReqVO = randomPojo(AppCourseScenesSaveReqVO.class).setId(null);

        // 调用
        Long courseScenesId = courseScenesService.createCourseScenes(createReqVO);
        // 断言
        assertNotNull(courseScenesId);
        // 校验记录的属性是否正确
        CourseScenesDO courseScenes = courseScenesMapper.selectById(courseScenesId);
        assertPojoEquals(createReqVO, courseScenes, "id");
    }

    @Test
    public void testUpdateCourseScenes_success() {
        // mock 数据
        CourseScenesDO dbCourseScenes = randomPojo(CourseScenesDO.class);
        courseScenesMapper.insert(dbCourseScenes);// @Sql: 先插入出一条存在的数据
        // 准备参数
        AppCourseScenesSaveReqVO updateReqVO = randomPojo(AppCourseScenesSaveReqVO.class, o -> {
            o.setId(dbCourseScenes.getId()); // 设置更新的 ID
        });

        // 调用
        courseScenesService.updateCourseScenes(updateReqVO);
        // 校验是否更新正确
        CourseScenesDO courseScenes = courseScenesMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, courseScenes);
    }

    @Test
    public void testUpdateCourseScenes_notExists() {
        // 准备参数
        AppCourseScenesSaveReqVO updateReqVO = randomPojo(AppCourseScenesSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> courseScenesService.updateCourseScenes(updateReqVO), COURSE_SCENES_NOT_EXISTS);
    }

    @Test
    public void testDeleteCourseScenes_success() {
        // mock 数据
        CourseScenesDO dbCourseScenes = randomPojo(CourseScenesDO.class);
        courseScenesMapper.insert(dbCourseScenes);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbCourseScenes.getId();

        // 调用
        courseScenesService.deleteCourseScenes(id);
       // 校验数据不存在了
       assertNull(courseScenesMapper.selectById(id));
    }

    @Test
    public void testDeleteCourseScenes_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> courseScenesService.deleteCourseScenes(id), COURSE_SCENES_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCourseScenesPage() {
       // mock 数据
       CourseScenesDO dbCourseScenes = randomPojo(CourseScenesDO.class, o -> { // 等会查询到
           o.setOrderNo(null);
           o.setDuration(null);
           o.setDriverType(null);
           o.setBusinessId(null);
           o.setCreateTime(null);
           o.setStatus(null);
       });
       courseScenesMapper.insert(dbCourseScenes);
       // 测试 orderNo 不匹配
       courseScenesMapper.insert(cloneIgnoreId(dbCourseScenes, o -> o.setOrderNo(null)));
       // 测试 duration 不匹配
       courseScenesMapper.insert(cloneIgnoreId(dbCourseScenes, o -> o.setDuration(null)));
       // 测试 driverType 不匹配
       courseScenesMapper.insert(cloneIgnoreId(dbCourseScenes, o -> o.setDriverType(null)));
       // 测试 businessId 不匹配
       courseScenesMapper.insert(cloneIgnoreId(dbCourseScenes, o -> o.setBusinessId(null)));
       // 测试 createTime 不匹配
       courseScenesMapper.insert(cloneIgnoreId(dbCourseScenes, o -> o.setCreateTime(null)));
       // 测试 status 不匹配
       courseScenesMapper.insert(cloneIgnoreId(dbCourseScenes, o -> o.setStatus(null)));
       // 准备参数
       AppCourseScenesPageReqVO reqVO = new AppCourseScenesPageReqVO();
       reqVO.setOrderNo(null);
       reqVO.setDuration(null);
       reqVO.setDriverType(null);
       reqVO.setBusinessId(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setStatus(null);

       // 调用
       PageResult<CourseScenesDO> pageResult = courseScenesService.getCourseScenesPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbCourseScenes, pageResult.getList().get(0));
    }

}