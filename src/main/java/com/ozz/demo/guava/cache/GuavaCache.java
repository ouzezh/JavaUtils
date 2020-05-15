package com.ozz.demo.guava.cache;

import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class GuavaCache<K, V> {
  private Cache<K, V> cache = CacheBuilder.newBuilder()
                                                           .maximumSize(100) // 设置缓存的最大容量
                                                           .expireAfterAccess(1, TimeUnit.HOURS)
                                                           .expireAfterWrite(12, TimeUnit.HOURS)
                                                           .concurrencyLevel(3) // 设置并发级别为3
                                                           .build();

  public static void main(String[] args) {
    GuavaCache<String, String> cache = new GuavaCache<>();
    cache.put("k", "v");
    System.out.println(cache.getIfPresent("k"));
  }

  public void put(K key, V value) {
    cache.put(key, value);
  }

  public V getIfPresent(K key) {
    try {
      return cache.getIfPresent(key);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
