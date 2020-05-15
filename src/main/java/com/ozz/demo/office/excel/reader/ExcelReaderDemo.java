package com.ozz.demo.office.excel.reader;

import com.ozz.demo.office.excel.reader.base.SimpleSheetContentsHandler;
import com.ozz.demo.office.excel.reader.base.SimpleXSSFSheetXMLHandler;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class ExcelReaderDemo {
  public static void main(String[] args) {
    List<Map<String, String>> data = ExcelReaderDemo.parse(Paths.get("D:/Nutstore/新东方/数据中台/科杰/权限相关表/模板.xlsx"));
    for(Map<String, String> row : data) {
      System.out.println(row.toString());
    }
  }

  public static List<Map<String, String>> parse(Path path) {
    try {
      try (InputStream inStream = Files.newInputStream(path); OPCPackage pkg = OPCPackage.open(inStream);) {
        XSSFReader xssfReader = new XSSFReader(pkg);
        StylesTable styles = xssfReader.getStylesTable();
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(pkg, false);

        try (InputStream sheetInputStream = xssfReader.getSheetsData().next();) {
          XMLReader sheetParser = XMLHelper.newXMLReader();
          SimpleSheetContentsHandler handler = new SimpleSheetContentsHandler();
          sheetParser.setContentHandler(new SimpleXSSFSheetXMLHandler(styles, strings, handler, false));
          sheetParser.parse(new InputSource(sheetInputStream));
          return handler.getData();
        }
      }
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
