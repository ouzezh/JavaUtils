package com.ozz.demo.office.excel.reader.base;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;

import com.ozz.demo.office.excel.ExcelUtil;

/**
 * modify from org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler
 * 
 * poi-ooxml 4.1.0
 * 
 * 改动:(1)将parseNmuber逻辑写入方法并覆盖(2)移除内部类SheetContentsHandler
 * 
 */
public class SimpleXSSFSheetXMLHandler extends XSSFSheetXMLHandler {

  public SimpleXSSFSheetXMLHandler(StylesTable styles, ReadOnlySharedStringsTable strings,
      org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler sheetContentsHandler, boolean formulasNotResults) {
    super(styles, strings, sheetContentsHandler, formulasNotResults);
  }

  public SimpleXSSFSheetXMLHandler(StylesTable styles, ReadOnlySharedStringsTable strings, SimpleSheetContentsHandler handler, boolean formulasNotResults) {
    super(styles, strings, handler, formulasNotResults);
  }

  @Override
  String parseNmuber(StringBuilder value, DataFormatter formatter, short formatIndex, String formatString) {
    if("General".equals(formatString)) {
      return value.toString();
    } else {
      formatString = new ExcelUtil().getDateFormatString(formatString);
      return super.parseNmuber(value, formatter, formatIndex, formatString);
    }
  }
}
