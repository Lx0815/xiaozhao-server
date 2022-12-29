package com.xiaozhao.xiaozhaoserver.web.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-29 16:46:53
 * @modify:
 */

public class RequestUtils {

    private RequestUtils() {}

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
    public static String getIpAddress(HttpServletRequest request) {
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
