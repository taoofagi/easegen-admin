package cn.iocoder.yudao.module.digitalcourse.controller.admin.voices.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoicesTrailVO {

    private String name;
    private String code;
    private String fixAuditionUrl;
    private String language;
    private String gender;
    private String accountId;
}
