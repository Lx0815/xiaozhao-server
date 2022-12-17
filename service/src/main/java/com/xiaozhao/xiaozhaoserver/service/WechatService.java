package com.xiaozhao.xiaozhaoserver.service;

import com.xiaozhao.xiaozhaoserver.configProp.Code2SessionRequestProperties;
import com.xiaozhao.xiaozhaoserver.model.User;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-16 19:50:04
 * @modify:
 */


public interface WechatService {

    User code2Session(Code2SessionRequestProperties code2SessionRequestProperties);

}
