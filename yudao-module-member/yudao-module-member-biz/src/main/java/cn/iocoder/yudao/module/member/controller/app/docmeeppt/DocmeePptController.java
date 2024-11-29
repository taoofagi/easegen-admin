package cn.iocoder.yudao.module.member.controller.app.docmeeppt;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.http.HttpUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.digitalcourse.api.DocmeePptUtilApi;
import cn.iocoder.yudao.module.infra.api.config.ConfigApi;
import cn.iocoder.yudao.module.member.controller.app.auth.vo.AppAuthLoginReqVO;
import cn.iocoder.yudao.module.member.controller.app.auth.vo.AppAuthLoginRespVO;
import cn.iocoder.yudao.module.member.controller.app.docmeeppt.vo.PageReqVO;
import cn.iocoder.yudao.module.member.controller.app.docmeeppt.vo.PopularPptReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "会员用户 热门ppt模板")
@RestController
@RequestMapping("/docmeeppt/ppt-api")
@Validated
@Slf4j
public class DocmeePptController {

    @Resource
    private DocmeePptUtilApi docmeepptUtil;
    @Resource
    private ConfigApi configApi;

    // API Key
    private static final String AIPPT_KEY = "aippt.key";

    private static final String AIPPT_LIMIT = "aippt.limit";

    private static final String POPULAR_PPT_PREFIXE = "https://docmee.cn";
    private static final String POPULAR_PPT_URL = "/api/ppt/templates";
    private static final String LIST_PPTX = "/api/ppt/listPptx";

    @PostMapping("/popular-ppt")
    @Operation(summary = "热门ppt模板")
    @PermitAll
    public CommonResult popularPpt(@RequestBody @Valid PopularPptReqVO popularPptReqVO) {
        //获取docmeeToken
        String apiToken = docmeepptUtil.createApiToken(configApi.getConfigValueByKey(AIPPT_KEY), String.valueOf(SecurityFrameworkUtils.getLoginUser().getId()), Integer.valueOf(configApi.getConfigValueByKey(AIPPT_LIMIT)));
        Map headerMap = new HashMap<>();
        headerMap.put("Api-Key", apiToken);
        String post = HttpUtils.post(POPULAR_PPT_PREFIXE + POPULAR_PPT_URL, headerMap, JSONUtil.toJsonStr(popularPptReqVO));
        JSONObject bean = JSONUtil.toBean(post, JSONObject.class);
        if (bean.getInt("code") == 0) {
            return success(bean);
        }
        log.error("热门ppt模板调用失败");
        return error(bean.getInt("code"),bean.getStr("message"));
    }

    @PostMapping("/list-pptx")
    @Operation(summary = "热门ppt模板")
    @PermitAll
    public CommonResult listPptx(@RequestBody @Valid PageReqVO pageReqVO) {
        //获取docmeeToken
        String apiToken = docmeepptUtil.createApiToken(configApi.getConfigValueByKey(AIPPT_KEY), String.valueOf(SecurityFrameworkUtils.getLoginUser().getId()), Integer.valueOf(configApi.getConfigValueByKey(AIPPT_LIMIT)));
        Map headerMap = new HashMap<>();
        headerMap.put("Api-Key", apiToken);
        String post = HttpUtils.post(POPULAR_PPT_PREFIXE + LIST_PPTX, headerMap, JSONUtil.toJsonStr(pageReqVO));
        JSONObject bean = JSONUtil.toBean(post, JSONObject.class);
        if (bean.getInt("code") == 0) {
            return success(bean);
        }
        log.error("用户生成ppt接口调用失败");
        return error(bean.getInt("code"),bean.getStr("message"));
    }
}
