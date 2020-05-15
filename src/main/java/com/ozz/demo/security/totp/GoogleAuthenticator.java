package com.ozz.demo.security.totp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;

import javax.swing.filechooser.FileSystemView;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;

import com.ozz.demo.security.totp.base.TOTP;
import com.ozz.demo.zxing.QRCodeDemo;

/**
 * http://www.asaph.org/2016/04/google-authenticator-2fa-java.html
 */
public class GoogleAuthenticator {
  public static void main(String[] args) {
    String secretKey = getRandomSecretKey();
    String issuer = "google-test";
    String account = "ouzezh";

    System.out.println("secretKey: " + secretKey);

    String code = getTOTPCode(secretKey, System.currentTimeMillis());
    System.out.println("TOTP code: " + code);

    String barCodeData = getGoogleAuthenticatorBarCode(secretKey, issuer, account);
    System.out.println("barCodeData: " + barCodeData);

    String filePath = FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "/QRCode.png";
    QRCodeDemo.createQRCode(barCodeData, filePath, 200, 200);
    System.out.println("save QRCode to " + filePath);
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

  public static String getGoogleAuthenticatorBarCode(String secretKey, String issuer, String account) {
    String normalizedBase32Key = secretKey.replace(" ", "").toUpperCase();
    try {
      return "otpauth://totp/"
             + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
             + "?secret=" + URLEncoder.encode(normalizedBase32Key, "UTF-8").replace("+", "%20")
             + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
    } catch (UnsupportedEncodingException e) {
      throw new IllegalStateException(e);
    }
  }

}
