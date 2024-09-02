package com.ozz.demo.excel.easyexcel;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.log.StaticLog;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.ozz.demo.excel.easyexcel.listener.MyExcelListener;
import com.ozz.demo.excel.easyexcel.model.MyExcelModel;
import com.ozz.demo.path.ResourcePathUtil;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EasyExcelExtUtil extends EasyExcelUtil {

    @SneakyThrows
    public static void main(String[] args) {
        String folder = String.format("%s/logs", ResourcePathUtil.getProjectPath());
        Files.createDirectories(Paths.get(folder));
        List<MyExcelModel> data = ListUtil.toList(new MyExcelModel("s1", new Date(), 0.1D),
                new MyExcelModel("s2", new Date(), 0.12345D));

        // 不创建对象读
        String pathName = String.format("%s\\简单读写.xlsx", folder);
        List<Map<Integer, String>> list2 = readNoModel(pathName);
        StaticLog.info(list2.stream().map(JSON::toJSONString).collect(Collectors.joining("\r\n", "\r\n", "")));

        // 分批读
        List<MyExcelModel> list3 = batchRead(pathName, MyExcelModel.class);
        StaticLog.info(list3.stream().map(JSON::toJSONString).collect(Collectors.joining("\r\n", "\r\n", "")));

        // 自定义配置写
        try (ExcelWriter excel = EasyExcel.write(String.format("%s\\自定义配置写.xlsx", folder)).build()) {
            writeSheet(excel, 0, null, MyExcelModel.class, data, sheetBuilder -> {
                sheetBuilder.excludeColumnFieldNames(ListUtil.toList("date"));
                return null;
            });
        }

        // 动态表头写
        try(ExcelWriter excel = EasyExcel.write(String.format("%s\\动态表头写.xlsx", folder)).build()) {
            List<List<String>> header = new ArrayList<>();
            header.add(ListUtil.toList("A", "A1"));
            header.add(ListUtil.toList("A", "A2"));
            header.add(ListUtil.toList("B"));
            List<List<String>> data2 = new ArrayList<>();
            data2.add(ListUtil.toList("a1", "a2", "a3"));
            data2.add(ListUtil.toList("b1", "b2", "b3"));
            writeSheet(excel, 0, null, header, data2, null);
        }

        // 使用table去写入
        try(ExcelWriter excel = EasyExcel.write(String.format("%s\\使用table去写入.xlsx", folder)).build()) {
            WriteSheet sheet = EasyExcelFactory.writerSheet(0, null).autoTrim(true).needHead(false).build();
            AtomicInteger tableNo = new AtomicInteger(0);
            // table 0
            excel.write(data, sheet,
                    EasyExcelFactory.writerTable(tableNo.incrementAndGet()).needHead(true).head(MyExcelModel.class)
                            .relativeHeadRowIndex(null)
                            .excludeColumnFieldNames(ListUtil.toList("date")).build());
            // table 1
            excel.write(data, sheet,
                    EasyExcelFactory.writerTable(tableNo.incrementAndGet()).needHead(true).head(MyExcelModel.class)
                            .relativeHeadRowIndex(2).build());
        }
    }

    public static List<Map<Integer, String>> readNoModel(String pathName) {
//        List<Map<Integer, String>> list = new ArrayList<>();
//        MyExcelListener<Map<Integer, String>> listener = new MyExcelListener<>(list::addAll);
//        EasyExcel.read(pathName, listener).sheet().doRead();
//        return list;
        return EasyExcel.read(pathName).sheet().doReadSync();
    }

    public static <T> List<T> batchRead(String pathName, Class<T> clz, int... sheetNo) {
        if (sheetNo.length == 0) {
            sheetNo = new int[]{0};
        }
        try(ExcelReader reader = EasyExcelFactory.read(pathName).autoTrim(true).ignoreEmptyRow(true).build()) {
            List<T> list = new ArrayList<>();
            int headRowNumber = getHeadRowNumber(clz);
            MyExcelListener<T> listener = new MyExcelListener<>(list::addAll);
            for (int no : sheetNo) {
                reader.read(EasyExcelFactory.readSheet(no).autoTrim(true).head(clz).headRowNumber(headRowNumber).registerReadListener(listener).build());
            }
            return list;
        }
    }

    private static <T> Integer getHeadRowNumber(Class<T> clz) {
        int headRowNumber = 1;
        for (Field field : clz.getDeclaredFields()) {
            ExcelProperty ann = field.getAnnotation(ExcelProperty.class);
            if (ann != null && ann.value().length > headRowNumber) {
                headRowNumber = ann.value().length;
            }
        }
        return headRowNumber;
    }

    public static <E> void writeSheet(ExcelWriter excel, Integer sheetNo, String sheetName, Class<E> clazz,
                                      List<E> data) {
        writeSheet(excel, sheetNo, sheetName, clazz, data, null);
    }

    public static <T> void writeSheet(ExcelWriter excel, Integer sheetNo, String sheetName, Class<T> header,
                                      List<T> data,
                                      Function<ExcelWriterSheetBuilder, Collection<SheetWriteHandler>> custom) {
        writeSheet(excel, sheetNo, sheetName, data, builder -> {
            builder.head(header);
            return custom != null ? custom.apply(builder) : null;
        });
    }

    public static <T> void writeSheet(ExcelWriter excel, Integer sheetNo, String sheetName, List<List<String>> header
            , List<T> data, Function<ExcelWriterSheetBuilder, Collection<SheetWriteHandler>> custom) {
        writeSheet(excel, sheetNo, sheetName, data, builder -> {
            builder.head(header);
            builder.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy());
            builder.registerWriteHandler( // row 水平行 默认样式
                    new HorizontalCellStyleStrategy(getDefaultCellStyle(true), getDefaultCellStyle(false)));
            return custom != null ? custom.apply(builder) : null;
        });
    }

    /**
     * 写入 Excel
     *
     * @param excel     写入的Excel
     * @param sheetNo   sheet序号
     * @param sheetName sheet名
     * @param data      写入的table数据
     * @param custom    自定义配置(参数与返回值无关联，参数修改默认值，返回值返回默认的Handler)
     *                  参数: 用于修改Excel初始化配置,如果执行ExcelWriterSheetBuilder.excludeColumnFiledNames排除字段;
     *                  返回值: 用于提供Excel自定义修改的Handler,如SheetWriteHandler设置单元格合并
     */
    private static <T> void writeSheet(ExcelWriter excel, Integer sheetNo, String sheetName, List<T> data,
                                       Function<ExcelWriterSheetBuilder, Collection<SheetWriteHandler>> custom) {
        ExcelWriterSheetBuilder builder = EasyExcelFactory.writerSheet(sheetNo, sheetName).autoTrim(true);
        if (custom != null) {
            Collection<SheetWriteHandler> writeHandler = custom.apply(builder);
            Optional.ofNullable(writeHandler).ifPresent(l -> l.forEach(builder::registerWriteHandler));
        }
        WriteSheet sheet = builder.build();
        excel.write(data, sheet);// 可重复多次写入
    }

}
