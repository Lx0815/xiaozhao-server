package com.xiaozhao.xiaozhaoserver.web.pool;

import com.xiaozhao.xiaozhaoserver.common.objectpool.AbstractPool;
import com.xiaozhao.xiaozhaoserver.web.pool.factory.ResponseObjectPooledObjectFactory;
import com.xiaozhao.xiaozhaoserver.web.response.ResponseCode;
import com.xiaozhao.xiaozhaoserver.web.response.ResponseObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import static com.xiaozhao.xiaozhaoserver.web.response.ResponseCode.SUCCESS;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-07 20:42:55
 * @modify:
 */


public class ResponseObjectPool extends AbstractPool<ResponseObject> {

    public ResponseObjectPool() {
        this(new ResponseObjectPooledObjectFactory());
    }

    public ResponseObjectPool(PooledObjectFactory<ResponseObject> pooledObjectFactory) {
        this(pooledObjectFactory, new GenericObjectPoolConfig<>());
    }
    
    public ResponseObjectPool(GenericObjectPoolConfig<ResponseObject> genericObjectPoolConfig) {
        this(new ResponseObjectPooledObjectFactory(), genericObjectPoolConfig);
    }

    public ResponseObjectPool(PooledObjectFactory<ResponseObject> pooledObjectFactory, GenericObjectPoolConfig<ResponseObject> genericObjectPoolConfig) {
        super(new GenericObjectPool<>(pooledObjectFactory, genericObjectPoolConfig));
    }

    
    /*===================================================*/
    
    
    public ResponseObject createResponse(ResponseCode code, String message) {
        return createResponse(code, null, message);
    }

    public ResponseObject createResponse(ResponseCode code, Object data) {
        return createResponse(code, data, code.getDescription());
    }

    public ResponseObject createResponse(ResponseCode code, Object data, String message) {
        return super.borrowObject().setCode(code).setData(data).setMessage(message);
    }

    public ResponseObject createSuccessResponse(String message) {
        return createResponse(SUCCESS, message);
    }

    public ResponseObject createSuccessResponse(Object data) {
        return createResponse(SUCCESS, data, "");
    }
}
