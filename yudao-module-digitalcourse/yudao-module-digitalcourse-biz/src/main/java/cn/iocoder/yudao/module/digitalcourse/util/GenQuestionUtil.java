package cn.iocoder.yudao.module.digitalcourse.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.ServletOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class GenQuestionUtil {

    public Workbook genExcelByJson(JSONArray questionsArray) throws IOException {
        InputStream fis = getClass().getClassLoader().getResourceAsStream("template/题目导入模板.xlsx");
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        int rowIndex = 2; // Start writing questions from row 1, since row 0 is the header

        CellStyle cellStyle = createCellStyle(workbook);

        for (int i = 0; i < questionsArray.size(); i++) {
            JSONObject questionObject = questionsArray.getJSONObject(i);
            Row row = sheet.createRow(rowIndex++);
            fillRowWithQuestionData(row, questionObject, cellStyle);
        }

        return workbook;
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
