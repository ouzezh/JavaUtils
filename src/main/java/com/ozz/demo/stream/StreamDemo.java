package com.ozz.demo.stream;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;

import java.util.Arrays;
import java.util.List;

public class StreamDemo {

  public static void main(String[] args) {
    test1();
  }

  private static void test1() {
    List<String> strs = Arrays.asList("a", "a", "a", "b");
    boolean aa = strs.stream().anyMatch(str -> str.equals("a"));
    boolean bb = strs.stream().allMatch(str -> str.equals("a"));
    boolean cc = strs.stream().noneMatch(str -> str.equals("a"));
    long count = strs.stream().filter(str -> str.equals("a")).count();
    StaticLog.info(StrUtil.toString(aa));// TRUE
    StaticLog.info(StrUtil.toString(bb));// FALSE
    StaticLog.info(StrUtil.toString(cc));// FALSE
    StaticLog.info(StrUtil.toString(count));// 3
  }
}
