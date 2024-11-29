package cn.iocoder.yudao.module.digitalcourse.apiImpl;

import cn.iocoder.yudao.module.digitalcourse.api.DocmeePptUtilApi;
import cn.iocoder.yudao.module.digitalcourse.util.DocmeePptApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocmeePptUtilApiImpl implements DocmeePptUtilApi {

    @Autowired
    private DocmeePptApi docmeePptApi;
    @Override
    public String createApiToken(String apiKey, String uid, Integer limit) {
        return docmeePptApi.createApiToken(apiKey, uid, limit);
    }
}
