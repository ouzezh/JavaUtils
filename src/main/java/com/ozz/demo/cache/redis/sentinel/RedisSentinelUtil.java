package com.ozz.demo.cache.redis.sentinel;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class RedisSentinelUtil {
  private static JedisSentinelPool pool;

  static {
    Properties props = new Properties();
    try (InputStream in = RedisSentinelUtil.class.getResourceAsStream(
        "/com/ozz/demo/cache/redis/sentinel/redis-sentinel.properties");) {
      props.load(in);
    } catch (RuntimeException e) {
      props = null;
      throw e;
    } catch (Exception e) {
      props = null;
      throw new RuntimeException(e);
    }

    String master = props.getProperty("redis.master");
    String password = props.getProperty("redis.passord");

    GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    poolConfig.setMaxWaitMillis(Integer.valueOf(props.getProperty("redis.timeout")));

    Set<String> nodeSet = Arrays
        .stream(props.getProperty("redis.nodes").replaceAll("\\s", "").split(","))
        .collect(Collectors.toSet());

    if (StringUtils.isEmpty(password)) {
      pool = new JedisSentinelPool(master, nodeSet, poolConfig);
    } else {
      pool = new JedisSentinelPool(master, nodeSet, poolConfig, password);
    }
    Runtime.getRuntime().addShutdownHook(new Thread(() -> pool.close()));
  }

  public static void main(String[] args) {
    System.out.println("-start-");
    System.out.println(RedisSentinelUtil.getCurrentHostMaster());
    System.out.println("-end-");
  }

  public static boolean exists(String key) {
    try (Jedis jedis = pool.getResource();) {
      return jedis.exists(key);
    }
  }

  public static void expire(String key, int seconds) {
    try (Jedis jedis = pool.getResource();) {
      jedis.expire(key, seconds);
    }
  }

  public static String get(String key) {
    try (Jedis jedis = pool.getResource();) {
      return jedis.get(key);
    }
  }

  public static void set(String key, String value) {
    try (Jedis jedis = pool.getResource();) {
      jedis.set(key, value);
    }
  }

  public static void setex(String key, int seconds, String value) {
    try (Jedis jedis = pool.getResource();) {
      jedis.setex(key, seconds, value);
    }
  }

  public static void del(String key) {
    try (Jedis jedis = pool.getResource();) {
      jedis.del(key);
    }
  }

  public static void delByPattern(String pattern) {
    try (Jedis jedis = pool.getResource();) {
      Set<String> keys = keys(pattern);
      for (String key : keys) {
        jedis.del(key);
      }
    }
  }

  /**
   * Glob style patterns examples:
   * 
   * h?llo will match hello hallo hhllo
   * 
   * h*llo will match hllo heeeello
   * 
   * h[ae]llo will match hello and hallo, but not hillo
   */
  public static Set<String> keys(String pattern) {
    try (Jedis jedis = pool.getResource();) {
      return jedis.keys(pattern);
    }
  }

  public static HostAndPort getCurrentHostMaster() {
    return pool.getCurrentHostMaster();
  }

  public static String hget(String key, String field) {
    try (Jedis jedis = pool.getResource();) {
      return jedis.hget(key, field);
    }
  }

  public static Map<String, String> hgetAll(String key) {
    try (Jedis jedis = pool.getResource();) {
      return jedis.hgetAll(key);
    }
  }

  public static void hset(String key, String field, String value) {
    try (Jedis jedis = pool.getResource();) {
      jedis.hset(key, field, value);
    }
  }

  public static void hmset(String key, Map<String, String> hash) {
    try (Jedis jedis = pool.getResource();) {
      jedis.hmset(key, hash);
    }
  }
}
