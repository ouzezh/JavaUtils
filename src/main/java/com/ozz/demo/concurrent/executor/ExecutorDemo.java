package com.ozz.demo.concurrent.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorDemo {
  public static void main(String[] args) throws InterruptedException {
    ExecutorService fixedPool = newFixedThreadPool(2, 10);
    try {
      List<Future> fuList = new ArrayList<>();
      for(int i=1; i<=10; i++) {
        int finalI = i;
        Future<?> fu = fixedPool.submit(() -> System.out.println(String.format("thread %s - task %02d", Thread.currentThread().getId(), finalI)));
        fuList.add(fu);
      }
      for (Future future : fuList) {
        while(!future.isDone()) {
          Thread.sleep(100);
        }
      }
    } finally {
      fixedPool.shutdown();
    }
  }

  private static ExecutorService newFixedThreadPool(int nThreads, int capacity) {
//    ExecutorService fixedPool = Executors.newFixedThreadPool(2);
    ThreadPoolExecutor tpe = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>(capacity), new ThreadPoolExecutor.AbortPolicy());
    return tpe;
  }
}
