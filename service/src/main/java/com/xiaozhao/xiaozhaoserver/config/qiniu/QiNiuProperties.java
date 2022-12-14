package com.xiaozhao.xiaozhaoserver.config.qiniu;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: 域名相关：<a href="https://developer.qiniu.com/kodo/1671/region-endpoint-fq">域名</a>
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-11-17 6:15:13
 * @modify:
 */

@Component
@ConfigurationProperties(prefix = "qiniu", ignoreInvalidFields = true)
public class QiNiuProperties {

    private String accessKey = "qiniu";

    private String secretKey;

    private String bucket;

    private String region;

    private String domain;

    private String accessRootPath;

    private int retryMaxCount = 3;

    private String accelerateUploadDomain;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDomain() {
        if (! "".equals(domain) && ! domain.endsWith("/")) {
            domain += '/';
        }
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAccessRootPath() {
        return accessRootPath;
    }

    public void setAccessRootPath(String accessRootPath) {
        this.accessRootPath = accessRootPath;
    }

    public int getRetryMaxCount() {
        return retryMaxCount;
    }

    public void setRetryMaxCount(int retryMaxCount) {
        this.retryMaxCount = retryMaxCount;
    }

    public String getAccelerateUploadDomain() {
        return accelerateUploadDomain;
    }

    public void setAccelerateUploadDomain(String accelerateUploadDomain) {
        this.accelerateUploadDomain = accelerateUploadDomain;
    }
}
