package com.xiaozhao.xiaozhaoserver.common.constants;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-09 9:56:02
 * @modify:
 */

public class Constants {

    /*=====================================
        第三方接口
        ======================================*/
    /**
        存储腾讯云 API 密钥 secretId 的默认环境变量名称
     */
    public static final String TENCENT_SECRET_ID_ENV = "TENCENT_SECRET_ID";

    /**
        存储腾讯云 API 密钥 secretKey 的默认环境变量名称
     */
    public static final String TENCENT_SECRET_KEY_ENV = "TENCENT_SECRET_KEY";

    /**
     * 七牛云存储 默认的 初始目录
     */
    public static final String QINIU_DEFAULT_DIRECTORY = "xiaozhao/person-face/";

    /**
     * 七牛云存储API 密钥 accessKey 的默认环境变量名称
     */
    public static final String QINIU_ACCESS_KEY_ENV = "QINIU_ACCESS_KEY";

    /**
     * 七牛云存储API 密钥 secretKey 的默认环境变量名称
     */
    public static final String QINIU_SECRET_KEY_ENV = "QINIU_SECRET_KEY";

    /*=====================================
        Cookie
        ======================================*/
    /**
     * Cookie 属性，人员库ID 在 cookie 中存储的时间
     */
    public static final Integer PERSON_GROUP_ID_COOKIE_MAX_AGE = 630720000;

    /**
     * Cookie 键，人员库ID 在 cookie 中存储的键名称
     */
    public static final String PERSON_GROUP_ID_COOKIE_KEY = "PERSON_GROUP_ID_COOKIE_KEY";



    /*=====================================
        Web 响应相关
        ======================================*/
    /**
     * 响应描述
     */
    public static final String DATA_ALREADY_EXISTS = "数据已存在";

    private Constants() {}

    public static final String SUCCESS = "成功";

    public static final String SERVER_EXCEPTION = "服务器异常";

    public static final String NO_FACE_IN_PHOTO_EXCEPTION = "图片中无人脸";

    public static final String FACE_DETECTION_SUCCEEDED = "人脸检测成功";

    public static final String MISSING_PARAMETERS_EXCEPTION = "缺失参数异常";
}
