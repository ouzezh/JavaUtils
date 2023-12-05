package com.ozz.demo.zip;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import lombok.SneakyThrows;

import java.io.File;

public class ZipUtils {
    private final static String DEFAULT_ENCODING = "gb2312";

    @SneakyThrows
    public static void zip(File zipFile, File... srcFiles) {
//        Archiver archiver = CompressUtil.createArchiver(CharsetUtil.defaultCharset(), ArchiveStreamFactory.SEVEN_Z, toFile);
//        Arrays.stream(srcFiles).map(archiver::add);
//        archiver.finish().close();
        ZipUtil.zip(zipFile, false, srcFiles);
    }

    @SneakyThrows
    public static void unZip(File zipFile, File outFile) {
//        Extractor extractor =     CompressUtil.createExtractor(CharsetUtil.defaultCharset(), srcFile);
//        extractor.extract(toDir);
        ZipUtil.unzip(zipFile, outFile);
    }

    @SneakyThrows
    public static byte[] gzip(String content) {
        return ZipUtil.gzip(content, CharsetUtil.defaultCharsetName());
    }

    @SneakyThrows
    public static String unGzip(byte[] buf) {
        return ZipUtil.unGzip(buf, CharsetUtil.defaultCharsetName());
    }
}
