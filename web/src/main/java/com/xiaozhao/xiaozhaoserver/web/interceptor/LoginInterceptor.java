package com.xiaozhao.xiaozhaoserver.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaozhao.xiaozhaoserver.common.constants.Constants;
import com.xiaozhao.xiaozhaoserver.web.pool.ResponseObjectPool;
import com.xiaozhao.xiaozhaoserver.web.response.ResponseCode;
import com.xiaozhao.xiaozhaoserver.web.utils.JWTUtils;
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
        String ip = getIpAddress(request);
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
        return true;
    }

    /**
     * 获取用户真实IP地址，不使用 request.getRemoteAddr(); 的原因是有可能用户使用了代理软件方式避免真实 IP 地址,
     * <a href="http://developer.51cto.com/art/201111/305181.htm">参考文章</a>
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request 请求对象
     * @return 返回获取到的 ip，获取不到则为 null
     */
    public String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (! StringUtils.isEmpty(ip)) {
            ip = StringUtils.trimToNull(ip.split(",")[0]);
        }
        return StringUtils.isBlank(ip) ? "" : ip;
    }
}
