package com.ozz.demo.text;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class RandomChenese {

  public static void main(String[] args) throws UnsupportedEncodingException {
    for (int i = 0; i < 3; i++) {
      System.out.println(getRandomChinese(6));
    }
  }

  public static String getRandomChinese(int length) throws UnsupportedEncodingException {
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
    try {
      str = new String(b, "GBK");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }

    //根据索引返回对应的字符
    return str.charAt(0);
  }
}
