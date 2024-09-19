package cn.iocoder.yudao.module.digitalcourse.util;

import cn.iocoder.yudao.module.digitalcourse.dal.redis.RedisKeyConstants;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Api
 *
 * @author luozhuowei
 * @date 2024/7/15
 */
@Component
public class DocmeePptApi {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    public static final String BASE_URL = "https://docmee.cn";
    private static final long CACHE_EXPIRE_TIME = 1; // 缓存1小时

    public String createApiToken(String apiKey, String uid, Integer limit) {
        // 1. 生成 Redis 的缓存 key
        String cacheKey = RedisKeyConstants.DIGITALCOURSE_AIPPT_LIMIT + uid;

        // 2. 从 Redis 获取缓存的 token
        String cachedToken = stringRedisTemplate.opsForValue().get(cacheKey);
        if (cachedToken != null) {
            // 如果缓存中有数据，直接返回
            return cachedToken;
        }

        // 3. 缓存没有，则调用接口生成 token
        String url = BASE_URL + "/api/user/createApiToken";
        JSONObject body = new JSONObject();
        body.put("uid", uid);
        body.put("limit", limit);

        HttpUtils.HttpRequest httpRequest = HttpUtils.HttpRequest.postJson(url);
        httpRequest.setBody(body.toJSONString());
        httpRequest.addHeaders("Api-Key", apiKey);

        HttpUtils.HttpResponse response = HttpUtils.request(httpRequest);
        if (response.getStatus() != 200) {
            throw new RuntimeException("创建apiToken失败，httpStatus=" + response.getStatus());
        }

        JSONObject result = response.getResponseToJson();
        if (result.getIntValue("code") != 0) {
            throw new RuntimeException("创建apiToken异常，" + result.getString("message"));
        }

        String token = result.getJSONObject("data").getString("token");

        // 4. 将生成的 token 缓存到 Redis，有效期 1 小时
        stringRedisTemplate.opsForValue().set(cacheKey, token, CACHE_EXPIRE_TIME, TimeUnit.HOURS);

        // 5. 返回 token
        return token;
    }

    public static String parseFileData(String apiToken, File file, String content, String fileUrl) {
        String url = BASE_URL + "/api/ppt/parseFileData";
        HttpUtils.HttpRequest httpRequest = new HttpUtils.HttpRequest();
        httpRequest.setUrl(url);
        httpRequest.setMethod("POST");
        httpRequest.addHeaders("token", apiToken);
        MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
        multipartEntity.setCharset(StandardCharsets.UTF_8);
        if (file != null) {
            multipartEntity.addBinaryBody("file", file);
        }
        if (content != null) {
            multipartEntity.addTextBody("content", content, ContentType.create("text/plain", StandardCharsets.UTF_8));
        }
        if (fileUrl != null) {
            multipartEntity.addTextBody("fileUrl", fileUrl, ContentType.create("text/plain", StandardCharsets.UTF_8));
        }
        httpRequest.setBody(multipartEntity.build());
        HttpUtils.HttpResponse response = HttpUtils.request(httpRequest);
        if (response.getStatus() != 200) {
            throw new RuntimeException("解析文件或内容失败，httpStatus=" + response.getStatus());
        }
        JSONObject result = response.getResponseToJson();
        if (result.getIntValue("code") != 0) {
            throw new RuntimeException("解析文件或内容异常，" + result.getString("message"));
        }
        return result.getJSONObject("data").getString("dataUrl");
    }

