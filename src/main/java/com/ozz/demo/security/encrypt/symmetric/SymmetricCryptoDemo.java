package com.ozz.demo.security.encrypt.symmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 对称加密
 *
 * https://www.hutool.cn/docs/#/crypto/%E5%AF%B9%E7%A7%B0%E5%8A%A0%E5%AF%86-SymmetricCrypto
 */
@Slf4j
public class SymmetricCryptoDemo {
    @SneakyThrows
    public static void main(String[] args) {
        String data = "test中文";

        // 生成密钥
        String key = Base64.encode(SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded());
        log.info("秘钥: " + key);

        // 加密解密
        String encryptHex = encryptHex(data, key);
        String decryptStr = decryptStr(encryptHex, key);
        // 打印
        log.info("密文: " + encryptHex);
        log.info("明文: " + decryptStr);
        Assert.isTrue(data.equals(decryptStr));

        // 带初始化向量(iv)加密解密：前端加密密文初始化向量通常是加密数据的前16个字节
        String encryptHexCBC = encryptHexCBC(data, key);
        String decryptStrCBC = decryptStrCBC(encryptHexCBC, key);
        // 打印
        log.info("带iv密文: " + encryptHexCBC);
        log.info("带iv明文: " + decryptStrCBC);
        Assert.isTrue(data.equals(decryptStrCBC));

    }

    private static String decryptStrCBC(String data, String key) {
        // 将Base64编码的密文解码
        byte[] cipherBytes = Base64.decode(data);

        // 获取IV
        byte[] ivBytes = new byte[16];
        System.arraycopy(cipherBytes, 0, ivBytes, 0, ivBytes.length);

        // 获取密文
        byte[] cipherText = new byte[cipherBytes.length - 16];
        System.arraycopy(cipherBytes, 16, cipherText, 0, cipherText.length);

        AES aes = new AES("CBC", "PKCS7Padding", Base64.decode(key), ivBytes);
        return new String(aes.decrypt(cipherText));
    }

    private static String encryptHexCBC(String data, String key) {
        byte[] iv = RandomUtil.randomBytes(16);
        AES aes = new AES("CBC", "PKCS7Padding", Base64.decode(key), iv);
        byte[] encrypt = aes.encrypt(data);
        return Base64.encode(concatAll(iv, encrypt));
    }

    private static byte[] concatAll(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }

        byte[] result = new byte[totalLength];
        int pos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, pos, array.length);
            pos += array.length;
        }
        return result;
    }

    private static String decryptStr(String data, String key) {
        // 构建
        AES aes = new AES(Mode.ECB, Padding.PKCS5Padding, Base64.decode(key));
        return aes.decryptStr(data, CharsetUtil.CHARSET_UTF_8);
    }

    private static String encryptHex(String data, String key) {
        // 构建
        AES aes = new AES(Mode.ECB, Padding.PKCS5Padding, Base64.decode(key));
        // 加密为Base64表示
        return aes.encryptHex(data);
    }

}
