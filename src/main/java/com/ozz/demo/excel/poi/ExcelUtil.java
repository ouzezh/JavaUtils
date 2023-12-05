package com.ozz.demo.excel.poi;

import cn.hutool.log.StaticLog;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExcelUtil {
    public static void main(String[] args) {
        Workbook wb = open(Paths.get("C:\\Users\\ouzezhou\\Downloads\\test.xlsx"));
        Cell cell = wb.getSheetAt(0).getRow(0).getCell(0);
        StaticLog.info(cell.getCellType().name());
//    StaticLog.info(cell.getCellType().getCode());
        StaticLog.info(cell.getCellStyle().getDataFormatString());
        StaticLog.info(String.valueOf(cell.getCellStyle().getFillForegroundColor()));// 读取背景色经测试转化成xls格式再读取，xlsx有可能读不出来
    }

    @SneakyThrows
    public static Workbook open(Path path) {
        try (InputStream input = Files.newInputStream(path)) {
            return WorkbookFactory.create(input);
        }
    }

}
