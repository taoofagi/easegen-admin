package cn.iocoder.yudao.module.digitalcourse.service.coursescenetexts;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo.AppCourseSceneTextsPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenetexts.vo.AppCourseSceneTextsSaveReqVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenetexts.CourseSceneTextsDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenetexts.CourseSceneTextsMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CourseSceneTextsServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(CourseSceneTextsServiceImpl.class)
public class CourseSceneTextsServiceImplTest extends BaseDbUnitTest {

    @Resource
    private CourseSceneTextsServiceImpl courseSceneTextsService;

    @Resource
    private CourseSceneTextsMapper courseSceneTextsMapper;

    @Test
    public void testCreateCourseSceneTexts_success() {
        // 准备参数
        AppCourseSceneTextsSaveReqVO createReqVO = randomPojo(AppCourseSceneTextsSaveReqVO.class).setId(null);

        // 调用
        Long courseSceneTextsId = courseSceneTextsService.createCourseSceneTexts(createReqVO);
        // 断言
        assertNotNull(courseSceneTextsId);
        // 校验记录的属性是否正确
        CourseSceneTextsDO courseSceneTexts = courseSceneTextsMapper.selectById(courseSceneTextsId);
        assertPojoEquals(createReqVO, courseSceneTexts, "id");
    }

    @Test
    public void testUpdateCourseSceneTexts_success() {
        // mock 数据
        CourseSceneTextsDO dbCourseSceneTexts = randomPojo(CourseSceneTextsDO.class);
        courseSceneTextsMapper.insert(dbCourseSceneTexts);// @Sql: 先插入出一条存在的数据
        // 准备参数
        AppCourseSceneTextsSaveReqVO updateReqVO = randomPojo(AppCourseSceneTextsSaveReqVO.class, o -> {
            o.setId(dbCourseSceneTexts.getId()); // 设置更新的 ID
        });

        // 调用
        courseSceneTextsService.updateCourseSceneTexts(updateReqVO);
        // 校验是否更新正确
        CourseSceneTextsDO courseSceneTexts = courseSceneTextsMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, courseSceneTexts);
    }

    @Test
    public void testUpdateCourseSceneTexts_notExists() {
        // 准备参数
        AppCourseSceneTextsSaveReqVO updateReqVO = randomPojo(AppCourseSceneTextsSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> courseSceneTextsService.updateCourseSceneTexts(updateReqVO), COURSE_SCENE_TEXTS_NOT_EXISTS);
    }

    @Test
    public void testDeleteCourseSceneTexts_success() {
        // mock 数据
        CourseSceneTextsDO dbCourseSceneTexts = randomPojo(CourseSceneTextsDO.class);
        courseSceneTextsMapper.insert(dbCourseSceneTexts);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbCourseSceneTexts.getId();

        // 调用
        courseSceneTextsService.deleteCourseSceneTexts(id);
       // 校验数据不存在了
       assertNull(courseSceneTextsMapper.selectById(id));
    }

    @Test
    public void testDeleteCourseSceneTexts_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> courseSceneTextsService.deleteCourseSceneTexts(id), COURSE_SCENE_TEXTS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCourseSceneTextsPage() {
       // mock 数据
       CourseSceneTextsDO dbCourseSceneTexts = randomPojo(CourseSceneTextsDO.class, o -> { // 等会查询到
           o.setPitch(null);
           o.setSpeed(null);
           o.setVolume(null);
           o.setSmartSpeed(null);
           o.setSpeechRate(null);
           o.setTextJson(null);
           o.setCreateTime(null);
           o.setStatus(null);
       });
       courseSceneTextsMapper.insert(dbCourseSceneTexts);
       // 测试 pitch 不匹配
       courseSceneTextsMapper.insert(cloneIgnoreId(dbCourseSceneTexts, o -> o.setPitch(null)));
       // 测试 speed 不匹配
       courseSceneTextsMapper.insert(cloneIgnoreId(dbCourseSceneTexts, o -> o.setSpeed(null)));
       // 测试 volume 不匹配
       courseSceneTextsMapper.insert(cloneIgnoreId(dbCourseSceneTexts, o -> o.setVolume(null)));
       // 测试 smartSpeed 不匹配
       courseSceneTextsMapper.insert(cloneIgnoreId(dbCourseSceneTexts, o -> o.setSmartSpeed(null)));
       // 测试 speechRate 不匹配
       courseSceneTextsMapper.insert(cloneIgnoreId(dbCourseSceneTexts, o -> o.setSpeechRate(null)));
       // 测试 textJson 不匹配
       courseSceneTextsMapper.insert(cloneIgnoreId(dbCourseSceneTexts, o -> o.setTextJson(null)));
       // 测试 createTime 不匹配
       courseSceneTextsMapper.insert(cloneIgnoreId(dbCourseSceneTexts, o -> o.setCreateTime(null)));
       // 测试 status 不匹配
       courseSceneTextsMapper.insert(cloneIgnoreId(dbCourseSceneTexts, o -> o.setStatus(null)));
       // 准备参数
       AppCourseSceneTextsPageReqVO reqVO = new AppCourseSceneTextsPageReqVO();
       reqVO.setPitch(null);
       reqVO.setSpeed(null);
       reqVO.setVolume(null);
       reqVO.setSmartSpeed(null);
       reqVO.setSpeechRate(null);
       reqVO.setTextJson(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setStatus(null);

       // 调用
       PageResult<CourseSceneTextsDO> pageResult = courseSceneTextsService.getCourseSceneTextsPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbCourseSceneTexts, pageResult.getList().get(0));
    }

}