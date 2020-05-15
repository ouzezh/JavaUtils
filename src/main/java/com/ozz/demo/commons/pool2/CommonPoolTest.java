package com.ozz.demo.commons.pool2;

import com.ozz.demo.commons.pool2.conn.IConnection;
import com.ozz.demo.commons.pool2.pool.GenericConnectionPool;
import com.ozz.demo.commons.pool2.pool.MyConnectionFactory;
import java.util.Properties;

public class CommonPoolTest {
  public static void main(String[] args) throws Exception {
    try (GenericConnectionPool<IConnection> connPool = new GenericConnectionPool<>(new MyConnectionFactory(new Properties()))) {
      Runtime.getRuntime().addShutdownHook(new Thread(() -> connPool.close()));

      for (int i = 0; i < 9; i++) {
        IConnection conn = connPool.getConnection();
        System.out.println(String.format("brrow a connection:%s", conn));
        if (i % 2 == 0) {
          System.out.println(String.format("return a connection:%s", conn));
          conn.close();
        }
      }
    }
  }
}
