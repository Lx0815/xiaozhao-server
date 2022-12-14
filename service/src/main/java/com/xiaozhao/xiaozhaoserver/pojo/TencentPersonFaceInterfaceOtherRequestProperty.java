package com.xiaozhao.xiaozhaoserver.pojo;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: 腾讯云接口的公共请求参数
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-08 9:42:33
 * @modify:
 */

@Data
@Accessors(chain = true)
@Component
@ConfigurationProperties(prefix = "tencent-person-face-interface-other-request-property")
public class TencentPersonFaceInterfaceOtherRequestProperty {

    private String secretId;

    private String secretKey;

    private String domainName;

    private String region;

}
