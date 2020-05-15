package com.ozz.demo.security.encrypt.base;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URL编码
 */
public class UrlcodeDemo {
  public static String urlEncode(String s) {
    try {
      return URLEncoder.encode(s, "UTF-8");
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String urlDecode(String s) {
    try {
      return URLDecoder.decode(s, "UTF-8");
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    String src = "你好 World!";
    System.out.println("原文：" + src);

    String encode = urlEncode(src);
    System.out.println("加密：" + encode);

    String decode = urlDecode(encode);
    System.out.println("解密：" + decode);
  }
}
