package com.ozz.demo.concurrent.executor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinDemo {

  private static final Integer MAX = 20;

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
        System.out.println(String.format("fork execute: thread=%s, startValue=%s, endValue=%s", Thread.currentThread().getId(), startValue, endValue));
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

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    System.out.println("CommonPoolParallelism = "+ForkJoinPool.getCommonPoolParallelism());

    ForkJoinPool pool = new ForkJoinPool(ForkJoinPool.getCommonPoolParallelism());
//    ForkJoinPool pool = ForkJoinPool.commonPool();
    Runtime.getRuntime().addShutdownHook(new Thread(() -> pool.shutdown()));

    // 同步获取结果
    Integer result1 = pool.invoke(new MyForkJoinTask(1, 10));
    System.out.println("result = " + result1);

    // 异步获取结果
    ForkJoinTask<Integer> taskFuture = pool.submit(new MyForkJoinTask(1, 100));
    Integer result2 = taskFuture.get();
    System.out.println("result = " + result2);

    pool.shutdown();
  }

}
