package com.xiaozhao.xiaozhaoserver.config.qiniu;

import com.xiaozhao.xiaozhaoserver.common.constants.Constants;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description: 域名相关：<a href="https://developer.qiniu.com/kodo/1671/region-endpoint-fq">域名</a>
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-11-17 6:15:13
 * @modify:
 */

@Component
public class QiNiuProperties {

    @Value("${qiniu.accessKeyEnvName:#{ null }}")
    @Setter
    private String accessKeyEnvName;

    @Value("${qiniu.secretKeyEnvName:#{ null }}")
    @Setter
    private String secretKeyEnvName;

    @Value("${qiniu.bucket}")
    @Getter
    @Setter
    private String bucket;

    @Value("${qiniu.region}")
    @Getter
    @Setter
    private String region;

    @Value("${qiniu.domain}")
    @Setter
    private String domain;

    @Value("${qiniu.retryMaxCount:#{ 3 }}")
    @Getter
    @Setter
    private int retryMaxCount;

    @Value("${qiniu.accelerateUploadDomain}")
    @Getter
    @Setter
    private String accelerateUploadDomain;

    private String accessKey;

    private String secretKey;


    public String getDomain() {
        if (! "".equals(domain) && ! domain.endsWith("/")) {
            domain += '/';
        }
        return domain;
    }

    public String getAccessKeyEnvName() {
        if (StringUtils.isBlank(accessKeyEnvName)) {
            accessKeyEnvName = Constants.QINIU_ACCESS_KEY_ENV;
        }
        return accessKeyEnvName;
    }

    public String getSecretKeyEnvName() {
        if (StringUtils.isBlank(secretKeyEnvName)) {
            secretKeyEnvName = Constants.QINIU_SECRET_KEY_ENV;
        }
        return secretKeyEnvName;
    }

    public String getAccessKey() {
        if (StringUtils.isBlank(accessKey)) {
            accessKey = System.getenv(getAccessKeyEnvName());
        }
        return accessKey;
    }

    public String getSecretKey() {
        if (StringUtils.isBlank(secretKey)) {
            secretKey = System.getenv(getSecretKeyEnvName());
        }
        return secretKey;
    }
}
