package com.ozz.demo.cache.redis.shard;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Setter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Setter
public class RedisShardTemplate implements InitializingBean, DisposableBean {

  private ShardedJedisPool jedisPool;
  @Value("${redis.nodes}")
  private String nodes;
  @Value("${redis.timeOut}")
  private Integer timeOut;

  public static void main(String[] args) throws Exception {
    System.out.println("-start-");
    RedisShardTemplate rst = new RedisShardTemplate();
    rst.setNodes("localhost:6379,localhost2:6379");
    rst.setTimeOut(30000);
    rst.afterPropertiesSet();

    System.out.println(rst.get("x"));

    rst.destroy();
    System.out.println("-end-");
  }

  @Override
  public void destroy() throws Exception {
    if (jedisPool != null && !jedisPool.isClosed()) {
      jedisPool.close();
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    //连接池配置
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(10);
    poolConfig.setMaxIdle(1);
    poolConfig.setMaxWaitMillis(200);
    poolConfig.setTestOnBorrow(false);
    poolConfig.setTestOnReturn(false);

    //分片信息
    List<JedisShardInfo> shards = Arrays
        .stream(this.nodes.replaceAll("\\s", "").split(","))
        .map(node -> new JedisShardInfo(node.split(":")[0], Integer.valueOf(node.split(":")[1]),
            this.timeOut))
        .collect(Collectors.toList());

    //根据分片信息创建连接池
    jedisPool = new ShardedJedisPool(poolConfig, shards);
  }

  public String get(String key) {
    try (ShardedJedis jedis = jedisPool.getResource();) {
      return jedis.get(key);
    }
  }
}
