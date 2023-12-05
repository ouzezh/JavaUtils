package com.ozz.demo.text;

import cn.hutool.log.StaticLog;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * 英文与数字转化
 *
 */
public class NumberFormatUtil {

    private static final Pattern PATTERN = Pattern.compile("[A-Za-z]+");

    public static String formatEnglish(int num) {
        if (num <= 0)
            throw new RuntimeException("数字不能小于等于0");
        String enNum = "";
        int mod;
        while (num > 0) {
            mod = num % 26;
            if (mod == 0) {
                // 正好进位时将上一位上取1，变成下一位的Z
                enNum = 'Z' + enNum;
                num = num / 26 - 1;
            } else {
                enNum = (char) ('A' + mod - 1) + enNum;
                num = num / 26;
            }
        }
        return enNum;
    }

    public static int parseEnglish(String en) {
        if (en == null || !PATTERN.matcher(en).matches()) {
            throw new RuntimeException("字母:'" + en + "'非法");
        }

        int num = 0;
        for (int i = 0; i < en.length(); i++) {
            num = num * 26 + en.charAt(i) - ('A' - 1);
        }
        return num;
    }

    public static void printNumber() {
        double d = 12.123d;
        StaticLog.info(new DecimalFormat("#.##").format(0.123D));
        StaticLog.info(new DecimalFormat("#.##").format(12.2D));
        StaticLog.info(new DecimalFormat("#.##").format(d));
        StaticLog.info(new DecimalFormat("#.##%").format(0.0123));
        StaticLog.info(new DecimalFormat("00.00").format(d));
        StaticLog.info(new DecimalFormat(",###.00").format(d * 1000));
    }

    public static void main(String[] args) {
        printNumber();
    }
}
