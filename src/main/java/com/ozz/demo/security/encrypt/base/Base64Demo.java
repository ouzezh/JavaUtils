package com.ozz.demo.security.encrypt.base;


import cn.hutool.core.codec.Base64;

/**
 * 最常见的用于传输8Bit字节码的编码方式之一，Base64就是一种基于64个可打印字符来表示二进制数据的方法
 */
public class Base64Demo {
  public static String encode(byte[] source) {
    return Base64.encode(source);
  }

  public static byte[] decode(String in) {
    return Base64.decode(in);
  }

}
