package cn.iocoder.yudao.module.digitalcourse.service.coursemedia.provider;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.coursemedia.vo.CourseMediaMegerVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.coursemedia.CourseMediaDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.coursemedia.CourseMediaMapper;
import cn.iocoder.yudao.module.digitalcourse.util.xingyun3d.Xingyun3dClient;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 魔珐星云 3D数字人提供者
 *
 * @author easegen
 */
@Component
@Slf4j
public class Xingyun3DProvider implements VideoSynthesisProvider {

    @Resource
    private Xingyun3dClient xingyun3dClient;

    @Resource
    private CourseMediaMapper courseMediaMapper;

    @Override
    public Integer getPlatformType() {
        return 2; // 3D
    }

    @Override
    public void createSynthesisTask(CourseMediaDO courseMedia, CourseMediaMegerVO mergeVO) {
        log.info("[DEBUG] 创建3D数字人视频合成任务，courseMediaId: {}, 当前platformType: {}", courseMedia.getId(), courseMedia.getPlatformType());

        try {
            Integer taskId;

            // 判断是segment方式还是PPT方式
            String synthesisType = mergeVO.getSynthesisType();
            if ("ppt".equalsIgnoreCase(synthesisType) && StrUtil.isNotBlank(mergeVO.getPptFileUrl())) {
                // PPT方式：先解析PPT，再创建任务
                log.info("[DEBUG] 使用PPT方式创建3D数字人视频合成任务");
                String parsePptFileName = xingyun3dClient.parsePptFile(mergeVO.getPptFileUrl());
                courseMedia.setParsePptFileName(parsePptFileName);
                taskId = xingyun3dClient.createRenderTaskByPpt(courseMedia, parsePptFileName);
            } else {
                // segment方式：直接创建任务
                log.info("[DEBUG] 使用segment方式创建3D数字人视频合成任务");
                List<Map<String, Object>> segments = buildSegments(mergeVO);
                taskId = xingyun3dClient.createRenderTaskBySegment(courseMedia, segments);
            }

            // 更新任务信息
            log.info("[DEBUG] 准备更新任务信息，设置platformTaskId: {}, platformType: 2", taskId);
            courseMedia.setPlatformTaskId(String.valueOf(taskId));
            courseMedia.setPlatformType(2); // 3D
            courseMedia.setStatus(1); // 合成中
            courseMedia.setSynthStatus("waiting"); // 等待处理
            courseMedia.setSynthStartTime(java.time.LocalDateTime.now());
            log.info("[DEBUG] 调用updateById之前，courseMedia.platformType: {}", courseMedia.getPlatformType());
            courseMediaMapper.updateById(courseMedia);
            log.info("[DEBUG] 调用updateById之后，重新查询platformType");

            log.info("3D数字人视频合成任务创建成功，courseMediaId: {}, taskId: {}", courseMedia.getId(), taskId);

        } catch (Exception e) {
            log.error("创建3D数字人视频合成任务失败，courseMediaId: {}", courseMedia.getId(), e);
            // 更新任务状态为失败
            courseMedia.setStatus(3); // 失败
            courseMedia.setSynthStatus("error");
            courseMedia.setErrorReason(e.getMessage() != null && e.getMessage().length() > 500
                    ? e.getMessage().substring(0, 500)
                    : e.getMessage());
            courseMediaMapper.updateById(courseMedia);
            throw new RuntimeException("创建3D数字人视频合成任务失败: " + e.getMessage(), e);
        }
    }

    /**
     * 构建segments列表
     */
    private List<Map<String, Object>> buildSegments(CourseMediaMegerVO mergeVO) {
        List<Map<String, Object>> segments = new ArrayList<>();

            // 从reqJson中解析segments（如果存在）
            if (StrUtil.isNotBlank(mergeVO.getReqJson())) {
                try {
                    com.alibaba.fastjson2.JSONObject reqJson = JSON.parseObject(mergeVO.getReqJson());
                    JSONArray segmentsArray = reqJson.getJSONArray("segments");
                    if (segmentsArray != null && !segmentsArray.isEmpty()) {
                        for (int i = 0; i < segmentsArray.size(); i++) {
                            com.alibaba.fastjson2.JSONObject segment = segmentsArray.getJSONObject(i);
                            Map<String, Object> segmentMap = new HashMap<>();
                            segmentMap.put("text", segment.getString("text"));
                            if (segment.containsKey("media_url")) {
                                segmentMap.put("media_url", segment.getString("media_url"));
                            }
                            segments.add(segmentMap);
                        }
                    }
                } catch (Exception e) {
                    log.warn("解析reqJson中的segments失败，将使用默认方式", e);
                }
            }
            
            // 如果还是没有segments，尝试从scenes中构建（如果存在）
            if (segments.isEmpty() && mergeVO.getScenes() != null && !mergeVO.getScenes().isEmpty()) {
                // 从scenes中提取文本作为segments
                for (var scene : mergeVO.getScenes()) {
                    // 从background中获取pptRemark作为文本
                    if (scene.getBackground() != null && scene.getBackground().getPptRemark() != null) {
                        Map<String, Object> segmentMap = new HashMap<>();
                        segmentMap.put("text", scene.getBackground().getPptRemark());
                        // 从background中获取src作为media_url
                        if (scene.getBackground().getSrc() != null) {
                            segmentMap.put("media_url", scene.getBackground().getSrc());
                        }
                        segments.add(segmentMap);
                    }
                }
            }

        // 如果segments为空，使用默认方式（从courseMedia中获取文本）
        if (segments.isEmpty() && StrUtil.isNotBlank(mergeVO.getText())) {
            Map<String, Object> segmentMap = new HashMap<>();
            segmentMap.put("text", mergeVO.getText());
            segments.add(segmentMap);
        }

        return segments;
    }

    @Override
    public void queryTaskStatus(CourseMediaDO courseMedia) {
        log.debug("查询3D数字人视频合成任务状态，courseMediaId: {}", courseMedia.getId());
        try {
            xingyun3dClient.queryRenderTask(courseMedia);
        } catch (Exception e) {
            log.error("查询3D数字人视频合成任务状态失败，courseMediaId: {}", courseMedia.getId(), e);
        }
    }

    @Override
    public void cancelTask(Long courseMediaId) {
        log.info("取消3D数字人视频合成任务，courseMediaId: {}", courseMediaId);
        try {
            xingyun3dClient.cancelRenderTask(courseMediaId);
        } catch (Exception e) {
            log.error("取消3D数字人视频合成任务失败，courseMediaId: {}", courseMediaId, e);
            throw new RuntimeException("取消3D数字人视频合成任务失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Integer platformType) {
        return platformType != null && platformType == 2;
    }
}

