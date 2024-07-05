package com.ozz.demo.security.encrypt.symmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
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

        // 构建
        AES aes = SecureUtil.aes(Base64.decode(key));
//        AES aes = new AES(Mode.ECB, Padding.PKCS5Padding, key, iv);

        // 加密
        byte[] encrypt = aes.encrypt(content);
        // 解密
        byte[] decrypt = aes.decrypt(encrypt);

        // 加密为Base64表示
        String encryptHex = aes.encryptBase64(content);
        // 解密为字符串
        String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);

        StaticLog.info(encryptHex);
        StaticLog.info(decryptStr);
        Assert.isTrue(content.equals(decryptStr));
    }
}
