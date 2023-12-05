package com.ozz.demo.text;

import cn.hutool.core.util.StrUtil;

public class TextUtil {
  /**
   * 半角转全角
   * 
   * @param input String.
   * @return 全角字符串.
   */
  public String toHalfWidth(String input) {
    if (StrUtil.isEmpty(input))
      return input;

    char c[] = input.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (c[i] == ' ') {
        c[i] = '\u3000';
      } else if (c[i] < '\177') {
        c[i] = (char) (c[i] + 65248);
      }
    }
    return new String(c);
  }

  /**
   * 全角转半角
   * 
   * @param input String.
   * @return 半角字符串
   */
  public String toFullWidth(String input) {
    if (StrUtil.isEmpty(input))
      return input;

    char c[] = input.toCharArray();
    for (int i = 0; i < c.length; i++) {
      if (c[i] == '\u3000') {
        c[i] = ' ';
      } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
        c[i] = (char) (c[i] - 65248);
      }
    }
    return new String(c);
  }
}
