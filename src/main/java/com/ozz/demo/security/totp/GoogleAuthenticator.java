package com.ozz.demo.security.totp;

import cn.hutool.core.codec.Base32;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.otp.TOTP;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class GoogleAuthenticator {
    public static void main(String[] args) {
        String secretKey = TOTP.generateSecretKey(10);
        log.info("secretKey: " + secretKey);

        log.info("code: " + generateStr(secretKey));

        String barCodeData = TOTP.generateGoogleSecretKey( "test-account", 10);
        log.info("barCodeData: " + barCodeData);

//        String filePath = ResourcePathUtil.getHomeDirectory() + "/QRCode.png";
//        QrCodeUtil.generate(barCodeData, 200, 200, FileUtil.file(filePath));
//        log.info("save QRCode to " + filePath);
    }

    private static String generateStr(String secretKey) {
        return StrUtil.fillBefore(String.valueOf(generate(secretKey)), '0', 6);
    }

    private static int generate(String secretKey) {
        byte[] bytes = Base32.decode(secretKey.replace(" ", "").toUpperCase());
        return new TOTP(bytes).generate(Instant.now());
    }

}
