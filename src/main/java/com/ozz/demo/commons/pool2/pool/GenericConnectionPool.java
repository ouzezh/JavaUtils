package com.ozz.demo.commons.pool2.pool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.time.Duration;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class GenericConnectionPool<T extends AutoCloseable> implements AutoCloseable {
  private GenericObjectPool<T> pool;

  public GenericConnectionPool(PooledObjectFactory<T> factory) {
    GenericObjectPoolConfig<T> config = new GenericObjectPoolConfig<>();
    config.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL);
    config.setMaxIdle(config.getMaxTotal());// 最大空闲的数量
    config.setMaxWaitMillis(60000);// 连接超时时间
    config.setTestOnCreate(false);
    config.setTestOnBorrow(false);
    config.setTestOnReturn(false);
    config.setTestWhileIdle(true);// 空闲检测时校验有效性
    config.setTimeBetweenEvictionRunsMillis(Duration.ofMinutes(30).toMillis());// 空闲检测周期（必须设置，否则空闲连接永远不会过期）
    config.setMinEvictableIdleTimeMillis(Duration.ofMinutes(30).toMillis());// 空闲检测时，空闲时长高于此值则移除

    pool = new GenericObjectPool<T>(factory, config);
  }

  @Override
  public void close() {
    if (pool != null) {
      pool.close();
      pool = null;
    }
  }

  public T getConnection() {
    try {
      if (pool == null) {
        throw new IllegalStateException("pool is closed");
      }
      T conn = pool.borrowObject();
      return getDynamicProxy(getTClass(), new GenericInvocationHandler<T>(conn, pool));
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("hiding")
  private <T> T getDynamicProxy(Class<T> cls, InvocationHandler handler) {
    @SuppressWarnings("unchecked")
    T f = (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[] {cls}, handler);
    return f;

    // @SuppressWarnings("rawtypes")
    // Class proxyClass = Proxy.getProxyClass(cls.getClassLoader(), new Class[] {cls});
    // @SuppressWarnings("unchecked")
    // T f1 = (T) proxyClass.getConstructor(new Class[] {InvocationHandler.class}).newInstance(new
    // Object[] {handler});
    // return f1;
  }

  @SuppressWarnings("unchecked")
  private Class<T> getTClass() {
    if (pool != null) {
      Type genericSuperclass = pool.getFactory().getClass().getGenericSuperclass();
      if (genericSuperclass instanceof ParameterizedType) {
        Type[] actualTypeArguments =
            ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        if (actualTypeArguments != null && actualTypeArguments.length > 0) {
          return (Class<T>) actualTypeArguments[0];
        }
      }
    }
    return null;
  }

  private static class GenericInvocationHandler<T extends AutoCloseable> implements InvocationHandler {
    private T target;
    private GenericObjectPool<T> pool;

    public GenericInvocationHandler(T target, GenericObjectPool<T> pool) {
      super();
      this.target = target;
      this.pool = pool;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
      if (method.getName().equals("close")) {
        if (target != null && pool != null) {
          pool.returnObject(target);
        }
        target = null;
        pool = null;
        return Void.TYPE;
      } else {
        if (target == null) {
          throw new IllegalStateException("connection is closed");
        }
        return method.invoke(target, args);
      }
    }
  }
}
