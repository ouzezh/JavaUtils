package com.ozz.demo.security.encrypt.digest;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;

import java.io.File;

/**
 * 数据摘要
 */
public class DigestDemo {
    @SneakyThrows
    public static String md5(File file) {
        return SecureUtil.md5(file);
    }

  public static String md5(String data) {
      return SecureUtil.md5(data);
  }

    public static void main(String[] args) {
        String data = "my test";
        StaticLog.info(md5(data));
    }
}
