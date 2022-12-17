package com.xiaozhao.xiaozhaoserver.service.impl;

import com.xiaozhao.xiaozhaoserver.configProp.Code2SessionRequestProperties;
import com.xiaozhao.xiaozhaoserver.dto.Code2SessionResponse;
import com.xiaozhao.xiaozhaoserver.exception.BadParameterException;
import com.xiaozhao.xiaozhaoserver.mapper.UserMapper;
import com.xiaozhao.xiaozhaoserver.model.User;
import com.xiaozhao.xiaozhaoserver.service.WechatService;
import com.xiaozhao.xiaozhaoserver.utils.HttpsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-16 19:50:30
 * @modify:
 */

@Slf4j
@Service
public class WechatServiceImpl implements WechatService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User code2Session(Code2SessionRequestProperties code2SessionRequestProperties) {
        log.info("准备开始向微信请求登录");
        String url = HttpsUtils.wrapperGetParameter(code2SessionRequestProperties.getUrl(),
                "appid", code2SessionRequestProperties.getAppid(),
                "secret", code2SessionRequestProperties.getSecret(),
                "js_code", code2SessionRequestProperties.getJs_code(),
                "grant_type", code2SessionRequestProperties.getGrant_type()
        );
        log.info("请求 URL：" + url);
        Code2SessionResponse code2SessionResponse = HttpsUtils.httpGet(url, Code2SessionResponse.class);
        if (StringUtils.isBlank(code2SessionResponse.getOpenid()) && ! StringUtils.isBlank(code2SessionResponse.getErrcode().toString())) {
            throw new BadParameterException("登录认证请求失败，响应信息为：\n" + code2SessionResponse.getErrorInfo());
        }

        log.info("请求成功，响应对象为：" + code2SessionResponse);
        log.info("准备插入 user");
        User user = new User();
        user.setOpenid(code2SessionResponse.getOpenid());
        userMapper.insert(user);
        log.info("插入成功。" + user);
        return user;
    }
}
