package com.ozz.demo.commons.pool2.pool;

import org.springframework.beans.factory.DisposableBean;

public interface MyConnPool<T extends AutoCloseable> extends AutoCloseable, DisposableBean {
    /**
     * 从连接池中取出
     */
    T borrowObject();
}
