package com.ozz.demo.security.encrypt.asymmetric;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class RSACoderTest {
  public static void main(String[] args) {
    RSACoderTest test = new RSACoderTest();
    Pair<String, String> keyPair = test.initKey();

    test.testEncryptByPublicKey(keyPair);// 公钥加密
    String encodedData = test.testEncryptByPrivateKey(keyPair);// 私钥加密
    test.testSign(keyPair, encodedData);// 签名
  }

  public Pair<String, String> initKey() {
    Pair<String, String> keyPair = RSACoder.initKey();

    System.out.println("公钥：" + keyPair.getLeft());
    System.out.println("私钥：" + keyPair.getRight());
    return keyPair;
  }

  public void testEncryptByPublicKey(Pair<String, String> keyPair) {
    System.out.println("--------\n公钥加密——私钥解密");
    String inputStr = "abc";

    String encodedData = RSACoder.encryptByPublicKey(inputStr, keyPair.getLeft());
    System.out.println("密文: " + new String(encodedData));

    String outputStr = RSACoder.decryptByPrivateKey(encodedData, keyPair.getRight());

    System.out.println("加密前: " + inputStr + "\n" + "解密后: " + outputStr);

    if (!StringUtils.equals(inputStr, outputStr)) {
      throw new RuntimeException("解密数据错误");
    }
  }

  public String testEncryptByPrivateKey(Pair<String, String> keyPair) {
    System.out.println("--------\n私钥加密——公钥解密");
    String inputStr = "def";

    String encodedData = RSACoder.encryptByPrivateKey(inputStr, keyPair.getRight());
    System.out.println("密文: " + new String(encodedData));

    String outputStr = RSACoder.decryptByPublicKey(encodedData, keyPair.getLeft());

    System.out.println("加密前: " + inputStr + "\n" + "解密后: " + outputStr);

    if (!StringUtils.equals(inputStr, outputStr)) {
      throw new RuntimeException("解密数据错误");
    }
    return encodedData;
  }

  public void testSign(Pair<String, String> keyPair, String encodedData) {
    System.out.println("--------\n私钥签名——公钥验证签名");
    // 产生签名
    String sign = RSACoder.sign(encodedData.getBytes(), keyPair.getRight());
    System.out.println("签名:\r" + sign);

    // 验证签名
    boolean status = RSACoder.verify(encodedData.getBytes(), keyPair.getLeft(), sign);
    System.out.println("状态:\r" + status);

    if (!status) {
      throw new RuntimeException("验证签名错误");
    }
  }

}
