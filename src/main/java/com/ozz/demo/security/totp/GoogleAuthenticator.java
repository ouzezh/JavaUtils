package com.ozz.demo.security.totp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.log.StaticLog;
import com.ozz.demo.path.ResourcePathUtil;
import com.ozz.demo.security.totp.base.TOTP;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import java.net.URLEncoder;
import java.security.SecureRandom;

/**
 * http://www.asaph.org/2016/04/google-authenticator-2fa-java.html
 */
public class GoogleAuthenticator {
  public static void main(String[] args) {
    String secretKey = getRandomSecretKey();
    String issuer = "google-test";
    String account = "ouzezh";

    StaticLog.info("secretKey: " + secretKey);

    String code = getTOTPCode(secretKey, System.currentTimeMillis());
    StaticLog.info("TOTP code: " + code);

    String barCodeData = getGoogleAuthenticatorQRCode(secretKey, issuer, account);
    StaticLog.info("barCodeData: " + barCodeData);

    String filePath = ResourcePathUtil.getHomeDirectory() + "/QRCode.png";
    QrCodeUtil.generate(barCodeData, 200, 200, FileUtil.file(filePath));
    StaticLog.info("save QRCode to " + filePath);
  }

  public static String getRandomSecretKey() {
    SecureRandom random = new SecureRandom();
    byte[] bytes = new byte[20];
    random.nextBytes(bytes);
    Base32 base32 = new Base32();
    String secretKey = base32.encodeToString(bytes);
    // make the secret key more human-readable by lower-casing and
    // inserting spaces between each group of 4 characters
    return secretKey.toLowerCase().replaceAll("(.{4})(?=.{4})", "$1 ");
  }

  public static String getTOTPCode(String secretKey, long currentTime) {
    String normalizedBase32Key = secretKey.replace(" ", "").toUpperCase();
    Base32 base32 = new Base32();
    byte[] bytes = base32.decode(normalizedBase32Key);
    String hexKey = Hex.encodeHexString(bytes);
    long time = (currentTime / 1000) / 30;
    String hexTime = Long.toHexString(time);
    return TOTP.generateTOTP(hexKey, hexTime, "6");// TOTP: Time-Based One-Time Password Algorithm
  }

  @SneakyThrows
  public static String getGoogleAuthenticatorQRCode(String secretKey, String issuer, String account) {
    String normalizedBase32Key = secretKey.replace(" ", "").toUpperCase();
      return "otpauth://totp/"
             + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
             + "?secret=" + URLEncoder.encode(normalizedBase32Key, "UTF-8").replace("+", "%20")
             + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
  }

}
