package cn.iocoder.yudao.module.digitalcourse.dal.dataobject.voices;

import lombok.*;

@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditionDO {

    /**
     * 需要转为语音的文本，不可超过50个字
     */
    private String text;

    /**
     * 语速
     */
    private Integer speed;

    /**
     * 音高
     */
    private Integer pitch;

    /**
     * 音量
     */
    private Integer volume;

    /**
     * 声音类型
     */
    private Integer voiceType;

    /**
     * 声音类型Id
     */
    private Integer voiceTypeId;

    /**
     * 声音Id
     */
    private String voiceId;

    /**
     * 智能语速
     */
    private Integer smartSpeed;

}
