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
     * 手动将字符串中的非ASCII字符转义为Unicode序列
     * 例如：将"中文"转换为unicode转义格式
     */
    private static String escapeNonAscii(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c > 127) {
                // 非ASCII字符，转义为unicode格式（反斜杠u加四位十六进制）
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

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

            // 3. 将data转换为json的字符串形式（深度排序所有key，无空格）
            // 使用MapSortField递归排序所有Map的key（包括嵌套Map）
            String sortJsonStr = "";
            if (data != null) {
                // 先使用fastjson2序列化（排序key）
                String jsonStr = JSON.toJSONString(data,
                    com.alibaba.fastjson2.JSONWriter.Feature.MapSortField
                ).replace(" ", "");

                // 然后手动转义所有非ASCII字符为Unicode序列
                // 这样确保与Python的json.dumps(data, sort_keys=True, ensure_ascii=True)完全一致
                sortJsonStr = escapeNonAscii(jsonStr);
            }

            // 4. 按照如下顺序连接字符串：lower_api_path + lower_method + sort_json_str + secret + timestamp
            String signStr = lowerApiPath + lowerMethod + sortJsonStr + secret + timestamp;

            // 5. 将sign以utf8编码，计算md5得到X-TOKEN
            String token = DigestUtil.md5Hex(signStr);

            log.debug("魔珐星云签名生成: apiPath={}, method={}, timestamp={}, token={}",
                apiPath, method, timestamp, token);

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

