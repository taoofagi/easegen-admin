package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.voices;

import lombok.*;

@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TTSDTO {

    /**
     * 用户Id
     */
    private String user_id;
    /**
     * 请求id，自行生成uuid
     */
    private String request_id;
    /**
     * 模型编码
     */
    private String model_code;
    /**
     * 需要转为语音的文本
     */
    private String sentence;
    /**
     * 声音类型 (0: 标准, 1: 定制)
     */
    private String voice_type;

    private Float rate;
    private Float pitch;
    private Float volume;



}
