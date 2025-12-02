package com.ozz.demo.text;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class RandomChenese {

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            log.info(getRandomChinese(3));
        }
    }

    public static String getRandomChinese(int length) {
        if (length <= 0) {
            throw new RuntimeException("length ");
        }

//    Pair<Integer,Integer> pair = Pair.of(0x4e00, 0x9fa5);// 基本汉字Unicode范围
        char[] charArr = new char[length];
        for (int i = 0; i < length; i++) {
//      charArr[i] = (char) RandomUtils.nextInt(pair.getLeft(), pair.getRight());
            charArr[i] = getRandomChineseCommon();
        }
        return new String(charArr);
    }

    @SneakyThrows
    private static char getRandomChineseCommon() {
        String str = "";

        //随机数对象
        Random random = new Random();

        ///区码，0xA0打头，从第16区开始，即0xB0=11*16=176,16~55一级汉字，56~87二级汉字
        int hightPos = (176 + Math.abs(random.nextInt(39)));

        //位码，0xA0打头，范围第1~94列
        int lowPos = (161 + Math.abs(random.nextInt(93)));

        //字节码
        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        //区位码组合成汉字
        str = new String(b, "GBK");

        //根据索引返回对应的字符
        return str.charAt(0);
    }
}
