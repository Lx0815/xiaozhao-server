package com.xiaozhao.xiaozhaoserver.exception;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-08 13:48:24
 * @modify:
 */

public class NoFaceInPhotoException extends RuntimeException {

    public NoFaceInPhotoException() {
    }

    public NoFaceInPhotoException(String message) {
        super(message);
    }

    public NoFaceInPhotoException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoFaceInPhotoException(Throwable cause) {
        super(cause);
    }
}
