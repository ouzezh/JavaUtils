package com.ozz.demo.commons.csv;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CsvUtils {
    final static Charset charset = CharsetUtil.CHARSET_UTF_8;

    public static <T> List<T> readCsv(String filePath, Class<T> clz, Charset charset) {
        final CsvReader reader = CsvUtil.getReader();
//        CsvRowHandler h = new CsvRowHandler() {
//            @Override
//            public void handle(CsvRow csvRow) {
//            }
//        };
        return reader.read(ResourceUtil.getReader(filePath, charset), clz);
    }

    @SneakyThrows
    public static <T> void writeCsv(String filePath, Class<T> clz, List<T> list) {
        Path path = Paths.get(filePath);
        Files.deleteIfExists(path);
        Files.createDirectories(path.getParent());
        Files.createFile(path);

        try (BufferedWriter out = Files.newBufferedWriter(path, charset);
             CsvWriter writer = CsvUtil.getWriter(filePath, charset);) {
            // 处理乱码问题
            if (CharsetUtil.UTF_8.equals(charset)) {
                out.write(new String(new byte[]{(byte) 0xef, (byte) 0xbb, (byte) 0xbf}, charset));
            }
            writer.writeBeans(list);
        }
    }
}
