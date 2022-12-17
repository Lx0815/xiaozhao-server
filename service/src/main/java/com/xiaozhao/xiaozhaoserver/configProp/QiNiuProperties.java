package com.xiaozhao.xiaozhaoserver.configProp;

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


    @Setter
    @Getter
    @Value("${qiniu.accessKeyEnvName:#{ 'XIAO_ZHAO_DEFAULT_QINIU_ACCESS_KEY' }}")
    private String accessKeyEnvName;


    @Setter
    @Getter
    @Value("${qiniu.secretKeyEnvName:#{ 'XIAO_ZHAO_DEFAULT_QINIU_SECRET_KEY' }}")
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

    @Value("${qiniu.rootDirectory:#{ 'xiaozhao/person-face/' }}")
    @Setter
    private String rootDirectory;


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
        if (! StringUtils.isBlank(domain) && !domain.endsWith("/")) {
            domain += '/';
        }
        return domain;
    }

    public String getRootDirectory() {
        if (! StringUtils.isBlank(rootDirectory) && rootDirectory.startsWith("/")) {
            rootDirectory = rootDirectory.substring(1);
        }
        if (! StringUtils.isBlank(rootDirectory) && !rootDirectory.endsWith("/")) {
            rootDirectory += '/';
        }
        return rootDirectory;
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
