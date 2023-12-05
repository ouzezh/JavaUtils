package com.ozz.demo.commons.pool2.pool;

import com.ozz.demo.commons.pool2.conn.MyConn;
import com.ozz.demo.commons.pool2.conn.MyConnImpl;
import lombok.SneakyThrows;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Properties;

public class MyConnFactory extends BasePooledObjectFactory<MyConn> {
    private Class<MyConn> objectInterface = MyConn.class;
    private Properties props;

    public MyConnFactory(Properties props) {
        this.props = props;
    }

    /**
     * 创建
     */
    @Override
    public MyConn create() {
        return new MyConnImpl(props);
    }

    @Override
    public PooledObject<MyConn> wrap(MyConn conn) {
        return new DefaultPooledObject<>(conn);
    }

    /**
     * 销毁
     */
    @SneakyThrows
    @Override
    public void destroyObject(final PooledObject<MyConn> p) {
        p.getObject().destroy();
    }

    /**
     * 验证对象有效性
     */
    @Override
    public boolean validateObject(final PooledObject<MyConn> p) {
        return p.getObject().validate();
    }

    /**
     * 激活
     */
    @Override
    public void activateObject(final PooledObject<MyConn> p) {
        p.getObject().activate();
    }

    /**
     * 钝化（归还连接，重置状态）
     */
    @Override
    public void passivateObject(PooledObject<MyConn> p) {
        p.getObject().passivate();
    }

}
