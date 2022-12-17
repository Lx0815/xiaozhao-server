package com.xiaozhao.xiaozhaoserver.utils;

import com.tencentcloudapi.common.AbstractModel;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.iai.v20200303.IaiClient;
import com.xiaozhao.xiaozhaoserver.configProp.TencentApiPublicProperties;
import com.xiaozhao.xiaozhaoserver.exception.BadParameterException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-08 8:59:48
 * @modify:
 */

@Slf4j
public class TencentApiUtils {

    private TencentApiUtils() {}

    private static List<Method> iaiClientMethodList;

    /*
      加载该类的所有请求方法
     */
    static {
        log.info("开始添加 iaiClient method");
        iaiClientMethodList = new LinkedList<>();
        Method[] methods = IaiClient.class.getMethods();
        iaiClientMethodList = Arrays.asList(methods);
        log.info("添加了 " + methods.length + " 个 iaiClient method");
    }

    /**
     * 向腾讯云接口提交请求
     * @param abstractModel 请求模型
     * @param responseClass 响应类的类对象
     * @param tencentApiPublicProperties 腾讯云接口的相关请求参数
     * @return 返回请求得到的响应对象
     * @param <T> 响应对象
     * @throws TencentCloudSDKException 调用腾讯云接口时抛出异常
     */
    @SuppressWarnings("unchecked")
    public static <T> T executeIciClientRequest(AbstractModel abstractModel, Class<T> responseClass,
                                                   TencentApiPublicProperties tencentApiPublicProperties) throws TencentCloudSDKException {

        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(tencentApiPublicProperties.getSecretId(),
                    tencentApiPublicProperties.getSecretKey());
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setDebug(true);
            // 实例化要请求产品的client对象,clientProfile是可选的
            IaiClient client = new IaiClient(cred, tencentApiPublicProperties.getRegion(), clientProfile);

            // 返回的resp是一个CreatePersonResponse的实例，与请求对象对应
            for (Method method : iaiClientMethodList) {
                if (method.getParameters()[0].getType() == abstractModel.getClass()) {
                    Object responseObj = method.invoke(client, abstractModel);
                    if (responseObj.getClass() == responseClass) {
                        return (T) responseObj;
                    } else {
                        // 删除刚刚创建的东西
                        // 。。。。。。
                        throw new BadParameterException(String.format("方法 %s 的返回值为 %s ，而收到的类型为 %s",
                                method.getName(), method.getReturnType(), responseClass));
                    }
                }
            }
        } catch (InvocationTargetException e) {
            throw (TencentCloudSDKException) e.getTargetException();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("IaiClient 中没用可用的方法以发送 " + abstractModel);
    }

}
