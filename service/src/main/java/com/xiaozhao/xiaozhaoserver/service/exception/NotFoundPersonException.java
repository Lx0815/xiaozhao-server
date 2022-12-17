package com.xiaozhao.xiaozhaoserver.service.exception;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-17 15:21:55
 * @modify:
 */

public class NotFoundPersonException extends RuntimeException {

    public NotFoundPersonException() {
        this("");
    }

    public NotFoundPersonException(String message) {
        this(message, null);
    }

    public NotFoundPersonException(Throwable cause) {
        super("", cause);
    }

    public NotFoundPersonException(String message, Throwable cause) {
        super(message, cause);
    }
}
