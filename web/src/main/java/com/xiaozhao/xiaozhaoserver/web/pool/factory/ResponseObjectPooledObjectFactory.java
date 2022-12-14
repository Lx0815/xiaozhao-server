package com.xiaozhao.xiaozhaoserver.web.pool.factory;

import com.xiaozhao.xiaozhaoserver.web.response.ResponseObject;
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


public class ResponseObjectPooledObjectFactory implements PooledObjectFactory<ResponseObject> {
    @Override
    public void activateObject(PooledObject<ResponseObject> pooledObject) {
        destroyObject(pooledObject);
    }

    /**
     * PooledObjectFactory使用默认（NORMAL）DestroyMode销毁池不再需要的实例。
     * 此方法的实现必须意识到，不能保证obj将处于何种状态，并且实现应该准备好处理意外错误。
     * 此外，实现必须考虑到垃圾收集器丢失的实例可能永远不会被销毁。
     * 指定的: 接口 池对象工厂中的 destroy对象
     *
     * @param pooledObject pooledObject–包装要销毁的实例的PooledObjects
     */
    @Override
    public void destroyObject(PooledObject<ResponseObject> pooledObject) {
        if (validateObject(pooledObject)) {
            pooledObject.getObject().clear();
        }
    }

    /**
     * 创建可由池提供服务的实例，并将其包装在池管理的PooledObject中。
     *
     * @return PooledObject包装可由池提供服务的实例
     */
    @Override
    public PooledObject<ResponseObject> makeObject() {
        return new DefaultPooledObject<>(new ResponseObject());
    }

    /**
     * "钝化"对象,当调用者"归还对象"时,Pool将会"钝化对象"；钝化的言外之意,就是此"对象"暂且需要"休息"一下.
     * 如果object是一个socket,那么可以passivateObject中清除buffer,将socket阻塞;
     * 如果object是一个线程,可以在"钝化"操作中将线程sleep或者将线程中的某个对象wait.
     * 需要注意的时,activateObject和passivateObject两个方法需要对应,避免死锁或者"对象"状态的混乱.
     */
    @Override
    public void passivateObject(PooledObject<ResponseObject> pooledObject) {
        destroyObject(pooledObject);
    }

    @Override
    public boolean validateObject(PooledObject<ResponseObject> pooledObject) {
        return !ObjectUtils.isEmpty(pooledObject) && !ObjectUtils.isEmpty(pooledObject.getObject());
    }
}
