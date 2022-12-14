package com.xiaozhao.xiaozhaoserver.web.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-09 8:44:35
 * @modify:
 */

@Data
@Accessors(chain = true)
public class ResponseObject {

    private ResponseCode code;

    private Object data;

    private String message;

    public void clear() {
        this.code = null;
        this.data = null;
        this.message = null;
    }

    @Override
    public String toString() {
        return "\n" +
                "ErrorCode: \t" + code + "\n" +
                "data: \t\t" + data + "\n" +
                "message: \t" + message + "\n" +
                "\n";
    }
}
