package com.ozz.demo.security.encrypt.symmetric;

import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AesEncryptUtil {
  private String KEY_ALGORITHM = "AES";

  // 加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式
  private static String CIPHER_ALGORITHM = "AES/ECB/PKCS7Padding";

  /**
   * 生成密钥
   */
  public String initkey() {
    try {
      KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM); // 实例化密钥生成器
      kg.init(128); // 初始化密钥生成器:AES要求密钥长度为128,192,256位
      SecretKey secretKey = kg.generateKey(); // 生成密钥
      return Base64.getEncoder().encodeToString(secretKey.getEncoded()); // 获取二进制密钥编码形式
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Cipher initCipher(int encryptMode, String key) {
    try {
      // 使用PKCS7Padding填充方式,这里就得这么写了(即调用BouncyCastle组件实现)
      if (CIPHER_ALGORITHM.contains("PKCS7Padding")) {
        Security.addProvider(new BouncyCastleProvider());
      }

      SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), KEY_ALGORITHM);
      Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

      cipher.init(encryptMode, keySpec); // 初始化Cipher对象，设置为加密模式

      return cipher;
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 加密数据
   * 
   * @param key 密钥
   * @param data 待加密数据
   * @return 加密后的数据
   */
  public String encrypt(String key, String data) {
    try {
      Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, key);
      return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes())); // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 解密数据
   * 
   * @param key 密钥
   * @param data 待解密数据
   * @return 解密后的数据
   */
  public String decrypt(String key, String data) {
    try {
      Cipher cipher = initCipher(Cipher.DECRYPT_MODE, key);
      return new String(cipher.doFinal(Base64.getDecoder().decode(data))); // 执行解密操作
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) {
    AesEncryptUtil demo = new AesEncryptUtil();

    String source = "Hello World!";
    System.out.println("原文：" + source);

    String key = demo.initkey();
    System.out.println("密钥：" + key);

    String encryptData = demo.encrypt(key, source);
    System.out.println("密文：" + encryptData);

    String decryptData = demo.decrypt(key, encryptData);
    System.out.println("明文: " + decryptData);

    if (!source.equals(decryptData)) {
      throw new RuntimeException("解密失败...");
    }
  }
}
