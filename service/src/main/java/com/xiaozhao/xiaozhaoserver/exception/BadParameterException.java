package com.xiaozhao.xiaozhaoserver.exception;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-08 12:36:56
 * @modify:
 */

public class BadParameterException extends RuntimeException {

    public BadParameterException() {
    }

    public BadParameterException(String message) {
        this(message, null);
    }

    public BadParameterException(Throwable cause) {
        this("bad parameter", cause);
    }

    public BadParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
