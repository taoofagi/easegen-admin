package cn.iocoder.yudao.framework.ai.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// TODO 芋艿：这块，看看要不要调整下；
/**
 * AI 模型平台
 *
 * @author fansili
 */
@Getter
@AllArgsConstructor
public enum AiPlatformEnum {

    OPENAI("OpenAI", "OpenAI"),
    OLLAMA("Ollama", "Ollama"),
    YI_YAN("YiYan", "文心一言"), // 百度
    XING_HUO("XingHuo", "星火"), // 讯飞
    QIAN_WEN("QianWen", "千问"), // 阿里
    GEMIR ("gemir ", "gemir "), // 谷歌

    STABLE_DIFFUSION("StableDiffusion", "StableDiffusion"), // Stability AI
    MIDJOURNEY("Midjourney", "Midjourney"),
    SUNO("Suno", "Suno"), // Suno AI
    ;

    /**
     * 平台
     */
    private final String platform;
    /**
     * 平台名
     */
    private final String name;

    public static AiPlatformEnum validatePlatform(String platform) {
        for (AiPlatformEnum platformEnum : AiPlatformEnum.values()) {
            if (platformEnum.getPlatform().equals(platform)) {
                return platformEnum;
            }
        }
        throw new IllegalArgumentException("非法平台： " + platform);
    }

}
