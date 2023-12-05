package com.ozz.demo.security.encrypt.base;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URL编码
 */
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
