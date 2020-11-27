package com.ozz.demo.office.excel;

import com.google.common.collect.Lists;
import com.ozz.demo.text.NumberFormatUtil;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.SAXException;

public class ExcelUtil {
  public static void main(String[] args) throws IOException, OpenXML4JException, SAXException, ParserConfigurationException {
//    try(Workbook wb = ExcelUtil.open(Paths.get("C:/Users/ouzezhou/Desktop/Temp/20170811/班级模板0811.xlsx"));) {
//      Row row = wb.getSheetAt(0).getRow(1);
//      System.out.println(ExcelUtil.getCellStringValue(row, 8));
//    }

    try(Workbook wb = ExcelUtil.create();) {
      CellStyle cs = wb.createCellStyle();
      ExcelCellStyleUtil.unlockCellOfProtectSheetSheet(cs);

      Sheet sheet = wb.createSheet("test");
      sheet.createRow(0).createCell(0).setCellValue("head");
      sheet.setDefaultColumnStyle(0, cs);

      Cell cell = sheet.createRow(1).createCell(0);
      cell.setCellValue("a");
      cell.setCellStyle(cs);
      cell = sheet.createRow(2).createCell(0);
      cell.setCellValue("b");
      cell.setCellStyle(cs);
      ExcelCellStyleUtil.setCombox(sheet, Lists.newArrayList("a", "b"), true, new CellRangeAddress(1, 1048575, 0, 0));

      ExcelCellStyleUtil.setAutoFilter(sheet, new CellRangeAddress(0, 0, 0, 1));

      ExcelCellStyleUtil.protectSheet(sheet, "");
      ExcelUtil.write(wb, Paths.get("C:\\Users\\ouzezhou\\Desktop\\Temp\\20191218\\test.xlsx"));
    }
  }

  public static Workbook create() {
    return new XSSFWorkbook();
  }

  public static Workbook open(Path path) throws IOException, EncryptedDocumentException, InvalidFormatException {
    try (InputStream input = Files.newInputStream(path)) {
      return open(input);
    }
  }

  public static Workbook open(InputStream input) throws EncryptedDocumentException, InvalidFormatException, IOException {
    Workbook workbook = WorkbookFactory.create(input);
    return workbook;
  }

  public static void write(Workbook workbook, Path path) throws FileNotFoundException, IOException {
    try (OutputStream out = Files.newOutputStream(path)) {
      workbook.write(out);
    }
  }

  public static Map<String, Integer> getCellValueIndexMap(Sheet sheet) {
    return getCellValueIndexMap(sheet, 0);
  }

  public static Map<String, Integer> getCellValueIndexMap(Sheet sheet, int rowIndex) {
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    Row row = sheet.getRow(rowIndex);
    if (row == null) {
      return map;
    }

    for (int i = 0; i <= row.getLastCellNum(); i++) {
      String name = getCellStringValue(row, i);
      if (StringUtils.isEmpty(name)) {
        continue;
      }
      map.put(name, i);
    }
    return map;
  }

  public static String getCellStringValue(Row row, String colEnIndex) {
    return getCellStringValue(row, NumberFormatUtil.parseEnglish(colEnIndex) - 1);
  }

  public static String getCellStringValue(Row row, int colIndex) {
    if (row == null) {
      return null;
    }
    Cell cell = row.getCell(colIndex);
    if (cell == null) {
      return null;
    }

    String result = null;
    try {
      if (cell.getCellType() == CellType.NUMERIC) {// 数字类型
        if (DateUtil.isCellDateFormatted(cell)) {// 时间
          Date date = cell.getDateCellValue();
          result = DateFormatUtils.format(date, getDateFormatString(cell.getCellStyle().getDataFormatString(), DateUtil.isCellDateFormatted(cell)));
        } else {
          result = String.valueOf(cell.getNumericCellValue()).replaceFirst("\\.0+", "");
        }
      } else if (cell.getCellType() == CellType.STRING) {// String类型
        result = cell.getStringCellValue();
      } else if (cell.getCellType() == CellType.BLANK) {
        result = "";
      } else {
        result = "";
      }
    } catch (Exception e) {
      throw new RuntimeException("读取Excel错误, " + getCellInfo(cell), e);
    }
    if (result != null) {
      result = result.trim();
    }
    return result;
  }

  public static String getDateFormatString(String formatString, boolean isDate) {
    if(isDate) {
      if ("m/d/yy".equals(formatString) || "m/d/yy".equals(formatString)) {
        return "yyyy-MM-dd";
      } else if ("m/d/yy h:mm".equals(formatString)) {
        return "yyyy-MM-dd HH:mm";
      } else {
        return formatString;
//        return "yyyy-MM-dd HH:mm:ss";
      }
    }
    return formatString;
  }

  public static Date getCellDateValue(Row row, int colIndex) {
    if (row == null) {
      return null;
    }
    Cell cell = row.getCell(colIndex);
    if (cell == null) {
      return null;
    }

    try {
      return cell.getDateCellValue();
    } catch (Exception e) {
      throw new RuntimeException("读取Excel错误, " + getCellInfo(cell), e);
    }
  }

  public static Cell setCellValue(Sheet sheet, int rowIndex, int colIndex, String value) {
    if (sheet.getRow(rowIndex) == null) {
      sheet.createRow(rowIndex);
    }
    if (sheet.getRow(rowIndex).getCell(colIndex) == null) {
      sheet.getRow(rowIndex).createCell(colIndex);
    }

    Cell cell = sheet.getRow(rowIndex).getCell(colIndex);
    cell.setCellValue(value);
    return cell;
  }

  private static String getCellInfo(Cell cell) {
    return "sheet: " + cell.getSheet().getSheetName()
           + ", 行: "
           + (cell.getRowIndex() + 1)
           + ", 列:"
           + NumberFormatUtil.formatEnglish(cell.getColumnIndex() + 1)
           + ", 值:"
           + cell.getNumericCellValue();
  }

  public static void removeRow(Sheet sheet, int rowIndex) {
    int lastRowNum = sheet.getLastRowNum();
    sheet.removeRow(sheet.getRow(rowIndex));
    if (lastRowNum == sheet.getLastRowNum() && rowIndex < sheet.getLastRowNum()) {
      sheet.shiftRows(rowIndex + 1, lastRowNum, -1);
    }
  }

  public static void loop(Workbook wb) {
    int sheetCount = wb.getNumberOfSheets();
    for (int i = 0; i < sheetCount; i++) {
      Sheet sheet = wb.getSheetAt(i);
      int rowCount = sheet.getPhysicalNumberOfRows();
      for (int j = 0; j < rowCount; j++) {
        Row row = sheet.getRow(j);
        int cellcount = row.getPhysicalNumberOfCells();
        for (int k = 0; k < cellcount; k++) {
          System.out.println("sheet:" + sheet.getSheetName() + ",row:" + (j + 1) + ",cell:" + NumberFormatUtil.formatEnglish(k + 1)+" = " + getCellStringValue(row, k));
        }
      }
    }
  }
}
