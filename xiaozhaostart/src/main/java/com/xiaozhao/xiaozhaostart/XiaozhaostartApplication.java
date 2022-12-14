package com.xiaozhao.xiaozhaostart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"com.xiaozhao"})
@ComponentScan("com.xiaozhao")
public class XiaozhaostartApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaozhaostartApplication.class, args);
    }

}
