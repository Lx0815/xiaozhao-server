package com.xiaozhao.xiaozhaoserver.config.qiniu;

import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.NoSuchProviderException;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-11-16 23:25:31
 * @modify:
 */

@Slf4j
@Configuration
public class QiNiuConfig {

    private QiNiuProperties qiNiuProperties;
    @Autowired
    public void setQiNiuProperties(QiNiuProperties qiNiuProperties) {
        this.qiNiuProperties = qiNiuProperties;
    }

    /**
     * 配置空间的存储区域
     */
    @Bean
    public com.qiniu.storage.Configuration qiNiuConfiguration() {
        try {
            log.info("准备开始读取 QiNiuProperties: ");
            log.info(qiNiuProperties.toString());
            com.qiniu.storage.Configuration configuration = new com.qiniu.storage.Configuration(RegionFactoryBuilder
                    .builder(qiNiuProperties.getRegion())
                    .createRegion());

            configuration.retryMax = qiNiuProperties.getRetryMaxCount();
            return configuration;
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建一个七牛上传工具实例
     */
    @Bean
    public UploadManager uploadManager() {
        return new UploadManager(qiNiuConfiguration());
    }

    /**
     * 认证信息实例
     */
    @Bean
    public Auth auth() {
        return Auth.create(qiNiuProperties.getAccessKey(), qiNiuProperties.getSecretKey());
    }
}
