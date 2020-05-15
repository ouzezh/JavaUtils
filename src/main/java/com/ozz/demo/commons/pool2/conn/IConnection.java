package com.ozz.demo.commons.pool2.conn;

public interface IConnection extends AutoCloseable {

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
