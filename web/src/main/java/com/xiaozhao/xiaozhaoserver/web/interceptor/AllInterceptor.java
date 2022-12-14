package com.xiaozhao.xiaozhaoserver.web.interceptor;

import com.xiaozhao.xiaozhaoserver.common.objectpool.impl.DPairPool;
import com.xiaozhao.xiaozhaoserver.web.pool.ResponseObjectPool;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-14 13:42:15
 * @modify:
 */

@Component
public class AllInterceptor implements HandlerInterceptor {

    @Autowired
    private ResponseObjectPool responseObjectPool;

    @Autowired
    private DPairPool<Object, Object> dPairPool;

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        responseObjectPool.returnObject();
        dPairPool.returnObject();
    }
}
