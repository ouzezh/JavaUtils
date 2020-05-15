package com.ozz.demo.security.encrypt.asymmetric;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.apache.commons.lang3.tuple.Pair;

/**
 * 非对称加密算法
 */
public class RSACoder {
  public static final String KEY_ALGORITHM = "RSA";
  public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

  /**
   * 用私钥对信息生成数字签名
   * 
   * @param data 加密数据
   * @param privateKey 私钥
   * 
   */
  public static String sign(byte[] data, String privateKey) {
    try {
      // 解密由base64编码的私钥
      byte[] keyBytes = Base64.getDecoder().decode(privateKey);

      // 构造PKCS8EncodedKeySpec对象
      PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

      // KEY_ALGORITHM 指定的加密算法
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

      // 取私钥匙对象
      PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

      // 用私钥对信息生成数字签名
      Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
      signature.initSign(priKey);
      signature.update(data);

      return Base64.getEncoder().encodeToString(signature.sign());
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 校验数字签名
   * 
   * @param data 加密数据
   * @param publicKey 公钥
   * @param sign 数字签名
   * 
   * @return 校验成功返回true 失败返回false
   * 
   */
  public static boolean verify(byte[] data, String publicKey, String sign) {
    try {
      // 解密由base64编码的公钥
      byte[] keyBytes = Base64.getDecoder().decode(publicKey);

      // 构造X509EncodedKeySpec对象
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

      // KEY_ALGORITHM 指定的加密算法
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

      // 取公钥匙对象
      PublicKey pubKey = keyFactory.generatePublic(keySpec);

      Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
      signature.initVerify(pubKey);
      signature.update(data);

      // 验证签名是否正常
      return signature.verify(Base64.getDecoder().decode(sign));
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 解密 用私钥解密
   * 
   */
  public static String decryptByPrivateKey(String data, String key) {
    try {
      byte[] byteData = Base64.getDecoder().decode(data);
      byte[] keyBytes = Base64.getDecoder().decode(key);
      // 取得私钥
      PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
      Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
      // 对数据解密
      Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      return new String(cipher.doFinal(byteData));
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 解密 用公钥解密
   * 
   */
  public static String decryptByPublicKey(String data, String key) {
    try {
      byte[] byteData = Base64.getDecoder().decode(data);
      byte[] keyBytes = Base64.getDecoder().decode(key);

      // 取得公钥
      X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
      Key publicKey = keyFactory.generatePublic(x509KeySpec);

      // 对数据解密
      Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
      cipher.init(Cipher.DECRYPT_MODE, publicKey);

      return new String(cipher.doFinal(byteData));
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 加密 用公钥加密
   * 
   */
  public static String encryptByPublicKey(String data, String key) {
    try {
      byte[] keyBytes = Base64.getDecoder().decode(key);


      // 取得公钥
      X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
      Key publicKey = keyFactory.generatePublic(x509KeySpec);

      // 对数据加密
      Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);

      return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 加密 用私钥加密
   * 
   */
  public static String encryptByPrivateKey(String data, String key) {
    try {
      byte[] byteData = data.getBytes();
      byte[] keyBytes = Base64.getDecoder().decode(key);

      // 取得私钥
      PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
      Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

      // 对数据加密
      Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
      cipher.init(Cipher.ENCRYPT_MODE, privateKey);

      return Base64.getEncoder().encodeToString(cipher.doFinal(byteData));
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 初始化密钥
   * 
   * @return <publicKey, privateKey>
   */
  public static Pair<String, String> initKey() {
    try {
      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
      keyPairGen.initialize(1024);

      KeyPair keyPair = keyPairGen.generateKeyPair();

      return Pair.of(keyToString(keyPair.getPublic()), keyToString(keyPair.getPrivate()));
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String keyToString(Key key) {
    return Base64.getEncoder().encodeToString(key.getEncoded());
  }

//  /**
//   * 使用N、e值还原公钥
//   */
//  public static PublicKey getPublicKey(String modulus, String publicExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
//    BigInteger bigIntModulus = new BigInteger(modulus);
//    BigInteger bigIntPrivateExponent = new BigInteger(publicExponent);
//    RSAPublicKeySpec keySpec = new RSAPublicKeySpec(bigIntModulus, bigIntPrivateExponent);
//    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//    PublicKey publicKey = keyFactory.generatePublic(keySpec);
//    return publicKey;
//  }
//
//  /**
//   * 使用N、d值还原私钥
//   */
//  public static PrivateKey getPrivateKey(String modulus, String privateExponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
//    BigInteger bigIntModulus = new BigInteger(modulus);
//    BigInteger bigIntPrivateExponent = new BigInteger(privateExponent);
//    RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(bigIntModulus, bigIntPrivateExponent);
//    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//    PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
//    return privateKey;
//  }
}
