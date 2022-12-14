package com.xiaozhao.xiaozhaoserver.configProp;

import com.xiaozhao.xiaozhaoserver.common.constants.Constants;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description: 腾讯云接口的公共请求参数
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-08 9:42:33
 * @modify:
 */

@Component
public class PublicTencentApiProperty {

    @Value("${tencent.secretIdEnvName:#{ null }}")
    @Setter
    private String secretIdEnvName;

    @Value("${tencent.secretKeyEnvName:#{ null }}")
    @Setter
    private String secretKeyEnvName;

    @Value("${tencent.domainName}")
    @Getter
    @Setter
    private String domainName;

    @Value("${tencent.region}")
    @Getter
    @Setter
    private String region;

    private String secretId;

    private String secretKey;

    public String getSecretId() {
        if (StringUtils.isBlank(secretId)) {
            this.secretId = System.getenv(getSecretIdEnvName());
        }
        return this.secretId;
    }

    public String getSecretKey() {
        if (StringUtils.isBlank(secretKey)) {
            this.secretKey = System.getenv(getSecretKeyEnvName());
        }
        return this.secretKey;
    }

    public String getSecretIdEnvName() {
        if (StringUtils.isBlank(secretIdEnvName)) {
            secretIdEnvName = Constants.TENCENT_SECRET_ID_ENV;
        }
        return secretIdEnvName;
    }

    public String getSecretKeyEnvName() {
        if (StringUtils.isBlank(secretKeyEnvName)) {
            secretKeyEnvName = Constants.TENCENT_SECRET_KEY_ENV;
        }
        return secretKeyEnvName;
    }
}
