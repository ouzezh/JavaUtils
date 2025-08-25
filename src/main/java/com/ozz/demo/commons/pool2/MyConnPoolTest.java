package com.ozz.demo.commons.pool2;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.ozz.demo.commons.pool2.conn.MyConnFactory;
import com.ozz.demo.commons.pool2.conn.MyGenericObjectPool;
import com.ozz.demo.commons.pool2.conn.MyProxyConn;
import com.p6spy.engine.spy.P6SpyDriver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
public class MyConnPoolTest {
    @SneakyThrows
    public static void main(String[] args) {
        MyConnFactory factory = new MyConnFactory(new P6SpyDriver()
                , StrUtil.format("jdbc:p6spy:sqlite:C:/Users/ouzezh/Desktop/temp/{}/MyConnPool.db", LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.SIMPLE_MONTH_PATTERN))
                , null, null, "select 1");

        GenericObjectPoolConfig<MyProxyConn> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(GenericObjectPoolConfig.DEFAULT_MAX_TOTAL);
        poolConfig.setMinIdle(1);
        poolConfig.setMaxIdle(poolConfig.getMaxTotal());// 最大空闲的数量
        poolConfig.setMaxWait(Duration.ofSeconds(300));// 连接超时时间
        poolConfig.setTestOnCreate(false);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(true);// 空闲检测时校验有效性
        poolConfig.setTimeBetweenEvictionRuns(Duration.ofMinutes(5));// 空闲检测周期（必须设置，否则空闲连接永远不会过期）
        poolConfig.setMinEvictableIdleDuration(Duration.ofMinutes(20));// 空闲检测时，空闲时长高于此值则移除

        AbandonedConfig abandonedConfig = new AbandonedConfig();
        abandonedConfig.setRemoveAbandonedOnBorrow(false);
        abandonedConfig.setRemoveAbandonedOnMaintenance(true);
        abandonedConfig.setRemoveAbandonedTimeout(Duration.ofMinutes(30));
        abandonedConfig.setLogAbandoned(true);
        abandonedConfig.setRequireFullStackTrace(true);
        abandonedConfig.setUseUsageTracking(true);// 设置为true时，requireFullStackTrace=true才生效

        try (MyGenericObjectPool<MyProxyConn> pool = new MyGenericObjectPool<>(factory, poolConfig)) {
//      Runtime.getRuntime().addShutdownHook(new Thread(() -> pool.close()));
            for (int i = 1; i <= 20; i++) {
                log.info("test {} start", i);
                try(MyProxyConn conn = pool.borrowObject()) {
                    log.info("borrow a connection: {}", conn);
                }
                log.info("test {} end", i);
            }
        }
    }
}
