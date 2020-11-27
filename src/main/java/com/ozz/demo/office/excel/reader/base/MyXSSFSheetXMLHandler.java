package com.ozz.demo.office.excel.reader.base;

import com.ozz.demo.office.excel.ExcelUtil;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;

/**
 * modify from org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler
 * 
 * poi-ooxml 4.1.0
 * 
 * 改动:(1)将parseNmuber逻辑写入方法并覆盖(2)移除内部类SheetContentsHandler
 * 
 */
public class MyXSSFSheetXMLHandler extends XSSFSheetXMLHandler {
  public MyXSSFSheetXMLHandler(StylesTable styles, ReadOnlySharedStringsTable strings,
      org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler sheetContentsHandler, boolean formulasNotResults) {
    super(styles, strings, sheetContentsHandler, formulasNotResults);
  }

  @Override
  String parseNumber(StringBuilder value, DataFormatter formatter, short formatIndex, String formatString) {
    org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler a;
    if("General".equals(formatString)) {
      return value.toString();
    } else {
      formatString = ExcelUtil.getDateFormatString(formatString, DateUtil.isADateFormat(formatIndex, formatString));
      return super.parseNumber(value, formatter, formatIndex, formatString);
    }
  }
}
