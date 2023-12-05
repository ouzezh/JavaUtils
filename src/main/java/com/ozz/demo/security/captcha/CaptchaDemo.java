package com.ozz.demo.security.captcha;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.ozz.demo.path.ResourcePathUtil;

/**
 *
 */
public class CaptchaDemo {
    public static void main(String[] args) {
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        //CircleCaptcha captcha = new CircleCaptcha(200, 100, 4, 20);
        //图形验证码写出，可以写出到文件，也可以写出到流
        captcha.write(ResourcePathUtil.getHomeDirectory() + "/captcha_circle.png");
        //验证图形验证码的有效性，返回boolean值
        captcha.verify("1234");
    }
}
