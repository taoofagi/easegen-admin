package cn.iocoder.yudao.module.digitalcourse.service.courses;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesUpdateReqVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courses.CoursesDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.courses.CoursesMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.buildBetweenTime;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomLongId;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.randomPojo;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.COURSES_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CoursesServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(CoursesServiceImpl.class)
public class CoursesServiceImplTest extends BaseDbUnitTest {

    @Resource
    private CoursesServiceImpl coursesService;

    @Resource
    private CoursesMapper coursesMapper;

    @Test
    public void testCreateCourses_success() {
        // 准备参数
        AppCoursesSaveReqVO createReqVO = randomPojo(AppCoursesSaveReqVO.class).setId(null);

        // 调用
        Long coursesId = coursesService.createCourses(createReqVO);
        // 断言
        assertNotNull(coursesId);
        // 校验记录的属性是否正确
        CoursesDO courses = coursesMapper.selectById(coursesId);
        assertPojoEquals(createReqVO, courses, "id");
    }

    @Test
    public void testUpdateCourses_success() {
        // mock 数据
        CoursesDO dbCourses = randomPojo(CoursesDO.class);
        coursesMapper.insert(dbCourses);// @Sql: 先插入出一条存在的数据
        // 准备参数
        AppCoursesUpdateReqVO updateReqVO = randomPojo(AppCoursesUpdateReqVO.class, o -> {
            o.setId(dbCourses.getId()); // 设置更新的 ID
        });

        // 调用
//        coursesService.updateCourses(updateReqVO);
        // 校验是否更新正确
        CoursesDO courses = coursesMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, courses);
    }

    @Test
    public void testUpdateCourses_notExists() {
        // 准备参数
        AppCoursesUpdateReqVO updateReqVO = randomPojo(AppCoursesUpdateReqVO.class);

        // 调用, 并断言异常
//        assertServiceException(() -> coursesService.updateCourses(updateReqVO), COURSES_NOT_EXISTS);
    }

    @Test
    public void testDeleteCourses_success() {
        // mock 数据
        CoursesDO dbCourses = randomPojo(CoursesDO.class);
        coursesMapper.insert(dbCourses);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbCourses.getId();

        // 调用
        coursesService.deleteCourses(id);
       // 校验数据不存在了
       assertNull(coursesMapper.selectById(id));
    }

    @Test
    public void testDeleteCourses_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> coursesService.deleteCourses(id), COURSES_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCoursesPage() {
       // mock 数据
       CoursesDO dbCourses = randomPojo(CoursesDO.class, o -> { // 等会查询到
           o.setName(null);
           o.setAspect(null);
           o.setDuration(null);
           o.setHeight(null);
           o.setWidth(null);
           o.setMatting(null);
           o.setPageMode(null);
           o.setStatus(null);
           o.setPageInfo(null);
           o.setSubtitlesStyle(null);
           o.setCreateTime(null);
       });
       coursesMapper.insert(dbCourses);
       // 测试 name 不匹配
       coursesMapper.insert(cloneIgnoreId(dbCourses, o -> o.setName(null)));
       // 测试 aspect 不匹配
       coursesMapper.insert(cloneIgnoreId(dbCourses, o -> o.setAspect(null)));
       // 测试 duration 不匹配
       coursesMapper.insert(cloneIgnoreId(dbCourses, o -> o.setDuration(null)));
       // 测试 height 不匹配
       coursesMapper.insert(cloneIgnoreId(dbCourses, o -> o.setHeight(null)));
       // 测试 width 不匹配
       coursesMapper.insert(cloneIgnoreId(dbCourses, o -> o.setWidth(null)));
       // 测试 matting 不匹配
       coursesMapper.insert(cloneIgnoreId(dbCourses, o -> o.setMatting(null)));
       // 测试 pageMode 不匹配
       coursesMapper.insert(cloneIgnoreId(dbCourses, o -> o.setPageMode(null)));
       // 测试 status 不匹配
       coursesMapper.insert(cloneIgnoreId(dbCourses, o -> o.setStatus(null)));
       // 测试 pageInfo 不匹配
       coursesMapper.insert(cloneIgnoreId(dbCourses, o -> o.setPageInfo(null)));
       // 测试 subtitlesStyle 不匹配
       coursesMapper.insert(cloneIgnoreId(dbCourses, o -> o.setSubtitlesStyle(null)));
       // 测试 createTime 不匹配
       coursesMapper.insert(cloneIgnoreId(dbCourses, o -> o.setCreateTime(null)));
       // 准备参数
       AppCoursesPageReqVO reqVO = new AppCoursesPageReqVO();
       reqVO.setName(null);
       reqVO.setAspect(null);
       reqVO.setDuration(null);
       reqVO.setHeight(null);
       reqVO.setWidth(null);
       reqVO.setMatting(null);
       reqVO.setPageMode(null);
       reqVO.setStatus(null);
       reqVO.setPageInfo(null);
       reqVO.setSubtitlesStyle(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<CoursesDO> pageResult = coursesService.getCoursesPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbCourses, pageResult.getList().get(0));
    }

}