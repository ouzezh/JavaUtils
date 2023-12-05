package com.ozz.demo.concurrent.executor;

import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorDemo {
    @SneakyThrows
    public static void main(String[] args) {
        ExecutorService fixedPool = null;
        try {
            fixedPool = newFixedThreadPool(Runtime.getRuntime().availableProcessors(), 10);
            List<Future> fuList = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                int finalI = i;
                Future<?> fu = fixedPool.submit(() -> StaticLog.info(String.format("thread %s - task %02d",
                        Thread.currentThread().getId(), finalI)));
                fuList.add(fu);
            }
            for (Future future : fuList) {
                while (!future.isDone()) {
                    Thread.sleep(100);
                }
            }
        } finally {
            if (fixedPool != null) {
                fixedPool.shutdown(); // RUNNING->SHUTDOWN 会等待进入队列的任务会执行完
//      fixedPool.shutdownNow(); // RUNNING->STOP 中断正在执行的任务，忽略队列中的任务
                fixedPool.awaitTermination(60, TimeUnit.MINUTES);
            }
        }
    }

    private static ExecutorService newFixedThreadPool(int nThreads, int capacity) {
//        ExecutorService fixedPool = Executors.newFixedThreadPool(nThreads);
        ThreadPoolExecutor tp = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(capacity), // 任务队列，默认LinkedBlockingQueue最大长度Integer.MAX_VALUE
                Executors.defaultThreadFactory(), // 线程工厂，可以指定线程名称，在 jstack 问题排查时有帮助
                /*
                 * 任务队列拒绝策略
                 * AbortPolicy 异常 DiscardPolicy 直接丢弃 DiscardOldestPolicy 丢弃旧任务 CallerRunsPolicy 主线程执行
                 * 实际使用中可以自定义拒绝策略 保存到消息队列中 当堆积太多需要增加机器
                 */
                new ThreadPoolExecutor.AbortPolicy());
        return tp;
    }
}
