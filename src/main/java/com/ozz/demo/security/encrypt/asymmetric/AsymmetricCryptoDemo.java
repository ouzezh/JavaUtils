package com.ozz.demo.security.encrypt.asymmetric;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.security.KeyPair;

/**
 * 非对称加密
 *
 * https://www.hutool.cn/docs/#/crypto/%E9%9D%9E%E5%AF%B9%E7%A7%B0%E5%8A%A0%E5%AF%86-AsymmetricCrypto
 */
public class AsymmetricCryptoDemo {
    public static void main(String[] args) {
        // 获取密钥
        KeyPair pair = SecureUtil.generateKeyPair("RSA");
        byte[] priKey = pair.getPrivate().getEncoded();
        byte[] pubKey = pair.getPublic().getEncoded();

        RSA rsa = new RSA(priKey, pubKey);

        String content = "my test";

        //公钥加密，私钥解密
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(content, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);
        byte[] decrypt = rsa.decrypt(encrypt, KeyType.PrivateKey);

        Assert.isTrue(content.equals(StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8)));

        //私钥加密，公钥解密
        byte[] encrypt2 = rsa.encrypt(StrUtil.bytes(content, CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
        byte[] decrypt2 = rsa.decrypt(encrypt2, KeyType.PublicKey);

        Assert.isTrue(content.equals(StrUtil.str(decrypt2, CharsetUtil.CHARSET_UTF_8)));
    }
}
