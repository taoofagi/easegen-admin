package cn.iocoder.yudao.module.digitalcourse.service.coursescenevoices;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo.AppCourseSceneVoicesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenevoices.vo.AppCourseSceneVoicesSaveReqVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursescenevoices.CourseSceneVoicesDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursescenevoices.CourseSceneVoicesMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CourseSceneVoicesServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(CourseSceneVoicesServiceImpl.class)
public class CourseSceneVoicesServiceImplTest extends BaseDbUnitTest {

    @Resource
    private CourseSceneVoicesServiceImpl courseSceneVoicesService;

    @Resource
    private CourseSceneVoicesMapper courseSceneVoicesMapper;

    @Test
    public void testCreateCourseSceneVoices_success() {
        // 准备参数
        AppCourseSceneVoicesSaveReqVO createReqVO = randomPojo(AppCourseSceneVoicesSaveReqVO.class).setId(null);

        // 调用
        Long courseSceneVoicesId = courseSceneVoicesService.createCourseSceneVoices(createReqVO);
        // 断言
        assertNotNull(courseSceneVoicesId);
        // 校验记录的属性是否正确
        CourseSceneVoicesDO courseSceneVoices = courseSceneVoicesMapper.selectById(courseSceneVoicesId);
        assertPojoEquals(createReqVO, courseSceneVoices, "id");
    }

    @Test
    public void testUpdateCourseSceneVoices_success() {
        // mock 数据
        CourseSceneVoicesDO dbCourseSceneVoices = randomPojo(CourseSceneVoicesDO.class);
        courseSceneVoicesMapper.insert(dbCourseSceneVoices);// @Sql: 先插入出一条存在的数据
        // 准备参数
        AppCourseSceneVoicesSaveReqVO updateReqVO = randomPojo(AppCourseSceneVoicesSaveReqVO.class, o -> {
            o.setId(dbCourseSceneVoices.getId()); // 设置更新的 ID
        });

        // 调用
        courseSceneVoicesService.updateCourseSceneVoices(updateReqVO);
        // 校验是否更新正确
        CourseSceneVoicesDO courseSceneVoices = courseSceneVoicesMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, courseSceneVoices);
    }

    @Test
    public void testUpdateCourseSceneVoices_notExists() {
        // 准备参数
        AppCourseSceneVoicesSaveReqVO updateReqVO = randomPojo(AppCourseSceneVoicesSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> courseSceneVoicesService.updateCourseSceneVoices(updateReqVO), COURSE_SCENE_VOICES_NOT_EXISTS);
    }

    @Test
    public void testDeleteCourseSceneVoices_success() {
        // mock 数据
        CourseSceneVoicesDO dbCourseSceneVoices = randomPojo(CourseSceneVoicesDO.class);
        courseSceneVoicesMapper.insert(dbCourseSceneVoices);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbCourseSceneVoices.getId();

        // 调用
        courseSceneVoicesService.deleteCourseSceneVoices(id);
       // 校验数据不存在了
       assertNull(courseSceneVoicesMapper.selectById(id));
    }

    @Test
    public void testDeleteCourseSceneVoices_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> courseSceneVoicesService.deleteCourseSceneVoices(id), COURSE_SCENE_VOICES_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCourseSceneVoicesPage() {
       // mock 数据
       CourseSceneVoicesDO dbCourseSceneVoices = randomPojo(CourseSceneVoicesDO.class, o -> { // 等会查询到
           o.setTonePitch(null);
           o.setVoiceType(null);
           o.setSpeechRate(null);
           o.setName(null);
           o.setCreateTime(null);
           o.setStatus(null);
       });
       courseSceneVoicesMapper.insert(dbCourseSceneVoices);
       // 测试 tonePitch 不匹配
       courseSceneVoicesMapper.insert(cloneIgnoreId(dbCourseSceneVoices, o -> o.setTonePitch(null)));
       // 测试 voiceType 不匹配
       courseSceneVoicesMapper.insert(cloneIgnoreId(dbCourseSceneVoices, o -> o.setVoiceType(null)));
       // 测试 speechRate 不匹配
       courseSceneVoicesMapper.insert(cloneIgnoreId(dbCourseSceneVoices, o -> o.setSpeechRate(null)));
       // 测试 name 不匹配
       courseSceneVoicesMapper.insert(cloneIgnoreId(dbCourseSceneVoices, o -> o.setName(null)));
       // 测试 createTime 不匹配
       courseSceneVoicesMapper.insert(cloneIgnoreId(dbCourseSceneVoices, o -> o.setCreateTime(null)));
       // 测试 status 不匹配
       courseSceneVoicesMapper.insert(cloneIgnoreId(dbCourseSceneVoices, o -> o.setStatus(null)));
       // 准备参数
       AppCourseSceneVoicesPageReqVO reqVO = new AppCourseSceneVoicesPageReqVO();
       reqVO.setTonePitch(null);
       reqVO.setVoiceType(null);
       reqVO.setSpeechRate(null);
       reqVO.setName(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setStatus(null);

       // 调用
       PageResult<CourseSceneVoicesDO> pageResult = courseSceneVoicesService.getCourseSceneVoicesPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbCourseSceneVoices, pageResult.getList().get(0));
    }

}