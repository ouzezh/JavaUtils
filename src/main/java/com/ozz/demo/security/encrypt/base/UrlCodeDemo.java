package com.ozz.demo.security.encrypt.base;

import cn.hutool.core.util.URLUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * URL编码
 */
@Slf4j
public class UrlCodeDemo {
    @SneakyThrows
    public static String urlEncode(String s) {
        return URLUtil.encode(s);
    }

    @SneakyThrows
    public static String urlDecode(String s) {
        return URLUtil.decode(s);
    }

}
