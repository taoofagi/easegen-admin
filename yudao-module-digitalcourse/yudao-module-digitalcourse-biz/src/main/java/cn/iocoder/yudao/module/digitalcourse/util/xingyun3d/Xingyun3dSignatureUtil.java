package cn.iocoder.yudao.module.digitalcourse.util.xingyun3d;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

/**
 * 魔珐星云签名工具
 *
 * @author easegen
 */
@Slf4j
public class Xingyun3dSignatureUtil {

    /**
     * 生成X-TOKEN签名
     *
     * @param apiPath  接口路径（不包含host，如：/user/v1/video_synthesis_task/create_render_task）
     * @param method   HTTP方法（GET、POST等）
     * @param data     请求数据（Map或Object）
     * @param secret   密钥
     * @param timestamp 时间戳（秒级）
     * @return MD5签名值
     */
    public static String generateToken(String apiPath, String method, Object data, String secret, Long timestamp) {
        try {
            // 1. 将api_path转换为全小写形式
            String lowerApiPath = apiPath != null ? apiPath.toLowerCase() : "";

            // 2. 将请求method方法转为小写形式
            String lowerMethod = method != null ? method.toLowerCase() : "";

            // 3. 将data转换为json的字符串形式（排序，无空格）
            String sortJsonStr = "";
            if (data != null) {
                if (data instanceof Map) {
                    // Map类型，转换为排序后的JSON
                    TreeMap<String, Object> sortedMap = new TreeMap<>((Map<String, Object>) data);
                    sortJsonStr = JSON.toJSONString(sortedMap).replace(" ", "");
                } else {
                    // 其他类型，转换为JSON
                    sortJsonStr = JSON.toJSONString(data).replace(" ", "");
                }
            }

            // 4. 按照如下顺序连接字符串：lower_api_path + lower_method + sort_json_str + secret + timestamp
            String signStr = lowerApiPath + lowerMethod + sortJsonStr + secret + timestamp;

            // 5. 将sign以utf8编码，计算md5得到X-TOKEN
            String token = DigestUtil.md5Hex(signStr);

            log.debug("Xingyun3d签名计算 - apiPath: {}, method: {}, data: {}, signStr: {}, token: {}", 
                    apiPath, method, sortJsonStr, signStr, token);

            return token;
        } catch (Exception e) {
            log.error("生成魔珐星云签名失败", e);
            throw new RuntimeException("生成魔珐星云签名失败: " + e.getMessage(), e);
        }
    }

    /**
     * 生成X-TOKEN签名（简化版，自动获取当前时间戳）
     */
    public static String generateToken(String apiPath, String method, Object data, String secret) {
        long timestamp = System.currentTimeMillis() / 1000;
        return generateToken(apiPath, method, data, secret, timestamp);
    }
}

