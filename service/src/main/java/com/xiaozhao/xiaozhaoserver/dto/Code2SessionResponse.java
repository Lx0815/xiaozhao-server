package com.xiaozhao.xiaozhaoserver.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-16 19:51:44
 * @modify:
 */

@Data
@Accessors(chain = true)
public class Code2SessionResponse {

    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 会话密钥
     */
    private String session_key;

    /**
     * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台帐号下会返回，详见 <a href="https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/union-id.html">UnionID 机制说明</a>。
     */
    private String unionid;

    /**
     * 错误码
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;

    public String getErrorInfo() {
        return "code: " + errcode +
                "message: " + errmsg;
    }
}