    public static String generateOutline(String apiToken, String subject, String dataUrl, String prompt) {
        String url = BASE_URL + "/api/ppt/generateOutline";
        JSONObject body = new JSONObject();
        body.put("subject", subject);
        body.put("dataUrl", dataUrl);
        body.put("prompt", prompt);
        HttpUtils.HttpRequest httpRequest = HttpUtils.HttpRequest.postJson(url);
        httpRequest.setBody(body.toJSONString());
        httpRequest.addHeaders("token", apiToken);
        StringBuilder sb = new StringBuilder();
        HttpUtils.HttpResponse response = HttpUtils.requestWithEventStream(httpRequest, data -> {
            if (data == null || data.isEmpty()) {
                return;
            }
            JSONObject json = JSONObject.parseObject(data);
            if (Objects.equals(json.getInteger("status"), -1)) {
                throw new RuntimeException(json.getString("error"));
            }
            String text = json.getString("text");
            sb.append(text);
            // 打印输出
            System.out.print(text);
        });
        if (response.getStatus() != 200) {
            throw new RuntimeException("生成大纲失败，httpStatus=" + response.getStatus());
        }
        if (response.getHeaders().getOrDefault("Content-Type", response.getHeaders().get("content-type")).contains("application/json")) {
            JSONObject result = response.getResponseToJson();
            throw new RuntimeException("生成大纲失败：" + result.getString("message"));
        }
        return sb.toString();
    }

    public static String generateContent(String apiToken, String outlineMarkdown, String dataUrl, String prompt) {
        String url = BASE_URL + "/api/ppt/generateContent";
        JSONObject body = new JSONObject();
        body.put("outlineMarkdown", outlineMarkdown);
        body.put("dataUrl", dataUrl);
        body.put("prompt", prompt);
        HttpUtils.HttpRequest httpRequest = HttpUtils.HttpRequest.postJson(url);
        httpRequest.setBody(body.toJSONString());
        httpRequest.addHeaders("token", apiToken);
        StringBuilder sb = new StringBuilder();
        HttpUtils.HttpResponse response = HttpUtils.requestWithEventStream(httpRequest, data -> {
            if (data == null || data.isEmpty()) {
                return;
            }
            JSONObject json = JSONObject.parseObject(data);
            if (Objects.equals(json.getInteger("status"), -1)) {
                throw new RuntimeException(json.getString("error"));
            }
            String text = json.getString("text");
            sb.append(text);
            // 打印输出
            System.out.print(text);
        });
        if (response.getStatus() != 200) {
            throw new RuntimeException("生成大纲内容失败，httpStatus=" + response.getStatus());
        }
        if (response.getHeaders().getOrDefault("Content-Type", response.getHeaders().get("content-type")).contains("application/json")) {
            JSONObject result = response.getResponseToJson();
            throw new RuntimeException("生成大纲内容失败：" + result.getString("message"));
        }
        return sb.toString();
    }

    public static String randomOneTemplateId(String apiToken) {
        String url = BASE_URL + "/api/ppt/randomTemplates";
        JSONObject body = new JSONObject();
        body.put("size", 1);
        JSONObject filters = new JSONObject();
        filters.put("type", 1);
        body.put("filters", filters);
        HttpUtils.HttpRequest httpRequest = HttpUtils.HttpRequest.postJson(url);
        httpRequest.setBody(body.toJSONString());
        httpRequest.addHeaders("token", apiToken);
        HttpUtils.HttpResponse response = HttpUtils.request(httpRequest);
        if (response.getStatus() != 200) {
            throw new RuntimeException("获取模板失败，httpStatus=" + response.getStatus());
        }
        JSONObject result = response.getResponseToJson();
        if (result.getIntValue("code") != 0) {
            throw new RuntimeException("获取模板异常，" + result.getString("message"));
        }
        JSONArray data = result.getJSONArray("data");
        JSONObject template = data.getJSONObject(0);
        return template.getString("id");
    }

