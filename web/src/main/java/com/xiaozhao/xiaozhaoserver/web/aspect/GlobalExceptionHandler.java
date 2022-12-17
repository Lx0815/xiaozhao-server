package com.xiaozhao.xiaozhaoserver.web.aspect;

import com.xiaozhao.xiaozhaoserver.common.constants.Constants;
import com.xiaozhao.xiaozhaoserver.service.exception.BadParameterException;
import com.xiaozhao.xiaozhaoserver.service.exception.NoFaceInPhotoException;
import com.xiaozhao.xiaozhaoserver.service.exception.NotFoundPersonException;
import com.xiaozhao.xiaozhaoserver.service.exception.ResourceNotFoundException;
import com.xiaozhao.xiaozhaoserver.web.pool.ResponseObjectPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.xiaozhao.xiaozhaoserver.web.response.ResponseCode.*;
/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-14 21:42:50
 * @modify:
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler  {

    @Autowired
    private ResponseObjectPool responseObjectPool;

    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return responseObjectPool.createResponse(SERVER_EXCEPTION, Constants.SERVER_EXCEPTION);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Object handleResourceNotFoundException(ResourceNotFoundException e) {
        e.printStackTrace();
        return responseObjectPool.createResponse(RESOURCE_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(NoFaceInPhotoException.class)
    public Object handleNoFaceInPhotoException(NoFaceInPhotoException e) {
        e.printStackTrace();
        return responseObjectPool.createResponse(NO_FACE_IN_PHOTO_EXCEPTION, Constants.NO_FACE_IN_PHOTO_EXCEPTION);
    }

    @ExceptionHandler(BadParameterException.class)
    public Object handleBadParameterException(BadParameterException e) {
        e.printStackTrace();
        return responseObjectPool.createResponse(ERROR_PARAMETER_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(NotFoundPersonException.class)
    public Object handleNotFoundPersonException(NotFoundPersonException e) {
        e.printStackTrace();
        return responseObjectPool.createResponse(NOT_FOUND_PERSON_EXCEPTION, Constants.NOT_FOUND_PERSON_EXCEPTION);
    }

}
