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

    /**
     * 存储桶名称
     */
    @Value("${qiniu.bucket}")
    private String bucket;

    /**
     * 地区描述，例如 huadongzhejiang2
     */
    @Value("${qiniu.region}")
    private String region;

    /**
     * 七牛云中配置的访问域名
     */
    @Value("${qiniu.domain}")
    private String domain;

    /**
     * 图片存储的根路径
     */
    @Value("${qiniu.rootDirectory:#{ 'xiaozhao/person-face/' }}")
    private String rootDirectory;

    /**
     * 最大重试次数
     */
    @Value("${qiniu.retryMaxCount:#{ 3 }}")
    private int retryMaxCount;

    /**
     * 加速域名（见官网，不知道有啥用）
     */
    @Value("${qiniu.accelerateUploadDomain}")
    private String accelerateUploadDomain;

    /**
     * 访问公钥，从环境变量获取
     */
    @Value("${XIAO_ZHAO_DEFAULT_QINIU_ACCESS_KEY}")
    private String accessKey;

    /**
     * 密钥，从环境变量获取
     */
    @Value("${XIAO_ZHAO_DEFAULT_QINIU_SECRET_KEY}")
    private String secretKey;


    /**
     * 手动复写该方法是为了确保不出现两个连续的斜杠
     */
    public String getDomain() {
        if (! StringUtils.isBlank(domain) && !domain.endsWith("/")) {
            domain += '/';
        }
        return domain;
    }

    /**
     * 手动复写该方法是为了确保不出现两个连续的斜杠
     */
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
