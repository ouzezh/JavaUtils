package com.ozz.demo.concurrent.executor;

import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinDemo {

    private static final Integer MAX = 20;

    // RecursiveTask 有返回值递归, RecursiveAction 无返回值递归
    static class MyForkJoinTask extends RecursiveTask<Integer> {

        private Integer startValue;
        private Integer endValue;

        public MyForkJoinTask(Integer startValue, Integer endValue) {
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        protected Integer compute() {
            if (endValue - startValue <= MAX) {
                StaticLog.info(String.format("fork execute: thread=%s, startValue=%s, endValue=%s",
                        Thread.currentThread().getId(), startValue, endValue));
                Integer totalValue = 0;
                for (int index = this.startValue; index <= this.endValue; index++) {
                    totalValue += index;
                }
                return totalValue;
            } else {
                MyForkJoinTask subTask1 = new MyForkJoinTask(startValue, (startValue + endValue) / 2);
                subTask1.fork();
                MyForkJoinTask subTask2 = new MyForkJoinTask((startValue + endValue) / 2 + 1, endValue);
                subTask2.fork();
                return subTask1.join() + subTask2.join();
            }
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        StaticLog.info("CommonPoolParallelism = " + ForkJoinPool.getCommonPoolParallelism());

        ForkJoinPool pool = new ForkJoinPool(ForkJoinPool.getCommonPoolParallelism());
//    ForkJoinPool pool = ForkJoinPool.commonPool();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> pool.shutdown()));

        // 同步获取结果
        Integer result1 = pool.invoke(new MyForkJoinTask(1, 10));
        StaticLog.info("result = " + result1);

        // 异步获取结果
        ForkJoinTask<Integer> taskFuture = pool.submit(new MyForkJoinTask(1, 100));
        Integer result2 = taskFuture.get();
        StaticLog.info("result = " + result2);

        pool.shutdown();
    }

}
