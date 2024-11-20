package cn.iocoder.yudao.module.digitalcourse.controller.admin.digitalhumans.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DigitalHumansTrailVO {

    private String name;
    private String code;
    private String fixVideoUrl;
    private String fixPictureUrl;
    private String useModel;
    private String accountId;

}
