package com.ozz.demo.zxing;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.ozz.demo.path.ResourcePathUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QRCodeDemo {
    public static void main(String[] args) {
        String content = "com.google.zxing:core";
        String filePath = ResourcePathUtil.getHomeDirectory() + "/QRCode.png";

        QrCodeUtil.generate(content, 200, 200, FileUtil.file(filePath));

        log.info("save QRCode to " + filePath);
    }
}
