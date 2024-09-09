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
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

/**
 * 对称加密
 *
 * https://www.hutool.cn/docs/#/crypto/%E5%AF%B9%E7%A7%B0%E5%8A%A0%E5%AF%86-SymmetricCrypto
 */
@Slf4j
public class SymmetricCryptoDemo {
    public static void main(String[] args) {
        String content = "test中文";

        // 随机生成密钥
        String key = Base64.encode(SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded());
        log.info("秘钥: " + key);

        // 生成随机key
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[16];
        random.nextBytes(iv);

        // 构建
        AES aes = new AES("CBC", "PKCS7Padding", Base64.decode(key), iv);
//        AES aes = new AES(Mode.ECB, Padding.PKCS5Padding, Base64.decode(key));

        // 加密
        byte[] encrypt = aes.encrypt(content);
        // 解密
        byte[] decrypt = aes.decrypt(encrypt);

        // 加密为Base64表示
        String encryptHex = aes.encryptHex(content);
        // 解密为字符串
        String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        // 打印
        log.info("密文: " + encryptHex);
        log.info("明文: " + decryptStr);
        Assert.isTrue(content.equals(decryptStr));

        // 加盐加密
        String salt = Base64.encode(RandomUtil.randomBytes(16));
        log.info("秘钥: " + salt);
        log.info("盐: " + salt);
        AES aes2 = new AES(Mode.CBC, Padding.PKCS5Padding, Base64.decode(key), Base64.decode(salt));
        String encryptHex2 = aes2.encryptHex(content);
        // 解密为字符串
        String decryptStr2 = aes2.decryptStr(encryptHex2);
        // 打印
        log.info("加盐密文: " + encryptHex2);
        log.info("加盐明文: " + decryptStr2);
        Assert.isTrue(content.equals(decryptStr2));
    }

}
