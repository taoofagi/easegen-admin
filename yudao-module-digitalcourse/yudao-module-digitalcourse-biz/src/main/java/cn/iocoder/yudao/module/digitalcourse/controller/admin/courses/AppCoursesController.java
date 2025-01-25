package cn.iocoder.yudao.module.digitalcourse.controller.admin.courses;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.*;
import cn.iocoder.yudao.module.digitalcourse.util.GenQuestionUtil;
import cn.iocoder.yudao.module.digitalcourse.util.Pdf2MdUtil;
import cn.iocoder.yudao.module.infra.api.config.ConfigApi;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.BAD_REQUEST;
import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.UNAUTHORIZED;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;


import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.courses.CoursesDO;
import cn.iocoder.yudao.module.digitalcourse.service.courses.CoursesService;
import com.alibaba.fastjson.JSON;

@Tag(name = "用户 APP - 存储课程的基本信息，包括课程名称、时长、状态等")
@RestController
@RequestMapping("/digitalcourse/courses")
@Validated
public class AppCoursesController {

    private static final String EASEGEN_CORE_URL = "easegen.core.url";

    static final String EASEGEN_CORE_KEY = "easegen.core.key";

    static final String USER_APIKEY_KEY = "system:user:apikey:";

    @Resource
    private CoursesService coursesService;

    @Resource
    private ConfigApi configApi;

    @Resource
    private GenQuestionUtil genQuestionUtil;

    @Resource
    private Pdf2MdUtil pdf2MdUtil;

    @Resource
    private StringRedisTemplate redisCache;


