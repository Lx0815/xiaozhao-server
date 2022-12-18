package com.xiaozhao.xiaozhaoserver.service.configProp;

import lombok.Data;
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

@Data
@Component
public class QiNiuProperties {

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.region}")
    private String region;

    @Value("${qiniu.domain}")
    private String domain;

    @Value("${qiniu.rootDirectory:#{ 'xiaozhao/person-face/' }}")
    private String rootDirectory;


    @Value("${qiniu.retryMaxCount:#{ 3 }}")
    private int retryMaxCount;

    @Value("${qiniu.accelerateUploadDomain}")
    private String accelerateUploadDomain;

    @Value("${XIAO_ZHAO_DEFAULT_QINIU_ACCESS_KEY}")
    private String accessKey;

    @Value("${XIAO_ZHAO_DEFAULT_QINIU_SECRET_KEY}")
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
}
