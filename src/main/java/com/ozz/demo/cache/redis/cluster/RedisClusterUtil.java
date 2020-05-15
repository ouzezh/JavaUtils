package com.ozz.demo.cache.redis.cluster;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import java.util.stream.Collectors;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class RedisClusterUtil {
  private static JedisCluster jedisCluster;

  static {
    Properties props = new Properties();
    try (InputStream in = RedisClusterUtil.class.getResourceAsStream(
        "/com/ozz/demo.cache.redis/cluster/redis-cluster.properties");) {
      props.load(in);
    } catch (RuntimeException e) {
      props = null;
      throw e;
    } catch (Exception e) {
      props = null;
      throw new RuntimeException(e);
    }

    int timeout = Integer.valueOf(props.getProperty("redis.timeout"));
    int maxAttempts = Integer.valueOf(props.getProperty("redis.maxAttempts"));

    Set<HostAndPort> nodeSet = Arrays
        .stream(props.getProperty("redis.nodes").replaceAll("\\s", "").split(","))
        .map(node -> new HostAndPort(node.split(":")[0], Integer.valueOf(node.split(":")[1])))
        .collect(Collectors.toSet());

    jedisCluster = new JedisCluster(nodeSet, timeout, maxAttempts);
    Runtime.getRuntime().addShutdownHook(new Thread(() -> jedisCluster.close()));
  }

  public static boolean exists(String key) {
    return jedisCluster.exists(key);
  }

  public static void expire(String key, int seconds) {
    jedisCluster.expire(key, seconds);
  }

  public static String get(String key) {
    return jedisCluster.get(key);
  }

  public static void set(String key, String value) {
    jedisCluster.set(key, value);
  }

  public static void setex(String key, int seconds, String value) {
    jedisCluster.setex(key, seconds, value);
  }

  public static void del(String key) {
    jedisCluster.del(key);
  }

  public static void delByPattern(String pattern) {
    Set<String> keys = keys(pattern);
    for (String key : keys) {
      jedisCluster.del(key);
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

  public static String hget(String key, String field) {
    return jedisCluster.hget(key, field);
  }

  public static Map<String, String> hgetAll(String key) {
    return jedisCluster.hgetAll(key);
  }

  public static void hset(String key, String field, String value) {
    jedisCluster.hset(key, field, value);
  }

  public static void hmset(String key, Map<String, String> hash) {
    jedisCluster.hmset(key, hash);
  }
}
