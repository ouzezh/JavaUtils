package com.ozz.demo.text;

import cn.hutool.core.lang.Assert;
import cn.hutool.log.StaticLog;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 英文与数字转化
 *
 */
public class NumberFormatUtil {

    private static final Pattern PATTERN = Pattern.compile("[A-Za-z]+");

    public static String formatEnglish(int num) {
        Assert.isTrue(num >= 0, "数字必须大于0");
        StringBuilder sb = new StringBuilder();

        List<Integer> list = new LinkedList<>();
        while (num > 0) {
            int mod = num % 26;
            num = num / 26;
            if(num>0 && mod==0) {// Z特殊处理
                num--;
                mod = 26;
            }
            list.add(0, mod);
        }
        return list.stream().map(t -> String.valueOf((char) ('A'+t-1))).collect(Collectors.joining());
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
