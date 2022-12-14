package com.xiaozhao.xiaozhaoserver.common.objectpool.impl;

import com.xiaozhao.xiaozhaoserver.common.objectpool.AbstractPool;
import com.xiaozhao.xiaozhaoserver.common.objectpool.factory.DPairPooledObjectFactory;
import com.xiaozhao.xiaozhaoserver.common.utils.DPair;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-07 20:42:55
 * @modify:
 */

public class DPairPool<T, U> extends AbstractPool<DPair<T, U>> {

    public DPairPool() {
        this(new DPairPooledObjectFactory<>());
    }

    public DPairPool(PooledObjectFactory<DPair<T, U>> pooledObjectFactory) {
        this(pooledObjectFactory, new GenericObjectPoolConfig<>());
    }

    public DPairPool(GenericObjectPoolConfig<DPair<T, U>> genericObjectPoolConfig) {
        this(new DPairPooledObjectFactory<>(), genericObjectPoolConfig);
    }

    public DPairPool(PooledObjectFactory<DPair<T, U>> pooledObjectFactory, GenericObjectPoolConfig<DPair<T, U>> genericObjectPoolConfig) {
        super(new GenericObjectPool<>(pooledObjectFactory, genericObjectPoolConfig));
    }

    @Override
    public DPair<T, U> borrowObject() {
        return super.borrowObject();
    }
}
