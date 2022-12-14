package com.xiaozhao.xiaozhaoserver.web.config;

import com.xiaozhao.xiaozhaoserver.web.interceptor.AllInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-14 13:46:03
 * @modify:
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AllInterceptor allInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(allInterceptor).addPathPatterns("/**");
    }
}
