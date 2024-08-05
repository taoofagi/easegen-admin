package cn.iocoder.yudao.module.digitalcourse.service.coursesceneaudios;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios.vo.AppCourseSceneAudiosPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursesceneaudios.vo.AppCourseSceneAudiosSaveReqVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursesceneaudios.CourseSceneAudiosDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursesceneaudios.CourseSceneAudiosMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CourseSceneAudiosServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(CourseSceneAudiosServiceImpl.class)
public class CourseSceneAudiosServiceImplTest extends BaseDbUnitTest {

    @Resource
    private CourseSceneAudiosServiceImpl courseSceneAudiosService;

    @Resource
    private CourseSceneAudiosMapper courseSceneAudiosMapper;

    @Test
    public void testCreateCourseSceneAudios_success() {
        // 准备参数
        AppCourseSceneAudiosSaveReqVO createReqVO = randomPojo(AppCourseSceneAudiosSaveReqVO.class).setId(null);

        // 调用
        Long courseSceneAudiosId = courseSceneAudiosService.createCourseSceneAudios(createReqVO);
        // 断言
        assertNotNull(courseSceneAudiosId);
        // 校验记录的属性是否正确
        CourseSceneAudiosDO courseSceneAudios = courseSceneAudiosMapper.selectById(courseSceneAudiosId);
        assertPojoEquals(createReqVO, courseSceneAudios, "id");
    }

    @Test
    public void testUpdateCourseSceneAudios_success() {
        // mock 数据
        CourseSceneAudiosDO dbCourseSceneAudios = randomPojo(CourseSceneAudiosDO.class);
        courseSceneAudiosMapper.insert(dbCourseSceneAudios);// @Sql: 先插入出一条存在的数据
        // 准备参数
        AppCourseSceneAudiosSaveReqVO updateReqVO = randomPojo(AppCourseSceneAudiosSaveReqVO.class, o -> {
            o.setId(dbCourseSceneAudios.getId()); // 设置更新的 ID
        });

        // 调用
        courseSceneAudiosService.updateCourseSceneAudios(updateReqVO);
        // 校验是否更新正确
        CourseSceneAudiosDO courseSceneAudios = courseSceneAudiosMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, courseSceneAudios);
    }

    @Test
    public void testUpdateCourseSceneAudios_notExists() {
        // 准备参数
        AppCourseSceneAudiosSaveReqVO updateReqVO = randomPojo(AppCourseSceneAudiosSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> courseSceneAudiosService.updateCourseSceneAudios(updateReqVO), COURSE_SCENE_AUDIOS_NOT_EXISTS);
    }

    @Test
    public void testDeleteCourseSceneAudios_success() {
        // mock 数据
        CourseSceneAudiosDO dbCourseSceneAudios = randomPojo(CourseSceneAudiosDO.class);
        courseSceneAudiosMapper.insert(dbCourseSceneAudios);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbCourseSceneAudios.getId();

        // 调用
        courseSceneAudiosService.deleteCourseSceneAudios(id);
       // 校验数据不存在了
       assertNull(courseSceneAudiosMapper.selectById(id));
    }

    @Test
    public void testDeleteCourseSceneAudios_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> courseSceneAudiosService.deleteCourseSceneAudios(id), COURSE_SCENE_AUDIOS_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetCourseSceneAudiosPage() {
       // mock 数据
       CourseSceneAudiosDO dbCourseSceneAudios = randomPojo(CourseSceneAudiosDO.class, o -> { // 等会查询到
           o.setUseVideoBackgroundAudio(null);
           o.setCreateTime(null);
           o.setStatus(null);
       });
       courseSceneAudiosMapper.insert(dbCourseSceneAudios);
       // 测试 useVideoBackgroundAudio 不匹配
       courseSceneAudiosMapper.insert(cloneIgnoreId(dbCourseSceneAudios, o -> o.setUseVideoBackgroundAudio(null)));
       // 测试 createTime 不匹配
       courseSceneAudiosMapper.insert(cloneIgnoreId(dbCourseSceneAudios, o -> o.setCreateTime(null)));
       // 测试 status 不匹配
       courseSceneAudiosMapper.insert(cloneIgnoreId(dbCourseSceneAudios, o -> o.setStatus(null)));
       // 准备参数
       AppCourseSceneAudiosPageReqVO reqVO = new AppCourseSceneAudiosPageReqVO();
       reqVO.setUseVideoBackgroundAudio(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setStatus(null);

       // 调用
       PageResult<CourseSceneAudiosDO> pageResult = courseSceneAudiosService.getCourseSceneAudiosPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbCourseSceneAudios, pageResult.getList().get(0));
    }

}