package com.ozz.demo.proxy.model;

import cn.hutool.log.StaticLog;

public class MyCar implements Runnable {
  @Override
  public void run() {
    StaticLog.info("running...");
  }
}
