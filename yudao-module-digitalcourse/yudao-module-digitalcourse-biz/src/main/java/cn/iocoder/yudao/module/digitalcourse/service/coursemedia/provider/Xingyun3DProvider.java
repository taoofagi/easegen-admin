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

        log.info("[buildSegments] 开始构建segments，reqJson存在: {}, scenes存在: {}",
                StrUtil.isNotBlank(mergeVO.getReqJson()),
                mergeVO.getScenes() != null && !mergeVO.getScenes().isEmpty());

        // 从reqJson中解析segments（如果存在）
        if (StrUtil.isNotBlank(mergeVO.getReqJson())) {
            try {
                com.alibaba.fastjson2.JSONObject reqJson = JSON.parseObject(mergeVO.getReqJson());
                JSONArray segmentsArray = reqJson.getJSONArray("segments");
                if (segmentsArray != null && !segmentsArray.isEmpty()) {
                    log.info("[buildSegments] 从reqJson解析到{}个segment", segmentsArray.size());
                    for (int i = 0; i < segmentsArray.size(); i++) {
                        com.alibaba.fastjson2.JSONObject segment = segmentsArray.getJSONObject(i);
                        Map<String, Object> segmentMap = new HashMap<>();
                        segmentMap.put("text", segment.getString("text"));
                        String mediaUrl = segment.getString("media_url");

                        log.info("[buildSegments] segment[{}]: text={}, media_url={}",
                                i, segment.getString("text"), mediaUrl);

                        // 如果reqJson中的media_url为空，尝试从scenes/components中补充
                        if (StrUtil.isBlank(mediaUrl) && mergeVO.getScenes() != null && i < mergeVO.getScenes().size()) {
                            var scene = mergeVO.getScenes().get(i);
                            log.info("[buildSegments] segment[{}]的media_url为空，尝试从scene[{}]的components中提取", i, i);

                            // 优先从components中查找画中画(category=1)
                            if (scene.getComponents() != null && !scene.getComponents().isEmpty()) {
                                log.info("[buildSegments] scene[{}]有{}个components", i, scene.getComponents().size());
                                for (var component : scene.getComponents()) {
                                    log.info("[buildSegments] component: category={}, src={}",
                                            component.getCategory(), component.getSrc());
                                    if (component.getCategory() != null && component.getCategory() == 1) {
                                        mediaUrl = component.getSrc();
                                        log.info("[buildSegments] 找到画中画! segment[{}]使用components中的URL: {}", i, mediaUrl);
                                        break;
                                    }
                                }
                            } else {
                                log.info("[buildSegments] scene[{}]没有components", i);
                            }

                            // 如果还是没有找到，使用background的src
                            if (StrUtil.isBlank(mediaUrl) && scene.getBackground() != null && scene.getBackground().getSrc() != null) {
                                mediaUrl = scene.getBackground().getSrc();
                                log.info("[buildSegments] segment[{}]使用background的URL: {}", i, mediaUrl);
                            }
                        }

                        if (StrUtil.isNotBlank(mediaUrl)) {
                            segmentMap.put("media_url", mediaUrl);
                        }
                        segments.add(segmentMap);
                    }
                }
            } catch (Exception e) {
                log.error("[buildSegments] 解析reqJson中的segments失败", e);
            }
        }

        // 如果还是没有segments，尝试从scenes中构建（如果存在）
        if (segments.isEmpty() && mergeVO.getScenes() != null && !mergeVO.getScenes().isEmpty()) {
            log.info("[buildSegments] reqJson没有segments，从{}个scenes中构建", mergeVO.getScenes().size());
            // 从scenes中提取文本作为segments
            for (var scene : mergeVO.getScenes()) {
                // 从background中获取pptRemark作为文本
                if (scene.getBackground() != null && scene.getBackground().getPptRemark() != null) {
                    Map<String, Object> segmentMap = new HashMap<>();
                    segmentMap.put("text", scene.getBackground().getPptRemark());

                    // 优先从components中查找画中画(category=1)作为media_url
                    String mediaUrl = null;
                    if (scene.getComponents() != null && !scene.getComponents().isEmpty()) {
                        for (var component : scene.getComponents()) {
                            // category=1表示画中画(PPT)
                            if (component.getCategory() != null && component.getCategory() == 1) {
                                mediaUrl = component.getSrc();
                                log.info("[buildSegments] 场景{}使用画中画URL: {}", scene.getOrderNo(), mediaUrl);
                                break; // 找到第一个画中画即可
                            }
                        }
                    }

                    // 如果没有找到画中画，使用background的src作为fallback
                    if (mediaUrl == null && scene.getBackground().getSrc() != null) {
                        mediaUrl = scene.getBackground().getSrc();
                        log.info("[buildSegments] 场景{}使用背景URL: {}", scene.getOrderNo(), mediaUrl);
                    }

                    // 设置media_url
                    if (mediaUrl != null) {
                        segmentMap.put("media_url", mediaUrl);
                    }

                    segments.add(segmentMap);
                }
            }
        }

        // 如果segments为空，使用默认方式（从courseMedia中获取文本）
        if (segments.isEmpty() && StrUtil.isNotBlank(mergeVO.getText())) {
            log.info("[buildSegments] 使用默认文本构建segment");
            Map<String, Object> segmentMap = new HashMap<>();
            segmentMap.put("text", mergeVO.getText());
            segments.add(segmentMap);
        }

        log.info("[buildSegments] 最终构建了{}个segments", segments.size());
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

