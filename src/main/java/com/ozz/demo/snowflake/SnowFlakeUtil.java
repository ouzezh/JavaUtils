package com.ozz.demo.snowflake;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.littlenb.snowflake.sequence.IdGenerator;
import com.littlenb.snowflake.support.ElasticIdGeneratorFactory;
import com.littlenb.snowflake.support.MillisIdGeneratorFactory;
import com.littlenb.snowflake.support.SecondsIdGeneratorFactory;
import com.littlenb.snowflake.worker.WorkerIdAssigner;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 雪花算法
 * <p>
 * https://github.com/littlenb/snowflake
 */
@Slf4j
public class SnowFlakeUtil {
    @SneakyThrows
    public static void main(String[] args) {
        IdGenerator idGenerator = getMillisIdGenerator(DateUtil.parseDate("2022-03-10"), 0);
        long id = idGenerator.nextId();
        log.info(StrUtil.toString(id));
        log.info(idGenerator.parse(id));
    }

    public static IdGenerator getIdGenerator(Date epochTimestamp, WorkerIdAssigner workerIdAssigner) {
        ElasticIdGeneratorFactory elasticFactory = new ElasticIdGeneratorFactory();
        // TimeBits + WorkerBits + SeqBits = 64 -1
        elasticFactory.setTimeBits(41);
        elasticFactory.setWorkerBits(10);
        elasticFactory.setSeqBits(12);
        // 时间单位
        elasticFactory.setTimeUnit(TimeUnit.MILLISECONDS);
        // 时间初始值，用于计算时间偏移量
        elasticFactory.setEpochTimestamp(epochTimestamp.getTime());
        return elasticFactory.create(workerIdAssigner);
    }

    /**
     * 默认毫秒策略
     * @param epochTimestamp 时间初始值
     * @param workerId 0-1023
     */
    public static IdGenerator getMillisIdGenerator(Date epochTimestamp, long workerId) {
        return new MillisIdGeneratorFactory(epochTimestamp.getTime()).create(workerId);
    }

    public static IdGenerator getSecondsIdGenerator(Date epochTimestamp, long workerId) {
        return new SecondsIdGeneratorFactory(TimeUnit.SECONDS.convert(epochTimestamp.getTime(), TimeUnit.MILLISECONDS)).create(workerId);
    }

}
