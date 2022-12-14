package com.xiaozhao.xiaozhaoserver.web.config;

import com.xiaozhao.xiaozhaoserver.web.pool.ResponseObjectPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-14 13:33:33
 * @modify:
 */

@Configuration
public class WebObjectPoolConfig {

    @Bean
    public ResponseObjectPool responseObjectPool() {
        return new ResponseObjectPool();
    }

}
