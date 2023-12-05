package com.ozz.demo.proxy;

import org.springframework.aop.support.AopUtils;

public class AopUtil {
    public static boolean isAopProxy(Object obj) {
        return AopUtils.isAopProxy(obj);
    }

    public static Class<?> getTargetClass(Object candidate) {
        return AopUtils.getTargetClass(candidate);
    }
}
