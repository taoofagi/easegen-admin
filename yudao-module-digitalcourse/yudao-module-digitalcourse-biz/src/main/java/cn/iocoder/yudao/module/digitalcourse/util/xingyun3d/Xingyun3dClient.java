package cn.iocoder.yudao.module.digitalcourse.util.xingyun3d;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaMegerVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia.CourseMediaDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursemedia.CourseMediaMapper;
import cn.iocoder.yudao.module.infra.api.config.ConfigApi;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 魔珐星云API客户端
 *
 * @author easegen
 */
@Component
@Slf4j
public class Xingyun3dClient {

    // 配置键常量（只保留必要的配置项）
    private static final String XINGYUN3D_APP_ID = "xingyun3d.app.id";
    private static final String XINGYUN3D_APP_SECRET = "xingyun3d.app.secret";
    private static final String XINGYUN3D_GATEWAY_SERVER = "xingyun3d.gateway.server";

    // 默认值（硬编码在代码中）
    private static final String DEFAULT_GATEWAY_SERVER = "https://nebula-agent.xingyun3d.com";
    private static final int DEFAULT_TIMEOUT = 30000; // 30秒
    private static final int DEFAULT_RETRY_TIMES = 3; // 3次

    @Resource
    private ConfigApi configApi;

    @Resource
    private CourseMediaMapper courseMediaMapper;

    @Resource
    private FileApi fileApi;

    /**
     * 获取App ID
     */
    private String getAppId() {
        String appId = configApi.getConfigValueByKey(XINGYUN3D_APP_ID);
        if (StrUtil.isBlank(appId)) {
            throw new RuntimeException("魔珐星云App ID未配置，请在系统配置中配置 xingyun3d.app.id");
        }
        return appId;
    }

    /**
     * 获取App Secret
     */
    private String getAppSecret() {
        String secret = configApi.getConfigValueByKey(XINGYUN3D_APP_SECRET);
        if (StrUtil.isBlank(secret)) {
            throw new RuntimeException("魔珐星云App Secret未配置，请在系统配置中配置 xingyun3d.app.secret");
        }
        return secret;
    }

    /**
     * 获取网关地址
     */
    private String getGatewayServer() {
        String server = configApi.getConfigValueByKey(XINGYUN3D_GATEWAY_SERVER);
        return StrUtil.isNotBlank(server) ? server : DEFAULT_GATEWAY_SERVER;
    }

    /**
     * 获取超时时间（使用默认值）
     */
    private int getTimeout() {
        return DEFAULT_TIMEOUT;
    }

    /**
     * 获取重试次数（使用默认值）
     */
    private int getRetryTimes() {
        return DEFAULT_RETRY_TIMES;
    }

    /**
     * 构建请求头
     */
    private Map<String, String> buildHeaders(String apiPath, String method, Object data) {
        Map<String, String> headers = new HashMap<>();
        long timestamp = System.currentTimeMillis() / 1000;

        log.info("[DEBUG] 签名计算参数:");
        log.info("[DEBUG]   - apiPath: {}", apiPath);
        log.info("[DEBUG]   - method: {}", method);
        log.info("[DEBUG]   - timestamp: {}", timestamp);
        log.info("[DEBUG]   - appId: {}", getAppId());
        log.info("[DEBUG]   - appSecret: {}", getAppSecret() != null ? getAppSecret().substring(0, Math.min(8, getAppSecret().length())) + "..." : "null");
        log.info("[DEBUG]   - data: {}", JSON.toJSONString(data));

        String token = Xingyun3dSignatureUtil.generateToken(apiPath, method, data, getAppSecret(), timestamp);

        log.info("[DEBUG]   - 生成的token: {}", token);

        headers.put("X-APP-ID", getAppId());
        headers.put("X-TIMESTAMP", String.valueOf(timestamp));
        headers.put("X-TOKEN", token);
        headers.put("Content-Type", "application/json");

        return headers;
    }

