package com.xiaozhao.xiaozhaoserver.web.controller;

import com.xiaozhao.xiaozhaoserver.configProp.Code2SessionRequestProperties;
import com.xiaozhao.xiaozhaoserver.exception.BadParameterException;
import com.xiaozhao.xiaozhaoserver.model.User;
import com.xiaozhao.xiaozhaoserver.service.WechatService;
import com.xiaozhao.xiaozhaoserver.web.pool.ResponseObjectPool;
import com.xiaozhao.xiaozhaoserver.web.utils.JWTUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-16 9:15:47
 * @modify:
 */

@Slf4j
@Api("用户控制器")
@RestController
@RequestMapping("/xiaozhao/user")
public class UserController {

    @Autowired
    private WechatService wechatService;

    @Autowired
    private Code2SessionRequestProperties code2SessionRequestProperties;

    @Autowired
    private ResponseObjectPool responseObjectPool;

    @PostMapping("/login")
    public Object login(@RequestBody Code2SessionRequestProperties code2SessionRequestProperties) {
        if (StringUtils.isBlank(code2SessionRequestProperties.getJs_code())) throw new BadParameterException();
        this.code2SessionRequestProperties.setJs_code(code2SessionRequestProperties.getJs_code());
        log.info(String.format("获取到 js_code：%s，准备登录", code2SessionRequestProperties.getJs_code()));
        User user = wechatService.code2Session(this.code2SessionRequestProperties);
        String token = JWTUtils.getToken("id", String.valueOf(user.getId()));
        log.info(String.format("登录成功，用户信息：%s，生成的 token ：%s", user, token));
        return responseObjectPool.createSuccessResponse(token);
    }

}
