package cn.iocoder.yudao.module.digitalcourse.service.coursemedia;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaMegerVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia.CourseMediaDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursemedia.CourseMediaMapper;
import cn.iocoder.yudao.module.digitalcourse.util.SrtToVttUtil;
import cn.iocoder.yudao.module.infra.api.config.ConfigApi;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Validated
@Slf4j
public class CourseMediaServiceUtil {

    private static final String EASEGEN_CORE_URL = "easegen.core.url";

    static final String EASEGEN_CORE_KEY = "easegen.core.key";

    @Resource
    private CourseMediaMapper courseMediaMapper;

    @Resource
    private ConfigApi configApi;

    @Resource
    private SrtToVttUtil srtToVttUtil;

    /**
     * 远程合并视频
     * @param updateReqVO
     * @return
     */
    @Async
    public void remoteMegerMedia(CourseMediaMegerVO updateReqVO) {
        CourseMediaDO courseMediaDO = courseMediaMapper.selectById(updateReqVO.getCourseMediaId());
        if (courseMediaDO == null) {
            // 如果找不到对应的课程媒体记录，直接返回或记录错误日志
            return;
        }

        int maxRetries = 3; // 最大重试次数
        int retryCount = 0;  // 当前重试次数
        boolean success = false;

        while (retryCount < maxRetries && !success) {
            try {
                // 发送POST请求
                HttpResponse execute = HttpRequest.post(configApi.getConfigValueByKey(EASEGEN_CORE_URL) + "/api/mergemedia")
                        .header("X-API-Key", configApi.getConfigValueByKey(EASEGEN_CORE_KEY))
                        .body(JSON.toJSONString(updateReqVO))
                        .execute();
                String body = execute.body();

                // 检查响应状态码是否成功
                if (execute.getStatus() != 200) {
                    retryCount++;
                    if (retryCount >= maxRetries) {
                        // 超过重试次数，更新状态和错误信息
                        courseMediaDO.setStatus(3); // 3 表示合成失败
                        courseMediaDO.setErrorReason(truncateErrorMsg("HTTP 请求报错: " + execute.getStatus()));
                        courseMediaMapper.updateById(courseMediaDO);
                        return;
                    }
                    continue; // 重新尝试
                }

                // 解析响应，检查是否有错误信息
                JSONObject responseJson = JSON.parseObject(body);
                if (!responseJson.getBoolean("success")) {
                    // 处理业务逻辑错误，更新状态和错误信息
                    String errorDetail = responseJson.getString("detail");
                    retryCount++;
                    if (retryCount >= maxRetries) {
                        courseMediaDO.setStatus(3); // 3 表示合成失败
                        courseMediaDO.setErrorReason(truncateErrorMsg("API 接口异常: " + errorDetail));
                        courseMediaMapper.updateById(courseMediaDO);
                        return;
                    }
                    continue; // 重新尝试
                }

                // 如果成功，更新状态为1（成功）
                courseMediaDO.setStatus(1); // 1 表示合成成功
                courseMediaMapper.updateById(courseMediaDO);
                success = true;

            } catch (Exception e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    // 捕获异常，记录错误原因并更新状态
                    courseMediaDO.setStatus(3); // 3 表示合成失败
                    courseMediaDO.setErrorReason(truncateErrorMsg("视频合成任务失败，请联系管理员，错误信息: " + e.getMessage()));
                    courseMediaMapper.updateById(courseMediaDO);
                    return;
                }

                try {
                    // 重试前等待一段时间，避免频繁请求
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // 处理中断异常
                    break;
                }
            }
        }
    }

    public Boolean reMegerMedia(CourseMediaDO courseMediaDO) {
        if (courseMediaDO == null) {
            // 如果找不到对应的课程媒体记录，直接返回或记录错误日志
            return false;
        }
        JSONObject reqJson = new JSONObject();
        reqJson.put("courseMediaId", courseMediaDO.getId());

        int maxRetries = 3; // 最大重试次数
        int retryCount = 0;  // 当前重试次数
        boolean success = false;

        while (retryCount < maxRetries && !success) {
            try {
                // 发送POST请求
                HttpResponse execute = HttpRequest.post(configApi.getConfigValueByKey(EASEGEN_CORE_URL) + "/api/reMergemedia")
                        .header("X-API-Key", configApi.getConfigValueByKey(EASEGEN_CORE_KEY))
                        .body(JSON.toJSONString(reqJson))
                        .execute();
                String body = execute.body();

                // 检查响应状态码是否成功
                if (execute.getStatus() != 200) {
                    retryCount++;
                    if (retryCount >= maxRetries) {
                        // 超过重试次数，更新状态和错误信息
                        courseMediaDO.setStatus(3); // 3 表示合成失败
                        courseMediaDO.setErrorReason(truncateErrorMsg("HTTP 请求报错: " + execute.getStatus()+", 报错内容: " + body));
                        courseMediaMapper.updateById(courseMediaDO);
                        return false;
                    }
                    continue; // 重新尝试
                }

                // 解析响应，检查是否有错误信息
                JSONObject responseJson = JSON.parseObject(body);
                if (!responseJson.getBoolean("success")) {
                    // 处理业务逻辑错误，更新状态和错误信息
                    String errorDetail = responseJson.getString("detail");
                    retryCount++;
                    if (retryCount >= maxRetries) {
                        courseMediaDO.setStatus(3); // 3 表示合成失败
                        courseMediaDO.setErrorReason(truncateErrorMsg("API 接口异常: " + errorDetail));
                        courseMediaMapper.updateById(courseMediaDO);
                        return false;
                    }
                    continue; // 重新尝试
                }

                // 如果成功，更新状态为1（成功）
                courseMediaDO.setErrorReason("");
                courseMediaDO.setStatus(1); // 1 表示请求成功，状态变为合成中
                courseMediaMapper.updateById(courseMediaDO);
                success = true;

            } catch (Exception e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    // 捕获异常，记录错误原因并更新状态
                    courseMediaDO.setStatus(3); // 3 表示合成失败
                    courseMediaDO.setErrorReason(truncateErrorMsg("视频合成任务失败，请联系管理员，错误信息: " + e.getMessage()));
                    courseMediaMapper.updateById(courseMediaDO);
                    return false;
                }

                try {
                    // 重试前等待一段时间，避免频繁请求
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // 处理中断异常
                    break;
                }
            }
        }
        return success;
    }

    /**
     * 截取错误信息，使其不超过指定的最大长度
     */
    private String truncateErrorMsg(String errorMsg) {
        int maxLength = 500;
        return errorMsg.length() > maxLength ? errorMsg.substring(0, maxLength) : errorMsg;
    }

    /**
     * 远程查询合并结果（定时任务）
     */

    public void queryRemoteMegerResult() {
        try {
            List<CourseMediaDO> courseMediaDOS = courseMediaMapper.selectList(new QueryWrapperX<CourseMediaDO>().lambda().eq(CourseMediaDO::getStatus, 1));

            // 检查 courseMediaDOS 是否为空
            if (courseMediaDOS == null || courseMediaDOS.isEmpty()) {
                return; // 如果为空，直接返回
            }

            // 收集所有的ID
            String courseMediaIds = courseMediaDOS.stream()
                    .map(e -> String.valueOf(e.getId()))
                    .collect(Collectors.joining(","));

            // 批量调用远程接口
            String result = HttpRequest.get(configApi.getConfigValueByKey(EASEGEN_CORE_URL) + "/api/mergemedia/result")
                    .header("X-API-Key", configApi.getConfigValueByKey(EASEGEN_CORE_KEY))
                    .form("courseMediaIds", courseMediaIds)
                    .timeout(5000)  // 设置超时时间
                    .execute()
                    .body();

            // 检查远程接口返回的结果是否有效
            if (result == null || result.isEmpty()) {
                System.err.println("Remote API returned empty or null response.");
                return;
            }
            //打印结果
            log.info("Remote API returned: " + result);
            if (JSON.isValidArray(result)) {
                JSONArray jsonArray = JSON.parseArray(result);
                Map<String, JSONObject> resultMap = jsonArray.stream()
                        .filter(obj -> JSON.isValidObject(JSON.toJSONString(obj)))
                        .map(obj -> JSON.parseObject(JSON.toJSONString(obj)))
                        .collect(Collectors.toMap(jsonObject -> jsonObject.getString("courseMediaId"), jsonObject -> jsonObject));

                courseMediaDOS.forEach(e -> {
                    JSONObject jsonObject = resultMap.get(String.valueOf(e.getId()));
                    if (jsonObject != null) {
                        BigInteger status = jsonObject.getBigInteger("status");
                        if (status != null) {
                            // 合并状态，0：草稿，1：合成中，2：合成成功，3：合成失败
                            // 状态为合成中，更新进度
                            if (status.intValue() == 1) { // 合成中
                                Float completionPercentage = jsonObject.getFloat("completion_percentage");
                                e.setProgress(completionPercentage); // 更新进度
                                e.setErrorReason(""); // 清空错误信息
                                courseMediaMapper.updateById(e);
                                log.info("合成中，已更新进度：" + completionPercentage + "%，课程ID: " + e.getId());
                            } else if (status.intValue() == 2) {
                                e.setStatus(status.intValue());
                                e.setFinishTime(jsonObject.getString("finish_time")); // 远程返回的完成时间
                                e.setPreviewUrl(jsonObject.getString("merge_video"));
                                e.setDuration(jsonObject.getLong("duration"));
                                e.setProgress(jsonObject.getFloat("completion_percentage")); // 合成进度
                                e.setSubtitlesUrl(jsonObject.getString("subtitles_url"));
                                e.setThumbnail(jsonObject.getString("thumbnail"));
                                try {
                                    String vtturl = srtToVttUtil.convertAndUploadSrtToVtt(jsonObject.getString("subtitles_url"));
                                    e.setSubtitlesVttUrl(vtturl);
                                } catch (IOException ex) {
                                    log.info("Failed to convert and upload SRT to VTT: " + ex.getMessage());
                                    throw new RuntimeException(ex);
                                }
                                e.setErrorReason(""); // 清空错误信息
                                courseMediaMapper.updateById(e);
                            } else if (status.intValue() == 3) {
                                e.setStatus(status.intValue());
                                String failureReasons = jsonObject.getString("failure_reasons");
                                if (StrUtil.isNotEmpty(failureReasons) && failureReasons.length() > 1000) {
                                    failureReasons = failureReasons.substring(0, 1000); // 截取字符串，确保不超过1000字符
                                }
                                e.setErrorReason(failureReasons); // 获取失败原因
                                e.setProgress(jsonObject.getFloat("completion_percentage")); // 合成进度
                                courseMediaMapper.updateById(e);
                            }
                        } else {
                            log.error("Status is null for courseMediaId: " + e.getId());
                        }
                    } else {
                        //如果没有匹配的记录，也修改为生成失败
                        e.setStatus(3);
                        e.setErrorReason("服务端没有查询到视频合成记录，请重新合成");
                        courseMediaMapper.updateById(e);
                        log.error("No matching result found for courseMediaId: " + e.getId());
                    }
                });
            } else {
                log.error("Invalid JSON array received from the remote API.");
            }
        } catch (Exception ex) {
            // 捕获所有异常，防止程序崩溃
            log.error("An error occurred while querying remote merge result: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
