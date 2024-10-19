package cn.iocoder.yudao.module.digitalcourse.service.courses;

import cn.hutool.core.collection.CollectionUtil;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.*;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo.AppCourseScenesSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.service.coursescenes.CourseScenesService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courses.CoursesDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.digitalcourse.dal.mysql.courses.CoursesMapper;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.digitalcourse.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.USER_NOT_EXISTS;

/**
 * 存储课程的基本信息，包括课程名称、时长、状态等 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class CoursesServiceImpl implements CoursesService {

    private static final String COURSE_SCENE_TEXT_KEY = "COURSE_SCENE_TEXT_KEY:";
    private static final String COURSE_SEGMENT_TEXT_KEY = "COURSE_SEGMENT_TEXT_KEY:";

    private static final String COURSE_PROGRESS_KEY = "COURSE_PROCESS_KEY:";

    @Resource
    private CoursesMapper coursesMapper;
    @Resource
    private CourseScenesService courseScenesService;
    @Resource
    private StringRedisTemplate redisCache;
    @Resource
    private AdminUserApi adminUserApi;

    @Override
    public Long createCourses(AppCoursesSaveReqVO createReqVO) {
        // 插入
        CoursesDO courses = BeanUtils.toBean(createReqVO, CoursesDO.class);
        if (StringUtils.isBlank(courses.getName())) courses.setName("未命名草稿");
        if (StringUtils.isBlank(courses.getAspect())) courses.setAspect("16:9");
        if (courses.getDuration() == null) courses.setDuration(0);
        if (courses.getHeight() == null) courses.setHeight(0);
        if (courses.getWidth() == null) courses.setWidth(0);
        if (courses.getMatting() == null) courses.setMatting(0);
        if (courses.getPageMode() == null) courses.setPageMode(0);
        if (courses.getStatus() == null) courses.setStatus(0);
        if (courses.getPageInfo() == null) courses.setPageInfo("");
        if (courses.getSubtitlesStyle() == null) courses.setSubtitlesStyle("{}");
        coursesMapper.insert(courses);
        // 返回
        return courses.getId();
    }

    @Override
    public void updateCourses(AppCoursesUpdateReqVO updateReqVO) {
        // 校验存在
        validateCoursesExists(updateReqVO.getId());
        // TODO 后面考虑做一个更新标志，使用hash等方式进行比对，如果本次数据与上次保存数据一致，则不执行更新操作，防止浪费数据库资源
        // 更新
        CoursesDO updateObj = BeanUtils.toBean(updateReqVO, CoursesDO.class);

        // 构造 COURSE_SCENE_TEXT_KEY 和 COURSE_SEGMENT_TEXT_KEY 所需要的缓存数据
        String sceneRedisKey = COURSE_SCENE_TEXT_KEY + updateObj.getId();
        String segmentRedisKey = COURSE_SEGMENT_TEXT_KEY + updateObj.getId();

        // 先删除历史数据
        courseScenesService.batchRemoveCouseScenes(updateReqVO.getId());

        // 重新insert
        List<AppCourseScenesSaveReqVO> scenes = updateReqVO.getScenes();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, String>> scenesMap = new HashMap<>();
        scenes.stream().forEach(e -> {
            e.setCourseId(updateObj.getId());
            e.setStatus(0);

            // 构建缓存数据 TODO 后面要考虑文本是ssml格式，需要转换
            Map<String, String> sceneData = new HashMap<>();
            sceneData.put("text", e.getTextDriver().getTextJson());
            sceneData.put("background", e.getBackground().getSrc());
            scenesMap.put(String.valueOf(e.getOrderNo()), sceneData);
        });

        // 将所有场景数据存放到一个key中
        try {
            redisCache.delete(sceneRedisKey);
            redisCache.delete(segmentRedisKey);
            String serializedScenes = objectMapper.writeValueAsString(scenesMap);
            log.info("序列化场景数据为：" + serializedScenes);
            redisCache.opsForValue().set(sceneRedisKey, serializedScenes);
        }  catch (JsonProcessingException e) {
            log.error("序列化场景数据时发生错误", e);
            //不抛异常，不影响更新动作
//            throw new RuntimeException(e);
        }


        courseScenesService.batchCreateCourseScenes(scenes);
        coursesMapper.updateById(updateObj);
    }

    @Override
    public void deleteCourses(Long id) {
        // 校验存在
        validateCoursesExists(id);
        // 删除
        coursesMapper.deleteById(id);
    }

    private void validateCoursesExists(Long id) {
        if (coursesMapper.selectById(id) == null) {
            throw exception(COURSES_NOT_EXISTS);
        }
    }

    @Override
    public AppCoursesUpdateReqVO getCourses(Long id) {
        CoursesDO coursesDO = coursesMapper.selectById(id);
        AppCoursesUpdateReqVO bean = BeanUtils.toBean(coursesDO, AppCoursesUpdateReqVO.class);
        List<AppCourseScenesSaveReqVO> appCourseScenesSaveReqVOS = courseScenesService.selectScenesInfo(id);
        bean.setScenes(appCourseScenesSaveReqVOS);
        return bean;
    }



    @Override
    public PageResult<AppCoursesRespVO> getCoursesPage(AppCoursesPageReqVO pageReqVO) {
        // 获取分页的课程列表
        PageResult<CoursesDO> pageResult = coursesMapper.selectPage(pageReqVO);

        // 将 CoursesDO 转换为 AppCoursesRespVO，并设置进度信息
        List<AppCoursesRespVO> respVOList = pageResult.getList().stream().map(course -> {
            AppCoursesRespVO respVO = new AppCoursesRespVO();
            // 复制课程的基本信息
            BeanUtils.copyProperties(course, respVO);
            // 获取并设置课程进度
            String progress = getCourseProgress(String.valueOf(course.getId()));
            respVO.setProgress(progress);
            return respVO;
        }).collect(Collectors.toList());

        // 构建返回的分页结果
        return new PageResult<>(respVOList, pageResult.getTotal());
    }

    /**
     * 根据课程ID、用户名和项目序号获取课程文本、音频和PPT的详细信息。
     *
     * @param courseId 课程ID
     * @param username 用户名
     * @param no item序号，默认是1或用户进度
     * @return 课程文本响应体
     */
    public CourseTextRespVO getCourseText(String courseId, String username, int no) {

        AdminUserRespDTO adminUserRespDTO = adminUserApi.getUserByName(username);
        if(adminUserRespDTO == null){
            throw exception(USER_NOT_EXISTS);
        }
        CoursesDO coursesDO = coursesMapper.selectOne(new LambdaQueryWrapper<CoursesDO>().eq(CoursesDO::getId, courseId).eq(CoursesDO::getCreator, adminUserRespDTO.getId()));
        if(null==coursesDO) {
            throw exception(COURSES_NOT_EXISTS);
        }
//        long step1StartTime = System.currentTimeMillis();
//        String redisKey = COURSE_SEGMENT_TEXT_KEY + courseId + ":" + no;

        // 步骤 1：尝试从缓存中获取数据
//        String cachedData = redisCache.opsForValue().get(redisKey);

//        long step1EndTime = System.currentTimeMillis();
//        log.info("步骤 1 耗时: " + (step1EndTime - step1StartTime)/1000.0 + " 秒");

//        if (cachedData != null) {
//            // 反序列化并返回缓存数据
//            return parseCachedData(cachedData);
//        }

        long step2StartTime = System.currentTimeMillis();
        // 步骤 2：如果缓存中没有数据，则从源获取数据
        List<Scene> scenes = fetchScenesByCourseId(courseId);
        long step2EndTime = System.currentTimeMillis();
        log.info("步骤 2 耗时: " + (step2EndTime - step2StartTime)/1000.0 + " 秒");
        //如果cachedData 为空，则返回课程不存在
        if (CollectionUtil.isEmpty(scenes)) {
            throw exception(COURSES_TEXT_NOT_EXISTS);
        }

        long step3StartTime = System.currentTimeMillis();
        // 步骤 3：拆分文本并处理场景
        List<Segment> segments = splitScenesIntoSegments(scenes);
        long step3EndTime = System.currentTimeMillis();
        log.info("步骤 3 耗时: " + (step3EndTime - step3StartTime)/1000.0 + " 秒");

        // 步骤 4：返回请求的段落
        long step4StartTime = System.currentTimeMillis();
        //现在速度已经很快了，不需要再缓存了
//        cacheSegments(courseId, segments);
        long step4EndTime = System.currentTimeMillis();
        log.info("步骤 4 耗时: " + (step4EndTime - step4StartTime)/1000.0 + " 秒");

        // 步骤 5：返回请求的段落
        long step5StartTime = System.currentTimeMillis();
        Segment requestedSegment = segments.get(Math.min(no - 1, segments.size() - 1));
        long step5EndTime = System.currentTimeMillis();
        log.info("步骤 5 耗时: " + (step5EndTime - step5StartTime)/1000.0 + " 秒");

        //记录上课进度
        if(requestedSegment.getNo() != segments.size()) {
            redisCache.opsForValue().set(COURSE_PROGRESS_KEY + courseId , String.valueOf(requestedSegment.getNo() +"/"+segments.size()), 1, TimeUnit.HOURS);
        } else {
            //播放完成，则删掉redis中缓存
            redisCache.delete(COURSE_PROGRESS_KEY + courseId);
        }


        return new CourseTextRespVO(
                "",
                requestedSegment.getText(),
                System.currentTimeMillis(),
                requestedSegment.getImageUrl(),
                requestedSegment.getNo(),
                segments.size()
        );
    }

    @Override
    public String getCourseProgress(String courseId) {
        if(redisCache.opsForValue().get(COURSE_PROGRESS_KEY + courseId)==null) {
            return "";
        }
        return redisCache.opsForValue().get(COURSE_PROGRESS_KEY + courseId);
    }

    /**
     * 根据课程ID获取所有的场景信息。
     *
     * @param courseId 课程ID
     * @return 场景列表
     */
    private List<Scene> fetchScenesByCourseId(String courseId) {
        String redisKey = COURSE_SCENE_TEXT_KEY + courseId;
        String cachedData = redisCache.opsForValue().get(redisKey);
        List<Scene> scenes = new ArrayList<>();
        if (cachedData != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Map<String, String>> scenesMap = objectMapper.readValue(cachedData, new TypeReference<Map<String, Map<String, String>>>() {});
                scenesMap.entrySet().stream()
                        .sorted((e1, e2) -> Integer.compare(Integer.parseInt(e1.getKey()), Integer.parseInt(e2.getKey())))
                        .forEachOrdered(entry -> {
                            Map<String, String> sceneData = entry.getValue();
                            scenes.add(new Scene(sceneData.get("text"), sceneData.get("background")));
                        });
            } catch (JsonProcessingException e) {
                log.error("反序列化场景数据时发生错误", e);
            }
        }
        return scenes;
    }

    /**
     * 将场景文本逐个拆分为各个段落。
     *
     * @param scenes 场景列表
     * @return 分段列表
     */
    private List<Segment> splitScenesIntoSegments(List<Scene> scenes) {
        List<Segment> segments = new ArrayList<>();
        int totalNo = 0;

        // 步骤 1：初始化文本总数变量
        for (Scene scene : scenes) {
            // 步骤 2：遍历场景数据
            String[] splitTexts = scene.getText().split("[，。！？；：,.!?;:\\n\\r]+\\s*");


            // 步骤 3：使用标点符号对场景的文本进行拆分
            for (String splitText : splitTexts) {
                if (!splitText.isEmpty()) {
                    // 步骤 4：拆分后，循环构建Segment对象，放到list中
                    segments.add(new Segment(++totalNo, splitText, scene.getImageUrl(), 0));
                }
            }
        }
        return segments;
    }

    /**
     * 将段落数据缓存到Redis中以便后续查询。
     *
     * @param courseId 课程ID
     * @param segments 分段列表
     */
    private void cacheSegments(String courseId, List<Segment> segments) {
        for (Segment segment : segments) {
            String redisKey = COURSE_SEGMENT_TEXT_KEY + courseId + ":" + segment.getNo();
            String serializedData = serializeSegment(segment,segments.size());
            redisCache.opsForValue().set(redisKey, serializedData, 1, TimeUnit.DAYS);
        }
    }

    /**
     * 将段落对象序列化为存储数据字符串。
     *
     * @param segment 分段对象
     * @return 序列化后的字符串
     */
    private String serializeSegment(Segment segment, int totalNo) {
        // Simulate serialization logic
        return String.format("%s|%s|%d|%d", segment.getText(), segment.getImageUrl(), segment.getNo(), totalNo);
    }

    /**
     * 将缓存的数据转换为CourseTextResponse对象。
     *
     * @param cachedData 缓存数据
     * @return CourseTextResponse对象
     */
    private CourseTextRespVO parseCachedData(String cachedData) {
        // Simulate deserialization logic
        String[] parts = cachedData.split("\\|");
        return new CourseTextRespVO(
                "",
                parts[0],
                System.currentTimeMillis(),
                parts[1],
                Integer.parseInt(parts[2]),
                Integer.parseInt(parts[3]) // Example value
        );
    }

    class Scene {
        private String text;
        private String imageUrl;

        public Scene(String text, String imageUrl) {
            this.text = text;
            this.imageUrl = imageUrl;
        }

        public String getText() {
            return text;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    class Segment {
        private int no;
        private String text;
        private String imageUrl;
        private int totalNo;

        public Segment(int no, String text, String imageUrl, int totalNo) {
            this.no = no;
            this.text = text;
            this.imageUrl = imageUrl;
            this.totalNo = totalNo;
        }

        public int getNo() {
            return no;
        }

        public String getText() {
            return text;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public int getTotalNo() {
            return totalNo;
        }
    }

}