package cn.iocoder.yudao.module.digitalcourse.controller.admin.courses.vo;

import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursescenes.vo.AppCourseScenesSaveReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "用户 APP - 存储课程的基本信息，包括课程名称、时长、状态等新增/修改 Request VO")
@Data
public class AppCoursesUpdateReqVO {

    @Schema(description = "课程ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30853")
    private Long id;

    @Schema(description = "账户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "361")
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

    private List<String> ppt;

    //场景
    private List<AppCourseScenesSaveReqVO> scenes;

}