package cn.iocoder.yudao.module.system.api.auth;

import cn.iocoder.yudao.module.system.api.auth.dto.OAuth2AccessTokenCheckRespDTO;
import cn.iocoder.yudao.module.system.api.auth.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.yudao.module.system.api.auth.dto.OAuth2AccessTokenRespDTO;
import cn.iocoder.yudao.module.system.convert.auth.OAuth2TokenConvert;
import cn.iocoder.yudao.module.system.dal.dataobject.auth.OAuth2AccessTokenDO;
import cn.iocoder.yudao.module.system.service.auth.OAuth2TokenService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * OAuth2.0 Token API 实现类
 *
 * @author 芋道源码
 */
@Service
public class OAuth2TokenApiImpl implements OAuth2TokenApi {

    @Resource
    private OAuth2TokenService oauth2TokenService;

    @Override
    public OAuth2AccessTokenRespDTO createAccessToken(OAuth2AccessTokenCreateReqDTO reqDTO) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(
                reqDTO.getUserId(), reqDTO.getUserType(), reqDTO.getClientId());
        return OAuth2TokenConvert.INSTANCE.convert2(accessTokenDO);
    }

    @Override
    public OAuth2AccessTokenCheckRespDTO checkAccessToken(String accessToken) {
        return OAuth2TokenConvert.INSTANCE.convert(oauth2TokenService.checkAccessToken(accessToken));
    }

    @Override
    public OAuth2AccessTokenRespDTO removeAccessToken(String accessToken) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.removeAccessToken(accessToken);
        return OAuth2TokenConvert.INSTANCE.convert2(accessTokenDO);
    }

    @Override
    public OAuth2AccessTokenRespDTO refreshAccessToken(String refreshToken, Long clientId) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.refreshAccessToken(refreshToken, clientId);
        return OAuth2TokenConvert.INSTANCE.convert2(accessTokenDO);
    }

}
