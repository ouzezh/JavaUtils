package com.ozz.demo.excel.easyexcel;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.ozz.demo.excel.easyexcel.model.MyExcelModel;
import com.ozz.demo.path.ResourcePathUtil;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EasyExcelUtil {

    @SneakyThrows
    public static void main(String[] args) {
        String folder = StrUtil.format("{}/logs", ResourcePathUtil.getProjectPath());
        Files.createDirectories(Paths.get(folder));
        List<MyExcelModel> data = ListUtil.toList(new MyExcelModel("s1", new Date(), 0.1D),
                new MyExcelModel("s2", new Date(), 0.12345D));
        String pathName = StrUtil.format("{}\\简单读写.xlsx", folder);

        // 简单写
        try(OutputStream out = new FileOutputStream(pathName)) {
            write(out, MyExcelModel.class, data);
        }

        // 简单读
        try(InputStream inout = new FileInputStream(pathName)) {
            List<MyExcelModel> list = read(inout, MyExcelModel.class);
            StaticLog.info(JSON.toJSONString(list));
        }
        StaticLog.info("你好");
        System.out.println("你好");
    }

    public static <T> void write(OutputStream out, Class<T> head, List<T> list) {
        ExcelWriterBuilder builder = EasyExcel.write(out, head).autoTrim(true);
        builder.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .registerWriteHandler(new HorizontalCellStyleStrategy(getDefaultCellStyle(true), getDefaultCellStyle(false)));
        try(ExcelWriter w = builder.build()) {
            WriteSheet sheet = EasyExcel.writerSheet().build();
            w.write(list, sheet);
        }
    }

    public static <T> List<T> read(InputStream input, Class<T> head) {
        return EasyExcel.read(input).autoTrim(true).sheet().head(head).doReadSync();
    }

    protected static WriteCellStyle getDefaultCellStyle(boolean isHead) {
        WriteCellStyle style = new WriteCellStyle();
        // 锁定（设置为不锁定，保护工作表后单元格依然可以编辑）
        style.setLocked(true);
        // 对齐方式
        style.setHorizontalAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        if (isHead) {
            // 背景
            style.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        }
        return style;
    }
}
