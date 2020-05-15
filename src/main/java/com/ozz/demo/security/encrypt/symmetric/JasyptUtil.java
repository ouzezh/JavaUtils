package com.ozz.demo.security.encrypt.symmetric;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * Java simplified encryption
 * 
 * 默认算法：StandardPBEByteEncryptor.DEFAULT_ALGORITHM="PBEWithMD5AndDES"
 */
public class JasyptUtil {
  public static String encrypt(String password, String message) {
    PBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    encryptor.setPassword(password);
    return encryptor.encrypt(message);
  }

  public static String decrypt(String password, String encryptedMessage) {
    PBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
    encryptor.setPassword(password);
    return encryptor.decrypt(encryptedMessage);
  }

  public static void main(String[] args) throws Exception {
    String source = "Hello World!";
    System.out.println("原文：" + source);

    String key = "2BTRS4Q6cmDsXOvquQq0qmjWtqvKnHEW";
    System.out.println("密钥：" + key);

    String encryptData = encrypt(key, source);
    System.out.println("密文：" + encryptData);

    String decryptData = decrypt(key, encryptData);
    System.out.println("明文: " + decryptData);

    if (!source.equals(decryptData)) {
      throw new RuntimeException("解密失败...");
    }
  }
}
