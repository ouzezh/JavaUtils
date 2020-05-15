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
    ForkJoinPool pool = new ForkJoinPool(3);
    Runtime.getRuntime().addShutdownHook(new Thread(() -> pool.shutdown()));

    ForkJoinTask<Integer> taskFuture = pool.submit(new MyForkJoinTask(1, 100));
    Integer result = taskFuture.get();
    System.out.println("result = " + result);

    pool.shutdown();
  }

}
