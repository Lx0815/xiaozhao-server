package com.xiaozhao.xiaozhaoserver.service.configProp;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-16 19:57:32
 * @modify:
 */

@Component
public class Code2SessionRequestProperties {

    /**
     * 该接口的 url
     */
    @Getter
    @Setter
    @Value("${wx.url:#{'https://api.weixin.qq.com/sns/jscode2session'}}")
    private String url;

    /**
     * 小程序 appId 的环境变量名称
     */
    @Getter
    @Setter
    @Value("${wx.appidEnvName:#{ 'XIAO_ZHAO_DEFAULT_WX_APPID' }}")
    private String appidEnvName;

    /**
     * 小程序 appId
     */
    private String appid;

    /**
     * 小程序 appSecret 的环境变量名称
     */
    @Getter
    @Setter
    @Value("${wx.secretEnvName:#{ 'XIAO_ZHAO_DEFAULT_WX_SECRET' }}")
    private String secretEnvName;


    /**
     * 小程序 appSecret
     */
    private String secret;

    /**
     * 登录时获取的 code
     */
    @Getter
    @Setter
    private String js_code;

    /**
     * 授权类型，此处只需填写 authorization_code
     */
    @Getter
    @Setter
    @Value("${wx.grant_type:#{ 'authorization_code' }}")
    private String grant_type;

    public String getAppid() {
        if (StringUtils.isBlank(appid)) {
            appid = System.getenv(getAppidEnvName());
        }
        return appid;
    }

    public String getSecret() {
        if (StringUtils.isBlank(secret)) {
            secret = System.getenv(getSecretEnvName());
        }
        return secret;
    }
}
