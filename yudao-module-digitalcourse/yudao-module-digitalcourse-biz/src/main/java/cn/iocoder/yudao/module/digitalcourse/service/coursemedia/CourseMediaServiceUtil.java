package cn.iocoder.yudao.module.digitalcourse.service.coursemedia;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.iocoder.yudao.framework.mybatis.core.query.QueryWrapperX;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaMegerVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia.CourseMediaDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursemedia.CourseMediaMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

    private static final String REMOTE_BASE_URL = "http://digitalcourse.taoofagi.com:7860";

    @Resource
    private CourseMediaMapper courseMediaMapper;

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
                HttpResponse execute = HttpRequest.post(REMOTE_BASE_URL + "/api/mergemedia")
                        .header("X-API-Key", "taoofagi")
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
            String result = HttpRequest.get(REMOTE_BASE_URL + "/api/mergemedia/result")
                    .header("X-API-Key", "taoofagi")
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
                            if (status.intValue() == 2) {
                                e.setStatus(status.intValue());
                                // 完成时间为当前时间
                                e.setFinishTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                                e.setPreviewUrl(jsonObject.getString("merge_video"));
                                e.setDuration(jsonObject.getLong("duration"));
                                courseMediaMapper.updateById(e);
                            } else if (status.intValue() == 3) {
                                e.setStatus(status.intValue());
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
