package cn.iocoder.yudao.module.digitalcourse.controller.admin.courses;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesRespVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesUpdateReqVO;
import cn.iocoder.yudao.module.infra.api.config.ConfigApi;
import jakarta.annotation.Resource;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
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

    @Resource
    private CoursesService coursesService;

    @Resource
    private ConfigApi configApi;


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
        PageResult<CoursesDO> pageResult = coursesService.getCoursesPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AppCoursesRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出存储课程的基本信息，包括课程名称、时长、状态等 Excel")
    public void exportCoursesExcel(@Valid AppCoursesPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CoursesDO> list = coursesService.getCoursesPage(pageReqVO).getList();
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
                    return CommonResult.error(statusCode, responseString);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return CommonResult.error(500, "Internal Server Error: " + e.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return CommonResult.error(500, "Internal Server Error: " + e.getMessage());
        }
    }

}