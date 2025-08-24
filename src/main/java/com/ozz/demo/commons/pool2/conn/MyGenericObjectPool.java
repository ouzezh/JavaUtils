package com.ozz.demo.commons.pool2.conn;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class MyGenericObjectPool<T> extends GenericObjectPool<T> {
    public MyGenericObjectPool(PooledObjectFactory<T> factory) {
        super(factory);
        if(factory instanceof MyConnFactory) {
            ((MyConnFactory) factory).pool = this;
        }
    }

    public MyGenericObjectPool(PooledObjectFactory<T> factory, GenericObjectPoolConfig<T> config) {
        super(factory, config);
        if(factory instanceof MyConnFactory) {
            ((MyConnFactory) factory).pool = this;
        }
    }

    public MyGenericObjectPool(PooledObjectFactory<T> factory, GenericObjectPoolConfig<T> config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
        if(factory instanceof MyConnFactory) {
            ((MyConnFactory) factory).pool = this;
        }
    }

    @Override
    public T borrowObject() throws Exception {
        return super.borrowObject();
    }
}