    /**
     * 解析PPT文件
     *
     * @param pptFileUrl PPT文件URL（OSS地址）
     * @return parse_ppt_file_name
     */
    public String parsePptFile(String pptFileUrl) {
        if (StrUtil.isBlank(pptFileUrl)) {
            throw new IllegalArgumentException("PPT文件URL不能为空");
        }

        String apiPath = "/user/v1/video_synthesis_task/parse_ppt_file";
        String method = "POST";
        String gatewayServer = getGatewayServer();

        try {
            // 1. 从URL下载PPT文件
            HttpResponse downloadResponse = HttpRequest.get(pptFileUrl)
                    .timeout(getTimeout())
                    .execute();
            
            if (downloadResponse.getStatus() != 200) {
                throw new RuntimeException("下载PPT文件失败，状态码: " + downloadResponse.getStatus() + ", URL: " + pptFileUrl);
            }
            
            byte[] pptBytes = downloadResponse.bodyBytes();
            if (pptBytes == null || pptBytes.length == 0) {
                throw new RuntimeException("下载PPT文件失败，文件数据为空: " + pptFileUrl);
            }

            // 2. 计算签名（文件上传时，X-TOKEN计算不需要加入ppt_file参数）
            Map<String, Object> data = new HashMap<>();
            long timestamp = System.currentTimeMillis() / 1000;
            String token = Xingyun3dSignatureUtil.generateToken(apiPath, method, data, getAppSecret(), timestamp);

            // 3. 构建请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("X-APP-ID", getAppId());
            headers.put("X-TIMESTAMP", String.valueOf(timestamp));
            headers.put("X-TOKEN", token);

            // 4. 创建临时文件
            File tempFile = File.createTempFile("ppt_", ".pptx");
            try {
                FileUtil.writeBytes(pptBytes, tempFile);

                // 5. 发送请求
                HttpResponse response = HttpRequest.post(gatewayServer + apiPath)
                        .headerMap(headers, true)
                        .form("ppt_file", tempFile, "application/vnd.openxmlformats-officedocument.presentationml.presentation")
                        .timeout(getTimeout())
                        .execute();

                String body = response.body();
                log.info("解析PPT文件响应: {}", body);

                if (response.getStatus() != 200) {
                    throw new RuntimeException("解析PPT文件失败，状态码: " + response.getStatus() + ", 响应: " + body);
                }

                JSONObject responseJson = JSON.parseObject(body);
                if (responseJson.getIntValue("error_code") != 0) {
                    String errorReason = responseJson.getString("error_reason");
                    throw new RuntimeException("解析PPT文件失败: " + errorReason);
                }

                JSONObject dataObj = responseJson.getJSONObject("data");
                return dataObj != null ? dataObj.getString("parse_ppt_file_name") : null;

            } finally {
                // 删除临时文件
                if (tempFile.exists()) {
                    tempFile.delete();
                }
            }

        } catch (Exception e) {
            log.error("解析PPT文件失败", e);
            throw new RuntimeException("解析PPT文件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 创建渲染任务（通过segment）
     */
    public Integer createRenderTaskBySegment(CourseMediaDO courseMedia, List<Map<String, Object>> segments) {
        String apiPath = "/user/v1/video_synthesis_task/create_render_task";
        String method = "POST";
        String gatewayServer = getGatewayServer();

        try {
            // 1. 构建请求数据
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("look_name", courseMedia.getLookName());
            requestData.put("tts_vcn_name", courseMedia.getTtsVcnName());
            requestData.put("studio_name", courseMedia.getStudioName());
            requestData.put("sub_title", StrUtil.isNotBlank(courseMedia.getSubTitle()) ? courseMedia.getSubTitle() : "on");
            requestData.put("if_aigc_mark", courseMedia.getIfAigcMark() != null && courseMedia.getIfAigcMark() == 1);
            if (StrUtil.isNotBlank(courseMedia.getName())) {
                requestData.put("video_name", courseMedia.getName());
            }
            if (segments != null && !segments.isEmpty()) {
                requestData.put("segment", segments);
            }

            // 2. 构建请求头
            Map<String, String> headers = buildHeaders(apiPath, method, requestData);

            // 3. 发送请求
            int maxRetries = getRetryTimes();
            int retryCount = 0;
            while (retryCount < maxRetries) {
                try {
                    HttpResponse response = HttpRequest.post(gatewayServer + apiPath)
                            .headerMap(headers, true)
                            .body(JSON.toJSONString(requestData))
                            .timeout(getTimeout())
                            .execute();

                    String body = response.body();
                    log.info("创建渲染任务响应: {}", body);

                    if (response.getStatus() != 200) {
                        retryCount++;
                        if (retryCount >= maxRetries) {
                            throw new RuntimeException("创建渲染任务失败，状态码: " + response.getStatus() + ", 响应: " + body);
                        }
                        TimeUnit.SECONDS.sleep(2);
                        continue;
                    }

                    JSONObject responseJson = JSON.parseObject(body);
                    if (responseJson.getIntValue("error_code") != 0) {
                        String errorReason = responseJson.getString("error_reason");
                        throw new RuntimeException("创建渲染任务失败: " + errorReason);
                    }

                    JSONObject dataObj = responseJson.getJSONObject("data");
                    if (dataObj == null) {
                        throw new RuntimeException("创建渲染任务失败，响应数据为空");
                    }

                    Integer taskId = dataObj.getInteger("task_id");
                    if (taskId == null) {
                        throw new RuntimeException("创建渲染任务失败，task_id为空");
                    }

                    return taskId;

                } catch (Exception e) {
                    retryCount++;
                    if (retryCount >= maxRetries) {
                        throw e;
                    }
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("请求被中断", ie);
                    }
                }
            }

            throw new RuntimeException("创建渲染任务失败，重试次数用尽");

        } catch (Exception e) {
            log.error("创建渲染任务失败", e);
            throw new RuntimeException("创建渲染任务失败: " + e.getMessage(), e);
        }
    }

    /**
     * 创建渲染任务（通过PPT）
     */
    public Integer createRenderTaskByPpt(CourseMediaDO courseMedia, String parsePptFileName) {
        String apiPath = "/user/v1/video_synthesis_task/create_render_task";
        String method = "POST";
        String gatewayServer = getGatewayServer();

        try {
            // 1. 构建请求数据
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("look_name", courseMedia.getLookName());
            requestData.put("tts_vcn_name", courseMedia.getTtsVcnName());
            requestData.put("studio_name", courseMedia.getStudioName());
            requestData.put("sub_title", StrUtil.isNotBlank(courseMedia.getSubTitle()) ? courseMedia.getSubTitle() : "on");
            requestData.put("if_aigc_mark", courseMedia.getIfAigcMark() != null && courseMedia.getIfAigcMark() == 1);
            requestData.put("parse_ppt_file_name", parsePptFileName);
            if (StrUtil.isNotBlank(courseMedia.getName())) {
                requestData.put("video_name", courseMedia.getName());
            }

            // 2. 构建请求头
            Map<String, String> headers = buildHeaders(apiPath, method, requestData);

            // 3. 发送请求（重试逻辑同segment方式）
            int maxRetries = getRetryTimes();
            int retryCount = 0;
            while (retryCount < maxRetries) {
                try {
                    HttpResponse response = HttpRequest.post(gatewayServer + apiPath)
                            .headerMap(headers, true)
                            .body(JSON.toJSONString(requestData))
                            .timeout(getTimeout())
                            .execute();

                    String body = response.body();
                    log.info("创建渲染任务响应: {}", body);

                    if (response.getStatus() != 200) {
                        retryCount++;
                        if (retryCount >= maxRetries) {
                            throw new RuntimeException("创建渲染任务失败，状态码: " + response.getStatus() + ", 响应: " + body);
                        }
                        TimeUnit.SECONDS.sleep(2);
                        continue;
                    }

                    JSONObject responseJson = JSON.parseObject(body);
                    if (responseJson.getIntValue("error_code") != 0) {
                        String errorReason = responseJson.getString("error_reason");
                        throw new RuntimeException("创建渲染任务失败: " + errorReason);
                    }

                    JSONObject dataObj = responseJson.getJSONObject("data");
                    if (dataObj == null) {
                        throw new RuntimeException("创建渲染任务失败，响应数据为空");
                    }

                    Integer taskId = dataObj.getInteger("task_id");
                    if (taskId == null) {
                        throw new RuntimeException("创建渲染任务失败，task_id为空");
                    }

                    return taskId;

                } catch (Exception e) {
                    retryCount++;
                    if (retryCount >= maxRetries) {
                        throw e;
                    }
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("请求被中断", ie);
                    }
                }
            }

            throw new RuntimeException("创建渲染任务失败，重试次数用尽");

        } catch (Exception e) {
            log.error("创建渲染任务失败", e);
            throw new RuntimeException("创建渲染任务失败: " + e.getMessage(), e);
        }
    }

    /**
     * 查询任务状态
     */
    public void queryRenderTask(CourseMediaDO courseMedia) {
        if (StrUtil.isBlank(courseMedia.getPlatformTaskId())) {
            log.warn("任务ID为空，无法查询任务状态，courseMediaId: {}", courseMedia.getId());
            return;
        }

        String apiPath = "/user/v1/video_synthesis_task/get_render_task";
        String method = "GET";
        String gatewayServer = getGatewayServer();

        try {
            // 1. 构建请求参数
            Map<String, Object> params = new HashMap<>();
            params.put("task_id", courseMedia.getPlatformTaskId());

            // 2. 构建请求头（GET请求，data为空）
            Map<String, String> headers = buildHeaders(apiPath + "?task_id=" + courseMedia.getPlatformTaskId(), method, new HashMap<>());

            // 3. 发送请求
            HttpResponse response = HttpRequest.get(gatewayServer + apiPath)
                    .headerMap(headers, true)
                    .form(params)
                    .timeout(getTimeout())
                    .execute();

            String body = response.body();
            log.debug("查询任务状态响应: {}", body);

            if (response.getStatus() != 200) {
                log.error("查询任务状态失败，状态码: {}, 响应: {}", response.getStatus(), body);
                return;
            }

            JSONObject responseJson = JSON.parseObject(body);
            if (responseJson.getIntValue("error_code") != 0) {
                String errorReason = responseJson.getString("error_reason");
                log.error("查询任务状态失败: {}", errorReason);
                courseMedia.setStatus(3); // 失败
                courseMedia.setErrorReason(errorReason);
                courseMediaMapper.updateById(courseMedia);
                return;
            }

            JSONObject dataObj = responseJson.getJSONObject("data");
            if (dataObj == null) {
                log.warn("查询任务状态响应数据为空");
                return;
            }

            // 4. 更新任务状态
            updateCourseMediaFromResponse(courseMedia, dataObj);

        } catch (Exception e) {
            log.error("查询任务状态失败，courseMediaId: {}", courseMedia.getId(), e);
        }
    }

    /**
     * 从响应更新CourseMediaDO
     */
    private void updateCourseMediaFromResponse(CourseMediaDO courseMedia, JSONObject dataObj) {
        // 更新合成状态
        String synthStatus = dataObj.getString("synth_state");
        courseMedia.setSynthStatus(synthStatus);

        // 更新状态映射：finished -> 2（成功），error -> 3（失败），其他 -> 1（合成中）
        if ("finished".equals(synthStatus)) {
            courseMedia.setStatus(2); // 成功
            // 更新视频URL
            String renderVideoOss = dataObj.getString("render_video_oss");
            if (StrUtil.isNotBlank(renderVideoOss)) {
                // 下载视频并上传到OSS
                try {
                    String ossUrl = downloadAndUploadVideo(renderVideoOss);
                    courseMedia.setPreviewUrl(ossUrl);
                } catch (Exception e) {
                    log.error("下载并上传视频失败", e);
                    // 如果下载失败，直接使用原始URL
                    courseMedia.setPreviewUrl(renderVideoOss);
                }
            }
            // 更新时长
            Long duration = dataObj.getLong("duration");
            if (duration != null) {
                courseMedia.setDuration(duration);
            }
            // 更新完成时间
            String finishTime = dataObj.getString("synth_finish_time");
            if (StrUtil.isNotBlank(finishTime)) {
                courseMedia.setFinishTime(finishTime);
                try {
                    courseMedia.setSynthFinishTime(LocalDateTime.parse(finishTime, DateTimeFormatter.ISO_DATE_TIME));
                } catch (Exception e) {
                    log.warn("解析完成时间失败: {}", finishTime, e);
                }
            }
        } else if ("error".equals(synthStatus) || "cancel".equals(synthStatus)) {
            courseMedia.setStatus(3); // 失败
            String errorReason = dataObj.getString("error_reason");
            if (StrUtil.isNotBlank(errorReason)) {
                courseMedia.setErrorReason(errorReason.length() > 500 ? errorReason.substring(0, 500) : errorReason);
            }
        } else {
            courseMedia.setStatus(1); // 合成中
        }

        // 更新开始时间
        String startTime = dataObj.getString("synth_start_time");
        if (StrUtil.isNotBlank(startTime)) {
            try {
                courseMedia.setSynthStartTime(LocalDateTime.parse(startTime, DateTimeFormatter.ISO_DATE_TIME));
            } catch (Exception e) {
                log.warn("解析开始时间失败: {}", startTime, e);
            }
        }

        // 更新进度（如果有）
        Float progress = dataObj.getFloat("progress");
        if (progress != null) {
            courseMedia.setProgress(progress);
        }

        // 保存更新
        courseMediaMapper.updateById(courseMedia);
    }

    /**
     * 下载视频并上传到OSS
     */
    private String downloadAndUploadVideo(String videoUrl) throws Exception {
        // 1. 下载视频
        HttpResponse response = HttpRequest.get(videoUrl)
                .timeout(getTimeout())
                .execute();

        if (response.getStatus() != 200) {
            throw new RuntimeException("下载视频失败，状态码: " + response.getStatus());
        }

        byte[] videoBytes = response.bodyBytes();
        if (videoBytes == null || videoBytes.length == 0) {
            throw new RuntimeException("下载视频失败，视频数据为空");
        }

        // 2. 上传到OSS
        String fileName = "video_" + System.currentTimeMillis() + ".mp4";
        String path = "video/" + fileName;
        return fileApi.createFile(fileName, path, videoBytes);
    }

    /**
     * 取消任务
     */
    public void cancelRenderTask(Long courseMediaId) {
        CourseMediaDO courseMedia = courseMediaMapper.selectById(courseMediaId);
        if (courseMedia == null || StrUtil.isBlank(courseMedia.getPlatformTaskId())) {
            log.warn("任务不存在或任务ID为空，无法取消任务，courseMediaId: {}", courseMediaId);
            return;
        }

        String apiPath = "/user/v1/video_synthesis_task/cancel_render_task";
        String method = "POST";
        String gatewayServer = getGatewayServer();

        try {
            // 1. 构建请求数据
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("task_id", Long.parseLong(courseMedia.getPlatformTaskId()));

            // 2. 构建请求头
            Map<String, String> headers = buildHeaders(apiPath, method, requestData);

            // 3. 发送请求
            HttpResponse response = HttpRequest.post(gatewayServer + apiPath)
                    .headerMap(headers, true)
                    .body(JSON.toJSONString(requestData))
                    .timeout(getTimeout())
                    .execute();

            String body = response.body();
            log.info("取消任务响应: {}", body);

            if (response.getStatus() != 200) {
                throw new RuntimeException("取消任务失败，状态码: " + response.getStatus() + ", 响应: " + body);
            }

            JSONObject responseJson = JSON.parseObject(body);
            if (responseJson.getIntValue("error_code") != 0) {
                String errorReason = responseJson.getString("error_reason");
                throw new RuntimeException("取消任务失败: " + errorReason);
            }

            // 4. 更新任务状态
            courseMedia.setStatus(3); // 失败
            courseMedia.setSynthStatus("cancel");
            courseMedia.setErrorReason("任务已取消");
            courseMediaMapper.updateById(courseMedia);

        } catch (Exception e) {
            log.error("取消任务失败，courseMediaId: {}", courseMediaId, e);
            throw new RuntimeException("取消任务失败: " + e.getMessage(), e);
        }
    }
}

