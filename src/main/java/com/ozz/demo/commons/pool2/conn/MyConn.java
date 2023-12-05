package com.ozz.demo.commons.pool2.conn;

import org.springframework.beans.factory.DisposableBean;

public interface MyConn extends AutoCloseable, DisposableBean {
    /**
     * 校验连接有效性
     */
    boolean validate();

    /**
     * 激活连接
     */
    void activate();

    /**
     * 钝化（归还连接，重置状态）
     */
    void passivate();
}
