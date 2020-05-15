package com.ozz.demo.stream;

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
    System.out.println(aa);// TRUE
    System.out.println(bb);// FALSE
    System.out.println(cc);// FALSE
    System.out.println(count);// 3
  }
}
