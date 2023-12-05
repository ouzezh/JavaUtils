package com.ozz.demo.commons.pool2.pool;

import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.lang.reflect.*;
import java.time.Duration;

public class MyConnPoolImpl<T extends AutoCloseable> implements MyConnPool {
    private GenericObjectPool<T> pool;
    private Class<T> objectType;

    public MyConnPoolImpl(PooledObjectFactory<T> factory) {
        GenericObjectPoolConfig<T> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL);
        config.setMaxIdle(config.getMaxTotal());// 最大空闲的数量
        config.setMaxWait(Duration.ofSeconds(60));// 连接超时时间
        config.setTestOnCreate(false);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        config.setTestWhileIdle(true);// 空闲检测时校验有效性
        config.setTimeBetweenEvictionRuns(Duration.ofMinutes(30));// 空闲检测周期（必须设置，否则空闲连接永远不会过期）
        config.setMinEvictableIdleTime(Duration.ofMinutes(30));// 空闲检测时，空闲时长高于此值则移除

        Type genericSuperclass = factory.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments =
                    ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                objectType = (Class<T>) actualTypeArguments[0];
            }
        }

        pool = new GenericObjectPool<T>(factory, config);
    }

    @SneakyThrows
    @Override
    public T borrowObject() {
        T conn = pool.borrowObject();
        StaticLog.debug("borrowObject {}", conn.toString());
        return (T) Proxy.newProxyInstance(objectType.getClassLoader(), new Class[]{objectType},
                new MyInvocationHandler<T>(conn, pool));
    }

    @Override
    public void close() {
        StaticLog.debug("shut down " + this.getClass().getName());
        if (pool != null) {
            pool.close();
        }
    }

    @Override
    public void destroy() {
        close();
    }

    private static class MyInvocationHandler<T extends AutoCloseable> implements InvocationHandler {
        private T target;
        private GenericObjectPool<T> pool;

        public MyInvocationHandler(T target, GenericObjectPool<T> pool) {
            super();
            this.target = target;
            this.pool = pool;
        }

        @SneakyThrows
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            if ("close".equals(method.getName())) {
                StaticLog.debug("returnObject {}", target.toString());
                pool.returnObject(target);
                return Void.TYPE;
            } else {
                return method.invoke(target, args);
            }
        }
    }
}
