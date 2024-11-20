package cn.iocoder.yudao.module.digitalcourse.service.digitalhumans;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.iocoder.yudao.module.digitalcourse.controller.admin.digitalhumans.vo.DigitalHumansTrailVO;
import cn.iocoder.yudao.module.digitalcourse.dal.dataobject.digitalhumans.DigitalHumansDO;
import cn.iocoder.yudao.module.digitalcourse.dal.mysql.digitalhumans.DigitalHumansMapper;
import cn.iocoder.yudao.module.infra.api.config.ConfigApi;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@Validated
public class DigitalHumansServiceUtil {

    private static final String EASEGEN_CORE_URL = "easegen.core.url";

    private static final String EASEGEN_CORE_KEY = "easegen.core.key";

    private static final int ERROR_STATUS = 5;
    private static final int COMPLETE_STATUS = 0;
    private static final int TARIN_STATUS = 3;

    @Resource
    private DigitalHumansMapper digitalHumansMapper;
    @Resource
    private ConfigApi configApi;

    public void remoteTrain(DigitalHumansTrailVO digitalHumansTrailVo){
        //训练前校验


        int maxRetries = 3; // 最大重试次数
        int retryCount = 0;  // 当前重试次数
        boolean success = false;
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        while (retryCount < maxRetries && !success) {
            try {
                // 发送POST请求
                HttpResponse execute = HttpRequest.post(configApi.getConfigValueByKey(EASEGEN_CORE_URL) + "/api/clone_digital_human")
                        .header("X-API-Key", configApi.getConfigValueByKey(EASEGEN_CORE_KEY))
                        .body(mapper.writeValueAsString(digitalHumansTrailVo))
                        .execute();
                String body = execute.body();

                // 检查响应状态码是否成功
                if (execute.getStatus() != 200) {
                    retryCount++;
                    if (retryCount >= maxRetries) {
                        // 超过重试次数，训练失败
                        digitalHumansMapper.update(new UpdateWrapper<DigitalHumansDO>().lambda().eq(DigitalHumansDO::getCode, digitalHumansTrailVo.getCode()).set(DigitalHumansDO::getStatus, ERROR_STATUS));
                        log.error("训练失败：->>>>>>>>>", execute.getStatus());
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
                        digitalHumansMapper.update(new UpdateWrapper<DigitalHumansDO>().lambda().eq(DigitalHumansDO::getCode, digitalHumansTrailVo.getCode()).set(DigitalHumansDO::getStatus, ERROR_STATUS));
                        log.error("训练失败：->>>>>>>>>", errorDetail);
                        return;
                    }
                    continue; // 重新尝试
                }

                // 如果成功，更新状态为1（成功）
//                digitalHumansMapper.update(new UpdateWrapper<DigitalHumansDO>().lambda().eq(DigitalHumansDO::getCode, digitalHumansTrailVo.getCode()).set(DigitalHumansDO::getStatus, 0));
                success = true;
            }catch (Exception e){
                retryCount++;
                if (retryCount >= maxRetries) {
                    // 捕获异常，记录错误原因并更新状态
                    digitalHumansMapper.update(new UpdateWrapper<DigitalHumansDO>().lambda().eq(DigitalHumansDO::getCode, digitalHumansTrailVo.getCode()).set(DigitalHumansDO::getStatus, ERROR_STATUS));
                    log.error("训练失败：->>>>>>>>>", e.getMessage());
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

    public void queryRemoteTrainResult(){
        try {
            List<String> codes = digitalHumansMapper.selectList(new QueryWrapper<DigitalHumansDO>().lambda().eq(DigitalHumansDO::getStatus, TARIN_STATUS))
                    .stream().map(e -> e.getCode()).collect(Collectors.toList());

            if (codes == null || codes.isEmpty()) return;

            // 批量调用远程接口
            String result = HttpRequest.get(configApi.getConfigValueByKey(EASEGEN_CORE_URL) + "/api/clone_digital_human/result")
                    .header("X-API-Key", configApi.getConfigValueByKey(EASEGEN_CORE_KEY))
                    .form("codes", String.join(",", codes))
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
                        .collect(Collectors.toMap(jsonObject -> jsonObject.getString("code"), jsonObject -> jsonObject));

                log.info(JSON.toJSONString(resultMap));
                codes.stream().forEach(e->{
                    JSONObject jsonObject = resultMap.get(e);
                    if (jsonObject != null) {
                        Integer status = jsonObject.getInteger("status");
                        if (status != null) {
                            // 合并状态，0：训练成功，1：未开始，2：训练中，3：训练失败
                            if (status == 0){
                                digitalHumansMapper.update(new UpdateWrapper<DigitalHumansDO>().lambda().set(DigitalHumansDO::getStatus,COMPLETE_STATUS).eq(DigitalHumansDO::getCode,e));
                            } else if (status == 3) {
                                digitalHumansMapper.update(new UpdateWrapper<DigitalHumansDO>().lambda().set(DigitalHumansDO::getStatus,ERROR_STATUS).eq(DigitalHumansDO::getCode,e));
                            }
                        } else {
                            log.error("Status is null for humans code: " + e);
                        }

                    }else {
                        //如果没有匹配的记录，也修改为训练失败
                        digitalHumansMapper.update(new UpdateWrapper<DigitalHumansDO>().lambda().set(DigitalHumansDO::getStatus,ERROR_STATUS).eq(DigitalHumansDO::getCode,e));
                        log.error("No matching result found for humans code: " + e);
                    }
                });

            }else {
                log.error("Invalid JSON array received from the remote API.");
            }
        }catch (Exception e){
            // 捕获所有异常，防止程序崩溃
            log.error("An error occurred while querying remote humans train result: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
