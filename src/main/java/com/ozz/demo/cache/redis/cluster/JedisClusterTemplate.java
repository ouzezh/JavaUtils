package com.ozz.demo.cache.redis.cluster;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.Setter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

@Setter
public class JedisClusterTemplate implements InitializingBean, DisposableBean {

  private JedisCluster jedisCluster;
  @Value("${redis.nodes}")
  private String nodes;
  @Value("${redis.timeOut}")
  private Integer timeOut;// 连接超时(ms)
  @Value("${redis.maxAttempts}")
  private Integer maxAttempts;

  public static void main(String[] args) throws Exception {
    JedisClusterTemplate jst = new JedisClusterTemplate();
    jst.setNodes("localhost:6379,localhost2:6379");
    jst.setTimeOut(2000);
    jst.setMaxAttempts(3);
    jst.afterPropertiesSet();

    System.out.println("-start-");
    System.out.println(jst.keys("*"));
    System.out.println("-end-");

    jst.destroy();
  }

  @Override
  public void destroy() throws Exception {
    if (jedisCluster != null) {
      jedisCluster.close();
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Set<HostAndPort> nodeSet = Arrays
        .stream(this.nodes.replaceAll("\\s", "").split(","))
        .map(node -> new HostAndPort(node.split(":")[0], Integer.valueOf(node.split(":")[1])))
        .collect(Collectors.toSet());
    jedisCluster = new JedisCluster(nodeSet, this.timeOut, this.maxAttempts);
  }

  public boolean exists(String key) {
    return jedisCluster.exists(key);
  }

  public void expire(String key, int seconds) {
    jedisCluster.expire(key, seconds);
  }

  public String get(String key) {
    return jedisCluster.get(key);
  }

  public void set(String key, String value) {
    jedisCluster.set(key, value);
  }

  public void setex(String key, int seconds, String value) {
    jedisCluster.setex(key, seconds, value);
  }

  public void del(String key) {
    jedisCluster.del(key);
  }

  public void delByPattern(String pattern) {
    Set<String> keys = keys(pattern);
    for (String key : keys) {
      jedisCluster.del(key);
    }
  }

  /**
   * Glob style patterns examples:
   * <p>
   * h?llo will match hello hallo hhllo
   * <p>
   * h*llo will match hllo heeeello
   * <p>
   * h[ae]llo will match hello and hallo, but not hillo
   */
  public Set<String> keys(String pattern) {
    TreeSet<String> keys = new TreeSet<>();
    Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
    for (String k : clusterNodes.keySet()) {
      JedisPool jp = clusterNodes.get(k);
      try (Jedis jedis = jp.getResource();) {
        keys.addAll(jedis.keys(pattern));
      }
    }
    return keys;
  }

  public String hget(String key, String field) {
    return jedisCluster.hget(key, field);
  }

  public Map<String, String> hgetAll(String key) {
    return jedisCluster.hgetAll(key);
  }

  public void hset(String key, String field, String value) {
    jedisCluster.hset(key, field, value);
  }

  public void hmset(String key, Map<String, String> hash) {
    jedisCluster.hmset(key, hash);
  }

}
