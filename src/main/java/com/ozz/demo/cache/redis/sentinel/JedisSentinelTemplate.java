package com.ozz.demo.cache.redis.sentinel;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisSentinelPool;

@Setter
public class JedisSentinelTemplate implements InitializingBean, DisposableBean {

  private JedisSentinelPool pool;
  @Value("${redis.nodes}")
  private String nodes;
  @Value("${redis.master}")
  private String master;
  @Value("${redis.passWord}")
  private String passWord;
  @Value("${redis.timeOut}")
  private Long timeOut;

  public static void main(String[] args) throws Exception {
    System.out.println("-start-");
    JedisSentinelTemplate jst = new JedisSentinelTemplate();
    jst.setNodes("localhost:26379,localhost2:26379");
    jst.setMaster("masterName");
    jst.setPassWord("");
    jst.setTimeOut(30000l);
    jst.afterPropertiesSet();

    System.out.println(jst.getCurrentHostMaster());

    jst.destroy();
    System.out.println("-end-");
  }

  @Override
  public void destroy() throws Exception {
    if (pool != null && !pool.isClosed()) {
      pool.close();
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    poolConfig.setMaxWaitMillis(this.timeOut);

    Set<String> nodeSet = Arrays
        .stream(this.nodes.replaceAll("\\s", "").split(","))
        .collect(Collectors.toSet());

    if (StringUtils.isEmpty(this.passWord)) {
      pool = new JedisSentinelPool(this.master, nodeSet, poolConfig);
    } else {
      pool = new JedisSentinelPool(this.master, nodeSet, poolConfig, this.passWord);
    }
  }

  public HostAndPort getCurrentHostMaster() {
    return pool.getCurrentHostMaster();
  }
}
