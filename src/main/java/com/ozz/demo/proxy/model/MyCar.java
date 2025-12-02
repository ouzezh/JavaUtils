package com.ozz.demo.proxy.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyCar implements Runnable {
  @Override
  public void run() {
    log.info("running...");
  }
}
