package com.xiaozhao.xiaozhaoserver.web.config;

import com.xiaozhao.xiaozhaoserver.web.annotation.handle.MultiParameterBodyResolver;
import com.xiaozhao.xiaozhaoserver.web.interceptor.AllInterceptor;
import com.xiaozhao.xiaozhaoserver.web.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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
    private MultiParameterBodyResolver multiParameterBodyResolver;

    @Autowired
    private TokenPayloadArgumentResolver tokenPayloadArgumentResolver;

    @Autowired
    private AllInterceptor allInterceptor;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(allInterceptor).addPathPatterns("/**");
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/xiaozhao/user/login")
                .excludePathPatterns("/xiaozhao/person_group")
                .excludePathPatterns("/xiaozhao/analyze");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(multiParameterBodyResolver);
        resolvers.add(tokenPayloadArgumentResolver);
    }
}
