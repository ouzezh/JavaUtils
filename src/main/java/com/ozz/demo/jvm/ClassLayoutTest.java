package com.ozz.demo.jvm;

import cn.hutool.log.StaticLog;
import org.openjdk.jol.info.ClassLayout;

public class ClassLayoutTest {
    /**
     * 打印对象内存布局
     */
    public static void main(String[] args) {
        StaticLog.info("ClassLayout: new Object()");
        StaticLog.info(ClassLayout.parseInstance(new Object()).toPrintable());
        StaticLog.info("ClassLayout: new int[]{1}");
        StaticLog.info(ClassLayout.parseInstance(new int[]{1}).toPrintable());
    }
}
