package com.ozz.demo.commons.pool2.conn;

import cn.hutool.core.lang.Assert;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@Slf4j
public class MyConnFactory extends BasePooledObjectFactory<MyProxyConn> {
    private Driver driver;
    private String jdbcUrl;
    private String validateSql;
    private Properties driverProperties;
    protected MyGenericObjectPool pool;

    public MyConnFactory(Driver driver, String jdbcUrl, String username, String password, String validateSql) {
        this.driver = driver;
        this.jdbcUrl = jdbcUrl;
        this.driverProperties = new Properties();
        if (username != null) {
            driverProperties.put("user", driverProperties.getProperty("user", username));
        }
        if (password != null) {
            driverProperties.put("password", driverProperties.getProperty("password", password));
        }
        this.validateSql = validateSql;
    }

    /**
     * 创建
     */
    @SneakyThrows
    @Override
    public MyProxyConn create() {
        log.info("create");
        Connection conn = driver.connect(jdbcUrl, driverProperties);
        MyProxyConn myConn = new MyProxyConn(conn);
        Assert.notNull(pool, "pool is empty");
        myConn.pool = pool;
        return myConn;
    }

    @Override
    public PooledObject<MyProxyConn> wrap(MyProxyConn conn) {
        log.info("wrap: {}", conn.toString());
        return new DefaultPooledObject<>(conn);
    }

    /**
     * 销毁
     */
    @SneakyThrows
    @Override
    public void destroyObject(final PooledObject<MyProxyConn> p) {
        log.info("destroyObject: {}", p.getObject().toString());
        p.getObject().closeAllStatements();
        if(!p.getObject().realIsClosed()) {
            p.getObject().realClose();
        }
    }

    /**
     * 验证对象有效性
     */
    @SneakyThrows
    @Override
    public boolean validateObject(final PooledObject<MyProxyConn> p) {
        log.info("validateObject: {}", p.getObject().toString());
        try {
            try(Statement s = p.getObject().createStatement()) {
                s.executeQuery(validateSql);
            }
        } catch (SQLException e) {
            log.error("validateObject fail", e);
            return false;
        }
        return true;
    }

    /**
     * 激活
     */
    @Override
    public void activateObject(final PooledObject<MyProxyConn> p) {
        log.info("activateObject: {}", p.getObject().toString());
    }

    /**
     * 钝化（归还连接，重置状态）
     */
    @Override
    public void passivateObject(PooledObject<MyProxyConn> p) {
        p.getObject().closeAllStatements();
        log.info("passivateObject: {}", p.getObject().toString());
    }

}
