package com.ozz.demo.zxing;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.log.StaticLog;
import com.ozz.demo.path.ResourcePathUtil;

public class QRCodeDemo {
    public static void main(String[] args) {
        String content = "com.google.zxing:core";
        String filePath = ResourcePathUtil.getHomeDirectory() + "/QRCode.png";

        QrCodeUtil.generate(content, 200, 200, FileUtil.file(filePath));

        StaticLog.info("save QRCode to " + filePath);
    }
}
