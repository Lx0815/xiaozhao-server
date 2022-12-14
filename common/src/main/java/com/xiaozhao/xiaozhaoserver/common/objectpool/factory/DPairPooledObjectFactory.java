package com.xiaozhao.xiaozhaoserver.common.objectpool.factory;

import com.xiaozhao.xiaozhaoserver.common.utils.DPair;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.util.ObjectUtils;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-07 20:42:55
 * @modify:
 */

public class DPairPooledObjectFactory<T, U> implements PooledObjectFactory<DPair<T, U>> {

    @Override
    public void activateObject(PooledObject<DPair<T, U>> p) {
        p.getObject().clear();
    }

    @Override
    public void destroyObject(PooledObject<DPair<T, U>> p) {
        p.getObject().clear();
    }

    @Override
    public PooledObject<DPair<T, U>> makeObject() {
        return new DefaultPooledObject<>(new DPair<>());
    }

    @Override
    public void passivateObject(PooledObject<DPair<T, U>> p) {
        p.getObject().clear();
    }

    @Override
    public boolean validateObject(PooledObject<DPair<T, U>> p) {
        return !ObjectUtils.isEmpty(p.getObject()) && !ObjectUtils.isEmpty(p.getObject().getKey());
    }
}
