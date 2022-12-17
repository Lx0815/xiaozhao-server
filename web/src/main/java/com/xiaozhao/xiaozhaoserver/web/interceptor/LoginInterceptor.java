package com.xiaozhao.xiaozhaoserver.web.interceptor;

import com.xiaozhao.xiaozhaoserver.common.constants.Constants;
import com.xiaozhao.xiaozhaoserver.web.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-17 12:26:49
 * @modify:
 */

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String token = request.getHeader(Constants.TOKEN_HEADER_KEY);
        if (StringUtils.isBlank(token)) {
            log.error("token 为 null");
            return false;
        }
        return JWTUtils.verify(token);
    }
}
