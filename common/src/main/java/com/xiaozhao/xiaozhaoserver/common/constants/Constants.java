package com.xiaozhao.xiaozhaoserver.common.constants;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-09 9:56:02
 * @modify:
 */

public class Constants {

    public static final Integer PERSON_GROUP_ID_COOKIE_MAX_AGE = 630720000;

    public static final String PERSON_GROUP_ID_COOKIE_KEY = "PERSON_GROUP_ID_COOKIE_KEY";
    public static final String QINIU_DEFAULT_DIRECTORY = "xiaozhao/person-face/";
    public static final String DATA_ALREADY_EXISTS = "数据已存在";

    private Constants() {}

    public static final String SUCCESS = "成功";

    public static final String SERVER_EXCEPTION = "服务器异常";

    public static final String NO_FACE_IN_PHOTO_EXCEPTION = "图片中无人脸";

    public static final String FACE_DETECTION_SUCCEEDED = "人脸检测成功";

    public static final String MISSING_PARAMETERS_EXCEPTION = "缺失参数异常";
}
