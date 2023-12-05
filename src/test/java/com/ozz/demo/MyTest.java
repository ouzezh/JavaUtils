package com.ozz.demo;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class MyTest {
    public static void main(String[] args) {
        upgradeEasyExcel("D:\\dev\\workspace\\payroll\\Payroll-back\\payroll-repo\\src\\main\\java");
    }

    private static void upgradeEasyExcel(String s) {
        try {
            Path start = Paths.get("D:\\dev\\workspace\\payroll\\Payroll-back\\payroll-repo\\src\\main\\java");
            Files.walkFileTree(start, new MyFileVisitor());
        } catch (IOException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    public static class MyFileVisitor implements FileVisitor<Path> {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if(!file.toFile().getName().endsWith(".java")) {
                return FileVisitResult.CONTINUE;
            }
            System.out.println(file.toFile().getName());
            String content = Files.readString(file);
            if(!content.contains("@HeadStyle")) {
                return FileVisitResult.CONTINUE;
            }
            content = handleContent(content, "com.alibaba.excel.enums.BooleanEnum", " true", " BooleanEnum.TRUE");
            content = handleContent(content, "com.alibaba.excel.enums.BooleanEnum", " false", " BooleanEnum.FALSE");
            content = handleContent(content, "com.alibaba.excel.enums.poi.BorderStyleEnum", " BorderStyle\\.", " BorderStyleEnum.");
            content = handleContent(content, "com.alibaba.excel.enums.poi.HorizontalAlignmentEnum", " HorizontalAlignment\\.", " HorizontalAlignmentEnum.");
            content = handleContent(content, "com.alibaba.excel.enums.poi.VerticalAlignmentEnum", " VerticalAlignment\\.", " VerticalAlignmentEnum.");
            content = handleContent(content, "com.alibaba.excel.enums.poi.FillPatternTypeEnum", " FillPatternType\\.", " FillPatternTypeEnum.");

            Files.writeString(file, content);
//            ExceptionUtil.wrapRuntimeAndThrow(file.toFile().getName());
            return FileVisitResult.CONTINUE;
        }

        private String handleContent(String content, String clz, String src, String to) {
            String toContent = content.replaceAll(src, to);
            if(!toContent.equals(content) && !toContent.contains(clz)) {
                toContent = toContent.replaceFirst("\r\nimport ", StrUtil.format("\r\nimport {};\r\nimport ", clz));
            }
            return toContent;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        ;
    }
}
