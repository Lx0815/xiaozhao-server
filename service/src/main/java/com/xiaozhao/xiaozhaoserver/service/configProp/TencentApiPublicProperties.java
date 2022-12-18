package com.xiaozhao.xiaozhaoserver.service.configProp;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description: 腾讯云接口的公共请求参数
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-08 9:42:33
 * @modify:
 */

@Data
@Component
public class TencentApiPublicProperties {

    @Value("${tencent.domainName}")
    private String domainName;

    @Value("${tencent.region}")
    private String region;

    @Value("${XIAO_ZHAO_DEFAULT_TENCENT_SECRET_ID}")
    private String secretId;

    @Value("${XIAO_ZHAO_DEFAULT_TENCENT_SECRET_KEY}")
    private String secretKey;

}
