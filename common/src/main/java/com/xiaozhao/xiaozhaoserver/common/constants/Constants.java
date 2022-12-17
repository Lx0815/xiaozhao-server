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

    /**
     * Token 在 header 中的键名称
     */
    public static final String TOKEN_HEADER_KEY = "Token";



    /*=====================================
        Web 响应相关
        ======================================*/
    /**
     * 响应描述
     */
    public static final String DATA_ALREADY_EXISTS = "数据已存在";
    public static final String RESOURCE_NOT_FOUND = "资源不存在";
    public static final String NOT_FOUND_PERSON_EXCEPTION = "没有找到人员";

    private Constants() {}

    public static final String SUCCESS = "成功";

    public static final String SERVER_EXCEPTION = "服务器异常";

    public static final String NO_FACE_IN_PHOTO_EXCEPTION = "图片中无人脸";

    public static final String FACE_DETECTION_SUCCEEDED = "人脸检测成功";

    public static final String ERROR_PARAMETERS_EXCEPTION = "错误参数异常";
}
