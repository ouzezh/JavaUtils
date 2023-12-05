package com.ozz.demo.commons.pool2.conn;

import cn.hutool.log.StaticLog;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class MyConnImpl implements MyConn {
    private static AtomicInteger idCount = new AtomicInteger(1);
    private int id;

    public MyConnImpl(Properties props) {
        this.id = idCount.getAndAdd(1);
    }

    @Override
    public String toString() {
        return String.format("{id=%s}", id);
    }

    @Override
    public void close() {
        StaticLog.debug("close {}", toString());
    }

    @Override
    public void destroy() {
        StaticLog.debug("destroy {}", toString());
        close();
    }

    @Override
    public boolean validate() {
        StaticLog.debug("validate {}", toString());
//    return !realConn.isClosed() && realConnection.isValid(60);
        return true;
    }

    @Override
    public void activate() {
        StaticLog.debug("activate {}", toString());
    }

    @Override
    public void passivate() {
        StaticLog.debug("passivate {}", toString());
//        if (!realConn.getAutoCommit()) {
//            realConn.rollback();
//        }
    }

}
