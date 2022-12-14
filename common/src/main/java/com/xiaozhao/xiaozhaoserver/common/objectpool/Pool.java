package com.xiaozhao.xiaozhaoserver.common.objectpool;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-07 20:42:55
 * @modify:
 */

/**
 * 对象池的根接口
 * @param <A>
 */
public interface Pool<A> {

    A borrowObject();

    void returnObject();

}
