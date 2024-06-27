package cn.iocoder.yudao.module.ai.controller.admin.music.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - AI 音乐生成 Request VO")
@Data
public class AiSunoGenerateReqVO {

    @Schema(description = "平台", requiredMode = Schema.RequiredMode.REQUIRED, example = "Suno")
    @NotBlank(message = "平台不能为空")
    private String platform; // 参见 AiPlatformEnum 枚举

    /**
     * 1. 描述模式：描述词 + 是否纯音乐 + 模型 TODO @xin：目前貌似描述词没弄对？看着不是 prompt 字段（也可能我弄错了）。可以微信再沟通下哈
     * 2. 歌词模式：歌词 + 音乐风格 + 标题 + 模型 TODO @xin：目前这块少传递了标题；
     */
    @Schema(description = "生成模式", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "生成模式不能为空")
    private Integer generateMode; // 参见 AiMusicGenerateModeEnum 枚举

    @Schema(description = "用于生成音乐音频的提示", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "创作一首带有轻松吉他旋律的流行歌曲，[verse] 描述夏日海滩的宁静，[chorus] 节奏加快，表达对自由的向往。")
    private String prompt;

    @Schema(description = "是否纯音乐", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "true")
    private Boolean makeInstrumental;

    // TODO @xin：看了下这个字段，发现最终还是 model 合适点；因为它其实是模型
    @Schema(description = "模型版本", requiredMode = Schema.RequiredMode.REQUIRED, example = "chirp-v3.5")
    @NotEmpty(message = "模型不能为空")
    private String modelVersion; // 参见 AiModelEnum 枚举

    @Schema(description = "音乐风格", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "[\"pop\",\"jazz\",\"punk\"]")
    private List<String> tags;

    @Schema(description = "音乐/歌曲名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "夜空中最亮的星")
    private String title;

}