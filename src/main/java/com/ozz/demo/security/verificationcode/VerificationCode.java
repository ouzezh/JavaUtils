package com.ozz.demo.security.verificationcode;

import java.awt.image.BufferedImage;
import java.util.Properties;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

public class VerificationCode {
  public static void main(String[] args) {
    try {
      new VerificationCode().createVerficationCode();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void createVerficationCode() {
    // 创建producer，与spring结合时直接使用@Autowired即可
    Producer captchaProducer = captchaProducer();

    String text = captchaProducer.createText();
    BufferedImage image = captchaProducer.createImage(text);
    System.out.println(text);
    System.out.println(image.getWidth() + "*" + image.getHeight());
  }

  private Producer captchaProducer() {
    DefaultKaptcha captchaProducer = new DefaultKaptcha();
    Config config = new Config(kaptchaProperties());
    captchaProducer.setConfig(config);
    return captchaProducer;
  }

  private Properties kaptchaProperties() {
    Properties properties = new Properties();
    properties.put("kaptcha.image.width", 200);
    properties.put("kaptcha.image.height", 50);
    properties.put("kaptcha.textproducer.char.string", "ABCDEFGHKLMNPQRSTWXY3456789");
    properties.put("kaptcha.textproducer.char.length", 6);
    return properties;
  }
}
