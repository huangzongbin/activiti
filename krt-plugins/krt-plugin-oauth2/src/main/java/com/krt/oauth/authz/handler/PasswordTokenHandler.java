package com.krt.oauth.authz.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.krt.common.bean.ReturnBean;
import com.krt.common.util.ServletUtils;
import com.krt.oauth.authz.OauthTokenxRequest;
import com.krt.oauth.authz.base.AbstractOauthTokenHandler;
import com.krt.oauth.authz.validator.AbstractOauthTokenValidator;
import com.krt.oauth.authz.validator.PasswordClientDetailsValidator;
import com.krt.oauth.entity.OauthToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;

/**
 * 密码模式（resource owner password credentials）处理器
 *
 * @author 殷帅
 * @version 1.0
 * @date 2017年12月09日
 */
@Slf4j
public class PasswordTokenHandler extends AbstractOauthTokenHandler {

    /**
     * 判断是否支持模式
     *
     * @param tokenRequest
     * @throws OAuthProblemException
     */
    @Override
    public boolean support(OauthTokenxRequest tokenRequest) throws OAuthProblemException {
        final String grantType = tokenRequest.getGrantType();
        return GrantType.PASSWORD.toString().equalsIgnoreCase(grantType);
    }

    /**
     * 规则验证通过后处理
     * 请求url:/oauth/token?client_id=mobile-client&client_secret=mobile&grant_type=password&scope=read,write&username=mobile&password=mobile
     * 响应:{"token_type":"Bearer","expires_in":33090,"refresh_token":"976aeaf7df1ee998f98f15acd1de63ea","access_token":"7811aff100eb7dadec132f45f1c01727"}
     *
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     */
    @Override
    public void handleAfterValidation() throws OAuthProblemException, OAuthSystemException {
        OauthToken oauthToken = oauthService.doAccessTokenByPassword(clientDetails(), tokenRequest.getScopes(), tokenRequest.getUsername());
        final ReturnBean returnBean = createTokenResponse(oauthToken, false);
        Object rbJson = JSON.toJSONString(returnBean);
        log.debug("密码模式响应: {}", rbJson);
        ServletUtils.printJson(response, rbJson);
    }

    @Override
    protected AbstractOauthTokenValidator getValidator() {
        return new PasswordClientDetailsValidator(tokenRequest);
    }
}
