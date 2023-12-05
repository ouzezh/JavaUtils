package com.ozz.demo.excel.poi;

import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFSheet;

public class ExcelStyleUtil {
  /**
   * 保护工作表
   */
  public static void protectSheet(Sheet sheet, String password) {
    sheet.protectSheet(password);
    if(sheet instanceof SXSSFSheet) {
      ((SXSSFSheet) sheet).lockAutoFilter(false);
    }
  }

  /**
   * 筛选
   */
  // new CellRangeAddress(firstRow, lastRow, firstCol, lastCol)
  public static void setAutoFilter(Sheet sheet, CellRangeAddress range) {
    sheet.setAutoFilter(range);
  }

  /**
   * 合并单元格
   */
  public static void addMergedRegion(Sheet sheet, CellRangeAddress range) {
    sheet.addMergedRegion(range);
  }

  /**
   * 冻结窗格首行
   */
  public static void createFreezeTopRow(Sheet sheet) {
    createFreezePane(sheet,0, 1);
  }

  /**
   * 冻结窗格
   *
   * @param col 冻结的列数
   * @param row 冻结的行数
   */
  public static void createFreezePane(Sheet sheet, int col, int row) {
    sheet.createFreezePane(col, row);
  }

  /**
   * 下拉框
   */
  public static void setCombobox(Sheet sheet, String[] listOfValues, boolean showErrorBox, CellRangeAddress range) {
    if(listOfValues==null || listOfValues.length==0) {
      return;
    }
    DataValidationHelper helper = sheet.getDataValidationHelper();
    DataValidationConstraint constraint = helper.createExplicitListConstraint(listOfValues);
    CellRangeAddressList rangeList = new CellRangeAddressList();
    rangeList.addCellRangeAddress(range);
    DataValidation validation = helper.createValidation(constraint, rangeList);
    validation.setShowErrorBox(showErrorBox);
    sheet.addValidationData(validation);
  }
}
