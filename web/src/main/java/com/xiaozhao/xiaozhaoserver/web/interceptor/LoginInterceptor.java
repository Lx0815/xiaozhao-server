package com.xiaozhao.xiaozhaoserver.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaozhao.xiaozhaoserver.common.constants.Constants;
import com.xiaozhao.xiaozhaoserver.web.pool.ResponseObjectPool;
import com.xiaozhao.xiaozhaoserver.web.response.ResponseCode;
import com.xiaozhao.xiaozhaoserver.web.utils.JWTUtils;
import com.xiaozhao.xiaozhaoserver.web.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResponseObjectPool responseObjectPool;

    private final Map<String, Integer> interceptorMap = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        String token = request.getHeader(Constants.TOKEN_HEADER_KEY);
        String ip = RequestUtils.getIpAddress(request);
        if (interceptorMap.containsKey(ip)) {
            return false;
        }
        if (StringUtils.isBlank(token) || ! JWTUtils.verify(token)) {

            Integer count = interceptorMap.getOrDefault(ip, 0);
            interceptorMap.put(ip, ++count);

            log.warn(String.format("IP 为 %s 的主机已经第 %d 次调用接口失败。", ip, count));
            if (count > 10) {
                log.warn("IP 为 %s 的主机已被加入黑名单。");
            }

            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            String responseJson = objectMapper.writeValueAsString(responseObjectPool.createResponse(
                    ResponseCode.NOT_LOGIN, null, null));
            writer.write(responseJson);
            return false;
        }
        log.info("源IP 为 " + ip + " 的请求已通过 Token 校验");
        return true;
    }


}
