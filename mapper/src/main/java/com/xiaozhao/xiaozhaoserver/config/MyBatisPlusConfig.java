package com.xiaozhao.xiaozhaoserver.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-07 0:01:30
 * @modify:
 */

@Configuration
@MapperScan("com.xiaozhao.xiaozhaoserver.mapper")
public class MyBatisPlusConfig {
}
