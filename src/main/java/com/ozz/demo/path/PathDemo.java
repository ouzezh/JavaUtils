package com.ozz.demo.path;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.ozz.demo.security.encrypt.digest.DigestDemo;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class PathDemo {
    public Path getPath(String path) {
        return Paths.get(path);
    }

    public boolean exists(Path path) {
        return Files.exists(path);
    }

    public boolean isDirectory(Path path) {
        return Files.isDirectory(path);
    }

    @SneakyThrows
    public String readFileToString(Path path) {
        // 短文本
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, StandardCharsets.UTF_8);
        /*
         * // 长文本 InputStream in = Files.newInputStream(path); Reader in = Files.newBufferedReader(path,
         * StandardCharsets.UTF_8);
         */
    }

    @SneakyThrows
    public List<String> readLines(Path path) {
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public void writeStringToFile(Path path, String content, boolean append) {
        // 短文本
        Files.write(path,
                content.getBytes(StandardCharsets.UTF_8),
                append ? StandardOpenOption.APPEND : StandardOpenOption.WRITE);
        /*
         * // 长文本 OutputStream out = Files.newOutputStream(path, append ? StandardOpenOption.APPEND :
         * StandardOpenOption.WRITE); Writer out = Files.newBufferedWriter(path, append ?
         * StandardOpenOption.APPEND : StandardOpenOption.WRITE);
         */
    }

    @SneakyThrows
    public void deleteFiles(Path path) {
        Files.delete(path);
    }

    @SneakyThrows
    public void copyFilesToFolder(Path source, Path target) {
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    public boolean equals(Path path1, Path path2) {
        return StrUtil.equals(DigestDemo.md5(path1.toFile()), DigestDemo.md5(path2.toFile()));
    }

    /**
     * 相对路径
     */
    public void relativize(Path root, Path path) {
        // 获取相对路径
        Path r = root.relativize(path);
        // 解析相对路径
        Path newPath = root.resolve(r);
        if (!newPath.equals(path)) {
            throw new RuntimeException("解析路径错误");
        }
    }

    @SneakyThrows
    public long getSize(Path path) {
        return Files.size(path);
    }

    public String getFileName(String filePath) {
        return FileNameUtil.getName(filePath);
    }

    public String mainName(String fileName) {
        return FileNameUtil.mainName(fileName);
    }

    public String extName(String fileName) {
        return FileNameUtil.extName(fileName);
    }
}
