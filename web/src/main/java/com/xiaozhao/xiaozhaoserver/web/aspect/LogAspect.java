package com.xiaozhao.xiaozhaoserver.web.aspect;

import com.xiaozhao.xiaozhaoserver.web.utils.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-29 16:32:41
 * @modify:
 */

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(* com.xiaozhao.xiaozhaoserver.web.controller.*Controller.*(..))")
    public void all() {}

    @Around("all()")
    public Object allAround(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(requestAttributes, "获取请求对象失败");
        HttpServletRequest request = requestAttributes.getRequest();

        String uuid = UUID.randomUUID().toString();

        log.info(String.format("\n请求 ID: %s,\n请求 IP: %s,\n请求 URL: %s,\n请求参数: %s",
                uuid, RequestUtils.getIpAddress(request), request.getRequestURL().toString(), Arrays.toString(pjp.getArgs())));

        Object result = pjp.proceed();
        log.info(String.format("\n请求 ID: %s,\n响应对象：%s", uuid, result));
        return result;
    }
}
