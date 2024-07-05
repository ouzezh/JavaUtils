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
import cn.hutool.log.StaticLog;

/**
 * 对称加密
 *
 * https://www.hutool.cn/docs/#/crypto/%E5%AF%B9%E7%A7%B0%E5%8A%A0%E5%AF%86-SymmetricCrypto
 */
public class SymmetricCryptoDemo {
    public static void main(String[] args) {
        String content = "test中文";

        // 随机生成密钥
        String key = Base64.encode(SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded());
        StaticLog.info("秘钥: " + key);

        // 构建
        AES aes = SecureUtil.aes(Base64.decode(key));
//        AES aes = new AES(Mode.ECB, Padding.PKCS5Padding, key, iv);

        // 加密
        byte[] encrypt = aes.encrypt(content);
        // 解密
        byte[] decrypt = aes.decrypt(encrypt);

        // 加密为Base64表示
        String encryptHex = aes.encryptHex(content);
        // 解密为字符串
        String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        // 打印
        StaticLog.info("密文: " + encryptHex);
        StaticLog.info("明文: " + decryptStr);
        Assert.isTrue(content.equals(decryptStr));

        // 加盐加密
        String salt = Base64.encode(RandomUtil.randomBytes(16));
        StaticLog.info("秘钥: " + salt);
        StaticLog.info("盐: " + salt);
        AES aes2 = new AES(Mode.CBC, Padding.PKCS5Padding, Base64.decode(key), Base64.decode(salt));
        String encryptHex2 = aes2.encryptHex(content);
        // 解密为字符串
        String decryptStr2 = aes2.decryptStr(encryptHex2);
        // 打印
        StaticLog.info("加盐密文: " + encryptHex2);
        StaticLog.info("加盐明文: " + decryptStr2);
        Assert.isTrue(content.equals(decryptStr2));
    }
}
