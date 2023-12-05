package com.ozz.demo.text;

import cn.hutool.core.util.StrUtil;

public class StrUtils {
    public static String format(CharSequence template, Object... params) {
        return StrUtil.format(template, params);
    }
}
