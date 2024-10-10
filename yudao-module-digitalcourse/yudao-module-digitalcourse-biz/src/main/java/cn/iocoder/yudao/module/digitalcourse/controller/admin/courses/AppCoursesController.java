package cn.iocoder.yudao.module.digitalcourse.controller.admin.courses;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesPageReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesRespVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesSaveReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo.AppCoursesUpdateReqVO;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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

    @PostMapping("/generateExcel")
    public void generateExcelFromQuestions(@RequestBody String jsonString, HttpServletResponse response) {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String arrayString = jsonObject.getString("jsonString");
        JSONArray questionsArray = JSON.parseArray(arrayString);

        // Load the template
        try (InputStream fis = getClass().getClassLoader().getResourceAsStream("template/题目导入模板.xlsx");
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int rowIndex = 2; // Start writing questions from row 1, since row 0 is the header

            CellStyle cellStyle = createCellStyle(workbook);

            for (int i = 0; i < questionsArray.size(); i++) {
                JSONObject questionObject = questionsArray.getJSONObject(i);
                Row row = sheet.createRow(rowIndex++);
                fillRowWithQuestionData(row, questionObject, cellStyle);
            }

            // Set the content type for Excel file
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            response.setHeader("Content-Disposition", "attachment; filename=智能试题_" + timestamp + ".xlsx");

            try (ServletOutputStream outputStream = response.getOutputStream()) {
                workbook.write(outputStream);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillRowWithQuestionData(Row row, JSONObject questionObject, CellStyle cellStyle) {
        // Question Type
        String type = questionObject.getString("type");
        Cell cell = row.createCell(0);
        cell.setCellValue(getQuestionTypeLabel(type));
        cell.setCellStyle(cellStyle);

        // Question Text
        cell = row.createCell(1);
        cell.setCellValue(questionObject.getString("question"));
        cell.setCellStyle(cellStyle);

        // Options
        List<String> items = questionObject.getJSONArray("items").toJavaList(String.class);
        for (int j = 0; j < items.size(); j++) {
            cell = row.createCell(2 + j);
            cell.setCellValue(items.get(j));
            cell.setCellStyle(cellStyle);
        }

        // Set empty cells with style for remaining options
        for (int j = items.size(); j < 8; j++) { // Assuming a maximum of 8 options
            cell = row.createCell(2 + j);
            cell.setCellValue("");
            cell.setCellStyle(cellStyle);
        }

        // Correct Answer
        cell = row.createCell(10);
        cell.setCellValue(questionObject.getString("answers"));
        cell.setCellStyle(cellStyle);

        // Difficulty
        String difficulty = questionObject.getString("difficulty");
        if ("普通".equals(difficulty)) {
            difficulty = "一般";
        }
        cell = row.createCell(11);
        cell.setCellValue(difficulty);
        cell.setCellStyle(cellStyle);

        // Explanation
        cell = row.createCell(12);
        cell.setCellValue(questionObject.getString("explan"));
        cell.setCellStyle(cellStyle);
    }

    private CellStyle createCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        Font font = workbook.createFont();
        font.setFontName("微软雅黑");
        cellStyle.setFont(font);
        return cellStyle;
    }

    private String getQuestionTypeLabel(String type) {
        switch (type) {
            case "single_choice":
                return "\u5355\u9009\u9898"; // "单选题"
            case "multiple_choice":
                return "\u591a\u9009\u9898"; // "多选题"
            case "true_false":
                return "\u5224\u65ad\u9898"; // "判断题"
            case "fill_in_the_blank":
                return "\u586b\u7a7a\u9898"; // "填空题"
            case "short_answer":
                return "\u95ee\u7b54\u9898"; // "问答题"
            default:
                return "\u672a\u77e5\u9898\u578b"; // "未知题型"
        }
    }

}