package com.xiaozhao.xiaozhaoserver.common.config;

import com.xiaozhao.xiaozhaoserver.common.objectpool.impl.DPairPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-14 13:10:04
 * @modify:
 */

@Configuration
public class CommonObjectPoolConfig {

    @Bean
    public <T, U> DPairPool<T, U> dPairPool() {
        return new DPairPool<>();
    }

}
