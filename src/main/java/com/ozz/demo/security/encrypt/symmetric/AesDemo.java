package com.ozz.demo.security.encrypt.symmetric;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
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
public class AesDemo {
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
        AES aesECB = getAesECB(key);
        String encryptHex = aesECB.encryptBase64(data);
        String decryptStr = aesECB.decryptStr(encryptHex);
        // 打印
        log.info("密文: " + encryptHex);
        log.info("明文: " + decryptStr);
        Assert.isTrue(data.equals(decryptStr));

        // 带初始化向量(iv)加密解密：前端加密密文初始化向量通常是加密数据的前16个字节
        AES aesCBC = getAesCBC(key, iv);
        String encryptHexCBC = aesCBC.encryptBase64(data);
        String decryptStrCBC = aesCBC.decryptStr(encryptHexCBC);
        // 打印
        log.info("带iv密文: " + encryptHexCBC);
        log.info("带iv明文: " + decryptStrCBC);
        Assert.isTrue(data.equals(decryptStrCBC));

    }

    public static byte[] randomBytes(int length) {
        return RandomUtil.randomBytes(length);
    }

    private static AES getAesCBC(String key, String iv) {
        return new AES(Mode.CBC.name(), "PKCS7Padding", Base64.decode(key), Base64.decode(iv));
    }

    private static AES getAesECB(String key) {
        return new AES(Mode.ECB, Padding.PKCS5Padding, Base64.decode(key));
    }

}
