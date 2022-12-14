package com.xiaozhao.xiaozhaoserver.web.response;

import com.fasterxml.jackson.annotation.JsonValue;
import com.xiaozhao.xiaozhaoserver.common.constants.Constants;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-09 9:31:25
 * @modify:
 */

public enum ResponseCode {

    SUCCESS("00000", Constants.SUCCESS),

    /*
        A 类错误码来源于用户端
     */
    ERROR_PARAMETER_EXCEPTION("A0001", Constants.ERROR_PARAMETERS_EXCEPTION),

    NO_FACE_IN_PHOTO_EXCEPTION("A0101", Constants.NO_FACE_IN_PHOTO_EXCEPTION),

    DATA_ALREADY_EXISTS("A0002", Constants.DATA_ALREADY_EXISTS),

    RESOURCE_NOT_FOUND("A0003", Constants.RESOURCE_NOT_FOUND),


    /*
        B 类错误码来源于当前系统
     */
    SERVER_EXCEPTION("B0000", Constants.SERVER_EXCEPTION);





    /*
        C 类错误码来源于第三方服务
     */


    private final String code;

    private final String description;

    ResponseCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("错误码：%s，\n描述：%s", code, description);
    }
}
