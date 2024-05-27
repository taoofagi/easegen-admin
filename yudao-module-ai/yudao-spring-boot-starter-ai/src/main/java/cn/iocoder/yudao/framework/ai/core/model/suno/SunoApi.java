package cn.iocoder.yudao.framework.ai.core.model.suno;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author xiaoxin
 * @Date 2024/5/27
 */
@Slf4j
public class SunoApi {

    public static final String APPLICATION_JSON = "application/json";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String API_URL = "https://api.acedata.cloud/suno/audios";
    public static final String TEST_TOKEN = "13f13540dd3f4ae9885f63ac9f5d0b9f";
    private static final int READ_TIMEOUT = 160; // 连接超时时间（秒），音乐生成时间较长，设置为 160s，后续可做callback
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public SunoApi() {
        this.client = new OkHttpClient().newBuilder().readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).build();
        this.objectMapper = new ObjectMapper();
    }

    public SunoResponse generateMusic(SunoRequest sunoRequest) throws IOException {
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", TOKEN_PREFIX + TEST_TOKEN)
                .post(RequestBody.create(MediaType.parse(APPLICATION_JSON), objectMapper.writeValueAsString(sunoRequest)))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("suno调用失败! response: {}", response);
                throw new IllegalStateException("suno调用失败!" + response);
            }
            return objectMapper.readValue(response.body().string(), SunoResponse.class);
        }
    }


    /**
     * 请求数据对象，用于生成音乐音频
     */
    @Data
    @Accessors(chain = true)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class SunoRequest {
        /**
         * 用于生成音乐音频的提示
         */
        private String prompt;

        /**
         * 用于生成音乐音频的歌词
         */
        private String lyric;

        /**
         * 指示音乐音频是否为定制，如果为 true，则从歌词生成，否则从提示生成
         */
        private boolean custom;

        /**
         * 音乐音频的标题
         */
        private String title;

        /**
         * 音乐音频的风格
         */
        private String style;

        /**
         * 音乐音频生成后回调的 URL
         */
        private String callbackUrl;
    }


    /**
     * API 响应的数据
     */
    @Data
    public static class SunoResponse {
        /**
         * 表示请求是否成功
         */
        private boolean success;

        /**
         * 任务 ID
         */
        @JsonProperty("task_id")
        private String taskId;

        /**
         * 音乐数据列表
         */
        private List<MusicData> data;

        /**
         * 表示单个音乐数据的类
         */
        @Data
        static class MusicData {
            /**
             * 音乐数据的 ID
             */
            private String id;

            /**
             * 音乐音频的标题
             */
            private String title;

            /**
             * 音乐音频的图片 URL
             */
            @JsonProperty("image_url")
            private String imageUrl;

            /**
             * 音乐音频的歌词
             */
            private String lyric;

            /**
             * 音乐音频的 URL
             */
            @JsonProperty("audio_url")
            private String audioUrl;

            /**
             * 音乐视频的 URL
             */
            @JsonProperty("video_url")
            private String videoUrl;

            /**
             * 音乐音频的创建时间
             */
            @JsonProperty("created_at")
            private String createdAt;

            /**
             * 使用的模型名称
             */
            private String model;

            /**
             * 生成音乐音频的提示
             */
            private String prompt;

            /**
             * 音乐音频的风格
             */
            private String style;
        }
    }


}
