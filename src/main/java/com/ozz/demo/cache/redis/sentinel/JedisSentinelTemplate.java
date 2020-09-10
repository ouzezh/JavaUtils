package com.ozz.demo.cache.redis.sentinel;

import com.google.common.base.Strings;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Setter;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
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
    JedisSentinelTemplate jst = new JedisSentinelTemplate();
    jst.setNodes("localhost:26379,localhost2:26379");
    jst.setMaster("masterName");
    jst.setPassWord("");
    jst.setTimeOut(2000l);
    jst.afterPropertiesSet();

    System.out.println("-start-");

    System.out.println(jst.get("x"));

    // print info
    String[] node = jst.nodes.replaceAll("\\s", "").split(",")[0].split(":");
    try (Jedis jedis = new Jedis(node[0], Integer.valueOf(node[1]))) {
      List<Map<String, String>> list = jedis.sentinelSlaves(jst.master);
      System.out.println(String.format("master info: %s, %s", jst.master, jst.pool.getCurrentHostMaster()));
      for (int i = 0; i < list.size(); i++) {
        System.out.println(String.format("\tslave %s: %s", i + 1, list.get(i).get("name")));
      }
    }

    System.out.println("-end-");

    jst.destroy();
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

    pool = new JedisSentinelPool(this.master, nodeSet, poolConfig,
        Strings.emptyToNull(this.passWord));
  }

  public String get(String key) {
    try (Jedis jedis = pool.getResource()) {
      return jedis.get(key);
    }
  }
}
