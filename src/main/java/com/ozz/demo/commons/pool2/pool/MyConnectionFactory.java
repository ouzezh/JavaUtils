package com.ozz.demo.commons.pool2.pool;

import java.util.Properties;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import com.ozz.demo.commons.pool2.conn.IConnection;
import com.ozz.demo.commons.pool2.conn.ConnectionImpl;

public class MyConnectionFactory extends BasePooledObjectFactory<IConnection> {
  private Properties props;
  public MyConnectionFactory(Properties props) {
    this.props = props;
  }
  /**
   * 创建
   */
  @Override
  public IConnection create() {
    return new ConnectionImpl(props);
  }
  @Override
  public PooledObject<IConnection> wrap(IConnection conn) {
    return new DefaultPooledObject<>(conn);
  }
  /**
   * 销毁
   */
  @Override
  public void destroyObject(final PooledObject<IConnection> p) throws Exception  {
    p.getObject().close();
  }
  /**
   * 验证对象有效性
   */
  @Override
  public boolean validateObject(final PooledObject<IConnection> p) {
    return p.getObject().validate();
  }
  /**
   * 激活
   */
  @Override
  public void activateObject(final PooledObject<IConnection> p) {
    p.getObject().activate();
  }
  /**
   * 钝化（归还连接，重置状态）
   */
  @Override
  public void passivateObject(PooledObject<IConnection> p) {
    p.getObject().passivate();
  }
}
