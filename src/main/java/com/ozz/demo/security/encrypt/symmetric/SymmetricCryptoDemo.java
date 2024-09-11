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
        // 生成一个随机的 128 位 IV (对于 AES-128)
        String iv = Base64.encode(randomBytes(16));
        log.info("iv: " + iv);

        // 加密解密
        String encryptHex = encryptBase64(data, key);
        String decryptStr = decryptStr(encryptHex, key);
        // 打印
        log.info("密文: " + encryptHex);
        log.info("明文: " + decryptStr);
        Assert.isTrue(data.equals(decryptStr));

        // 带初始化向量(iv)加密解密：前端加密密文初始化向量通常是加密数据的前16个字节
        String encryptHexCBC = encryptBase64CBC(data, key, iv);
        String decryptStrCBC = decryptStrCBC(encryptHexCBC, key, iv);
        // 打印
        log.info("带iv密文: " + encryptHexCBC);
        log.info("带iv明文: " + decryptStrCBC);
        Assert.isTrue(data.equals(decryptStrCBC));

    }

    private static String decryptStrCBC(String data, String key, String iv) {
        AES aes = new AES(Mode.CBC.name(), "PKCS7Padding", Base64.decode(key), Base64.decode(iv));
        return aes.decryptStr(data);
    }

    private static String encryptBase64CBC(String data, String key, String iv) {
        AES aes = new AES(Mode.CBC.name(), "PKCS7Padding", Base64.decode(key), Base64.decode(iv));
        return aes.encryptBase64(data);
    }

    public static byte[] randomBytes(int length) {
        return RandomUtil.randomBytes(length);
    }

    private static String decryptStr(String data, String key) {
        AES aes = new AES(Mode.ECB, Padding.PKCS5Padding, Base64.decode(key));
        return aes.decryptStr(data, CharsetUtil.CHARSET_UTF_8);
    }

    private static String encryptBase64(String data, String key) {
        AES aes = new AES(Mode.ECB, Padding.PKCS5Padding, Base64.decode(key));
        return aes.encryptBase64(data);
    }

}
