package com.xiaozhao.xiaozhaoserver.xiaozhaostart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"com.xiaozhao"})
@ComponentScan("com.xiaozhao")
@EnableConfigurationProperties
@PropertySource("file:config/environment.properties")
public class XiaozhaostartApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaozhaostartApplication.class, args);
    }

}
