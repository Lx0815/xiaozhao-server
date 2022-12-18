package com.xiaozhao.xiaozhaoserver.service.configProp;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-16 19:57:32
 * @modify:
 */

@Data
@Component
public class Code2SessionRequestProperties {

    /**
     * 该接口的 url
     */
    @Value("${wx.url:#{'https://api.weixin.qq.com/sns/jscode2session'}}")
    private String url;

    /**
     * 小程序 appId
     */
    @Value("${XIAO_ZHAO_DEFAULT_WX_APPID}")
    private String appid;


    /**
     * 小程序 appSecret
     */
    @Value("${XIAO_ZHAO_DEFAULT_WX_SECRET}")
    private String secret;

    /**
     * 登录时获取的 code
     */
    private String js_code;

    /**
     * 授权类型，此处只需填写 authorization_code
     */
    @Value("${wx.grant_type:#{ 'authorization_code' }}")
    private String grant_type;

}
