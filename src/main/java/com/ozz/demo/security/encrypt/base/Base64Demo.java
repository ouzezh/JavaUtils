package com.ozz.demo.security.encrypt.base;

import java.util.Base64;

/**
 * 最常见的用于传输8Bit字节码的编码方式之一，Base64就是一种基于64个可打印字符来表示二进制数据的方法
 */
public class Base64Demo {
  public static String encode(byte[] src) {
    return Base64.getEncoder().encodeToString(src);
  }

  public static byte[] decode(String src) {
    return Base64.getDecoder().decode(src);
  }

  public static void main(String[] args) {
    String src = "Hello World!";
    System.out.println("原文：" + src);

    String encode = encode(src.getBytes());
    System.out.println("加密,字节码->打印字符：" + encode);

    String decode = new String(decode(encode));
    System.out.println("解密,打印字符->字节码：" + decode);
  }
}
