package com.ozz.demo.security.encrypt.asymmetric;

import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;

import java.security.KeyPair;

public class SignDemo {
    public static void main(String[] args) {
        // 获取密钥
        KeyPair pair = SecureUtil.generateKeyPair("RSA");
        byte[] priKey = pair.getPrivate().getEncoded();
        byte[] pubKey = pair.getPublic().getEncoded();

        byte[] data = "my test".getBytes();
        Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA, priKey, pubKey);
        //签名
        byte[] signed = sign.sign(data);
        //验证签名
        boolean verify = sign.verify(data, signed);
        Assert.isTrue(verify);
    }
}