    public static JSONObject generatePptx(String apiToken, String templateId, String markdown, boolean pptxProperty) {
        String url = BASE_URL + "/api/ppt/generatePptx";
        JSONObject body = new JSONObject();
        body.put("templateId", templateId);
        body.put("outlineContentMarkdown", markdown);
        body.put("pptxProperty", pptxProperty);
        HttpUtils.HttpRequest httpRequest = HttpUtils.HttpRequest.postJson(url);
        httpRequest.setBody(body.toJSONString());
        httpRequest.addHeaders("token", apiToken);
        HttpUtils.HttpResponse response = HttpUtils.request(httpRequest);
        if (response.getStatus() != 200) {
            throw new RuntimeException("生成PPT失败，httpStatus=" + response.getStatus());
        }
        JSONObject result = response.getResponseToJson();
        if (result.getIntValue("code") != 0) {
            throw new RuntimeException("生成PPT异常，" + result.getString("message"));
        }
        return result.getJSONObject("data").getJSONObject("pptInfo");
    }

    public static String downloadPptx(String apiToken, String id) {
        String url = BASE_URL + "/api/ppt/downloadPptx";
        JSONObject body = new JSONObject();
        body.put("id", id);
        HttpUtils.HttpRequest httpRequest = HttpUtils.HttpRequest.postJson(url);
        httpRequest.setBody(body.toJSONString());
        httpRequest.addHeaders("token", apiToken);
        HttpUtils.HttpResponse response = HttpUtils.request(httpRequest);
        if (response.getStatus() != 200) {
            throw new RuntimeException("下载PPT失败，httpStatus=" + response.getStatus());
        }
        JSONObject result = response.getResponseToJson();
        if (result.getIntValue("code") != 0) {
            throw new RuntimeException("下载PPT异常，" + result.getString("message"));
        }
        return result.getJSONObject("data").getString("fileUrl");
    }

    public static JSONObject directGeneratePptx(String apiToken, boolean stream, String templateId, String subject, String dataUrl, String prompt, boolean pptxProperty) {
        String url = BASE_URL + "/api/ppt/directGeneratePptx";
        JSONObject body = new JSONObject();
        body.put("stream", stream);
        body.put("templateId", templateId);
        body.put("subject", subject);
        body.put("dataUrl", dataUrl);
        body.put("prompt", prompt);
        body.put("pptxProperty", pptxProperty);
        HttpUtils.HttpRequest httpRequest = HttpUtils.HttpRequest.postJson(url);
        httpRequest.setBody(body.toJSONString());
        httpRequest.addHeaders("token", apiToken);
        if (stream) {
            // 流式生成
            JSONObject[] pptInfo = new JSONObject[1];
            HttpUtils.HttpResponse response = HttpUtils.requestWithEventStream(httpRequest, data -> {
                if (data == null || data.isEmpty()) {
                    return;
                }
                JSONObject json = JSONObject.parseObject(data);
                if (Objects.equals(json.getInteger("status"), -1)) {
                    throw new RuntimeException(json.getString("error"));
                }
                if (Objects.equals(json.getInteger("status"), 4) && json.containsKey("result")) {
                    pptInfo[0] = json.getJSONObject("result");
                }
                String text = json.getString("text");
                // 打印输出
                System.out.print(text);
            });
            if (response.getStatus() != 200) {
                throw new RuntimeException("生成PPT失败，httpStatus=" + response.getStatus());
            }
            if (response.getHeaders().getOrDefault("Content-Type", response.getHeaders().get("content-type")).contains("application/json")) {
                JSONObject result = response.getResponseToJson();
                throw new RuntimeException("生成PPT失败：" + result.getString("message"));
            }
            return pptInfo[0];
        } else {
            // 非流式生成
            HttpUtils.HttpResponse response = HttpUtils.request(httpRequest);
            if (response.getStatus() != 200) {
                throw new RuntimeException("生成PPT失败，httpStatus=" + response.getStatus());
            }
            JSONObject result = response.getResponseToJson();
            if (result.getIntValue("code") != 0) {
                throw new RuntimeException("生成PPT异常，" + result.getString("message"));
            }
            return result.getJSONObject("data").getJSONObject("pptInfo");
        }
    }

}
