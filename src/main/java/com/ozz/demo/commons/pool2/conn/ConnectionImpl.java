package com.ozz.demo.commons.pool2.conn;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionImpl implements IConnection {
  private static AtomicInteger idCount = new AtomicInteger(1);
  private int id;
  public ConnectionImpl(Properties props) {
    this.id = idCount.getAndAdd(1);
  }
  @Override
  public String toString() {
    return String.format("{id=%s}", id);
  }
  @Override
  public void close() {
    System.out.println("close "+id);
  }
  public boolean validate() {
    return true;
  }
  public void activate() {
    System.out.println("activate "+id);
  }
  public void passivate() {
    System.out.println("passivate "+id);
  }
}
