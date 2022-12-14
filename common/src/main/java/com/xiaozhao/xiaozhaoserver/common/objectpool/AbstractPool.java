package com.xiaozhao.xiaozhaoserver.common.objectpool;

import org.apache.commons.pool2.impl.GenericObjectPool;

import java.util.Objects;

/**
 * @description: 通过组合关系和泛型，实现了对象池的工厂
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-07 20:42:55
 * @modify:
 */


public class AbstractPool<A> implements Pool<A> {

    private final GenericObjectPool<A> pool;

    private final ThreadLocal<A> threadLocal;

    protected AbstractPool(GenericObjectPool<A> pool) {
        this.pool = pool;
        this.threadLocal = new ThreadLocal<>();
    }



    /**
     * 从对象池取出对象
     *
     * @return 返回从对象池取出对象
     */
    @Override
    public A borrowObject() {
        try {
            A obj = pool.borrowObject();
            threadLocal.set(obj);
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将对象放回对象池
     */
    @Override
    public void returnObject() {
        A obj = threadLocal.get();
        if (Objects.nonNull(obj)) {
            pool.returnObject(obj);
            threadLocal.remove();
        }
    }
}