    @PostMapping("/create")
    @Operation(summary = "创建存储课程的基本信息，包括课程名称、时长、状态等")
    public CommonResult<Long> createCourses(@Valid @RequestBody AppCoursesSaveReqVO createReqVO) {
        return success(coursesService.createCourses(createReqVO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新存储课程的基本信息，包括课程名称、时长、状态等")
    public CommonResult<Boolean> updateCourses(@Valid @RequestBody AppCoursesUpdateReqVO updateReqVO) {
        coursesService.updateCourses(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除存储课程的基本信息，包括课程名称、时长、状态等")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteCourses(@RequestParam("id") Long id) {
        coursesService.deleteCourses(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得存储课程的基本信息，包括课程名称、时长、状态等")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<AppCoursesUpdateReqVO> getCourses(@RequestParam("id") Long id) {
        AppCoursesUpdateReqVO courses = coursesService.getCourses(id);
        return success(courses);
    }

    @GetMapping("/page")
    @Operation(summary = "获得存储课程的基本信息，包括课程名称、时长、状态等分页")
    public CommonResult<PageResult<AppCoursesRespVO>> getCoursesPage(@Valid AppCoursesPageReqVO pageReqVO) {
        PageResult<AppCoursesRespVO> pageResult = coursesService.getCoursesPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppCoursesRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出存储课程的基本信息，包括课程名称、时长、状态等 Excel")
    public void exportCoursesExcel(@Valid AppCoursesPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AppCoursesRespVO> list = coursesService.getCoursesPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "存储课程的基本信息，包括课程名称、时长、状态等.xls", "数据", AppCoursesRespVO.class,
                        BeanUtils.toBean(list, AppCoursesRespVO.class));
    }

    @PostMapping("/genQuestion")
    public CommonResult<String> genQuestion(@RequestBody Map<String, Object> requestParams) {
        String apiUrl = configApi.getConfigValueByKey(EASEGEN_CORE_URL) + "/api/generate_questions";
        String apiKey = configApi.getConfigValueByKey(EASEGEN_CORE_KEY);
        // 创建HTTP客户端
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建POST请求
            HttpPost httpPost = new HttpPost(apiUrl);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("X-API-Key", apiKey);
            httpPost.setEntity(new StringEntity(JSON.toJSONString(requestParams), ContentType.APPLICATION_JSON));
            // 执行请求
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getCode();
                String responseString = EntityUtils.toString(response.getEntity());
                if (statusCode == 200) {
                    return CommonResult.success(responseString);
                } else {
                    return error(statusCode, responseString);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return error(500, "Internal Server Error: " + e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return error(500, "Internal Server Error: " + e.getMessage());
        }
    }

    @PostMapping("/generateExcel")
    public void generateExcelFromQuestions(@RequestBody String jsonString, HttpServletResponse response) {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String arrayString = jsonObject.getString("jsonString");
        JSONArray questionsArray = JSON.parseArray(arrayString);

        // Load the template
        try {
            Workbook workbook = genQuestionUtil.genExcelByJson(questionsArray);

            // Set the content type for Excel file
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode("智能试题_","UTF-8") + timestamp + ".xlsx");

            try (ServletOutputStream outputStream = response.getOutputStream()) {
                workbook.write(outputStream);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/docparse")
    public CommonResult<String> docparse(@RequestBody Map<String, Object> requestParams) {
        String type = (String) requestParams.get("type");
        String fileUrl = (String) requestParams.get("fileUrl");
        try {
            String responseString = pdf2MdUtil.recognizeMarkdown(fileUrl, type);
            return CommonResult.success(responseString);
        } catch (Exception e) {
            e.printStackTrace();
            return error(500, "Internal Server Error: " + e.getMessage());
        }

    }


    @GetMapping("/getCourseText")
    @Operation(summary = "获得存储课程的基本信息，包括文本、音频、图片等，拆分返回，支持fay实时数字人")
    @Parameter(name = "course_id", description = "课程编号", required = true, example = "1024")
    @Parameter(name = "no", description = "item序号，非必须，默认是1或用户当前上课进度", required = false, example = "1")
    public CommonResult<CourseTextRespVO> getCourseText(@RequestParam String course_id,
                                                        @RequestParam(required = false) Integer no,
                                                        @RequestHeader(value = "easegen-api-key", required = false) String apiKey) {
        if (StrUtil.isBlank(apiKey)) {
            return error(BAD_REQUEST.getCode(), "缺少 easegen-api-key 请求头或请求头为空");
        }
        // 通过apiKey从缓存中获取用户信息
        String userid = redisCache.opsForValue().get(USER_APIKEY_KEY + apiKey);
        if (userid == null) {
            return error(BAD_REQUEST.getCode(), "未找到用户信息，请确认apikey是否正确");
        }
        
        CourseTextRespVO response = coursesService.getCourseText(course_id, userid, no);
        return success(response);
    }


    @GetMapping("/getCourseProgress")
    @Operation(summary = "获得上课进度")
    @Parameter(name = "course_id", description = "课程编号", required = true, example = "1024")
    public CommonResult<String> getCourseProgress(@RequestParam String course_id) {
        String response = coursesService.getCourseProgress(course_id);
        return success(response);
    }

    @GetMapping("/getCoursePage")
    @Operation(summary = "获得存储课程的基本信息，包括课程名称、时长、状态等分页")
    public CommonResult<PageResult<AppCoursesRespVO>> getCoursePage(@Valid AppCoursesPageReqVO pageReqVO,
                                                                    @RequestHeader(value = "easegen-api-key", required = false) String apiKey) {
        if (StrUtil.isBlank(apiKey)) {
            return error(BAD_REQUEST.getCode(), "缺少 easegen-api-key 请求头或请求头为空");
        }
        // 通过apiKey从缓存中获取用户信息
        String userid = redisCache.opsForValue().get(USER_APIKEY_KEY + apiKey);
        if (userid == null) {
            return error(BAD_REQUEST.getCode(), "未找到用户信息，请确认apikey是否正确");
        }
        PageResult<AppCoursesRespVO> pageResult = coursesService.getCoursesPage(pageReqVO, userid);
        return success(BeanUtils.toBean(pageResult, AppCoursesRespVO.class));
    }



}