package cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo.AppCourseScenesMegerReqVO;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo.AppCourseScenesSaveReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class CourseMediaMegerVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "361")
    private Long id;
    @Schema(description = "视频id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long courseMediaId;
    @Schema(description = "账户ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "361")
    private Integer accountId;

    @Schema(description = "课程名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    private String name;

    @Schema(description = "屏幕比例", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String aspect;

    @Schema(description = "时长（秒）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer duration;

    @Schema(description = "高度（像素）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer height;

    @Schema(description = "宽度（像素）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer width;

    @Schema(description = "是否抠图标识", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer matting;

    @Schema(description = "页面模式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer pageMode;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "2")
    private Integer status;

    @Schema(description = "页面信息", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String pageInfo;

    @Schema(description = "缩略图", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String thumbnail;

    @Schema(description = "字幕样式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String subtitlesStyle;

    private List<AppCourseScenesMegerReqVO> scenes;

    private List<String> ppt;

    @Schema(description = "前端传的预估时间 秒", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long expectedDuration;

    // ========== 3D数字人相关字段 ==========
    @Schema(description = "平台类型：1-2D（easegen），2-3D（魔珐星云）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @com.fasterxml.jackson.annotation.JsonProperty("platformType")
    private Integer platformType;

    @Schema(description = "数字人形象名称（3D使用）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String lookName;

    @Schema(description = "音色名称（3D使用）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String ttsVcnName;

    @Schema(description = "演播室名称（3D使用）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String studioName;

    @Schema(description = "字幕开关：on/off（3D使用）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String subTitle;

    @Schema(description = "是否显示AI生成标识：0-否，1-是（3D使用）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer ifAigcMark;

    @Schema(description = "合成方式：segment-文本片段，ppt-PPT文件", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String synthesisType;

    @Schema(description = "PPT文件URL（ppt方式）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String pptFileUrl;

    @Schema(description = "文本内容（segment方式）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String text;

    @Schema(description = "请求JSON（用于存储额外的请求参数）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String reqJson;

}
