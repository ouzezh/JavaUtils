package com.ozz.demo.commons.pool2;

import cn.hutool.log.StaticLog;
import com.ozz.demo.commons.pool2.conn.MyConn;
import com.ozz.demo.commons.pool2.pool.MyConnFactory;
import com.ozz.demo.commons.pool2.pool.MyConnPool;
import com.ozz.demo.commons.pool2.pool.MyConnPoolImpl;
import lombok.SneakyThrows;

import java.util.Properties;

public class MyConnPoolTest {
    @SneakyThrows
    public static void main(String[] args) {
        try (MyConnPool<MyConn> pool =
                     new MyConnPoolImpl<>(new MyConnFactory(new Properties()))) {
//      Runtime.getRuntime().addShutdownHook(new Thread(() -> pool.close()));
            for (int i = 1; i <= 10; i++) {
                MyConn conn = pool.borrowObject();
                StaticLog.info(String.format("borrow a connection:%s", conn));
                if (i % 2 == 0) {
                    StaticLog.info(String.format("return a connection:%s", conn));
                    conn.close();
                }
            }
        }
    }
}
