package com.ozz.demo.office.excel;

import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ExcelCellStyleUtil {

  // IndexedColors.GREY_25_PERCENT.getIndex()
  public static void setFillForegroundColor(CellStyle cs, short color) {
    cs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cs.setFillForegroundColor(color);
  }

  public static void protectSheet(Sheet sheet, String password) {
    sheet.protectSheet(password);
    if(sheet instanceof XSSFSheet) {
      ((XSSFSheet) sheet).getCTWorksheet().getSheetProtection().setAutoFilter(false);
    }
  }

  public static void unlockCellOfProtectSheetSheet(CellStyle cs) {
    cs.setLocked(false);
  }

  public static void setAutoFilter(Sheet sheet, CellRangeAddress range) {
    sheet.setAutoFilter(range);
  }

  // HorizontalAlignment.CENTER
  public static void setAlignment(CellStyle cs, HorizontalAlignment ha, VerticalAlignment va) {
    cs.setAlignment(ha);// 水平
    cs.setVerticalAlignment(va);// 垂直
  }

  // new CellRangeAddress(firstRow, lastRow, firstCol, lastCol)
  public static void addMergedRegion(Sheet sheet, CellRangeAddress regin) {
    if(!checkRegin(regin)) {
      return;
    }
    sheet.addMergedRegion(regin);

    // 设置格式（边框等）
    if(sheet.getRow(regin.getFirstRow())!=null && sheet.getRow(regin.getFirstRow()).getCell(regin.getFirstColumn())!=null) {
      CellStyle cellStyle = sheet.getRow(regin.getFirstRow()).getCell(regin.getFirstColumn()).getCellStyle();
      for(int i=regin.getFirstRow(); i<=regin.getLastRow(); i++) {
        for(int j= regin.getFirstColumn(); j<=regin.getLastColumn(); j++) {
          Cell tc = sheet.getRow(i).getCell(j);
          if(tc == null) {
            tc = sheet.getRow(i).createCell(j);
          }
          tc.setCellStyle(cellStyle);
        }
      }
    }
  }

  private static boolean checkRegin(CellRangeAddress regin) {
    return regin.getLastRow() >= regin.getFirstRow() && regin.getLastColumn() >= regin.getFirstColumn();
  }

  public static void setBorder(CellStyle cs) {
    cs.setBorderBottom(BorderStyle.THIN); // 下边框
    cs.setBorderLeft(BorderStyle.THIN);// 左边框
    cs.setBorderTop(BorderStyle.THIN);// 上边框
    cs.setBorderRight(BorderStyle.THIN);// 右边框
  }

  public static void createFreezePane(Sheet sheet, int colSplit, int rowSplit) {
    sheet.createFreezePane(colSplit, rowSplit);
  }

  public static void createFreezeTopRow(Sheet sheet) {
    sheet.createFreezePane(0, 1);
  }

  public static void setCombox(Sheet sheet, Collection<String> selects, boolean showErrorBox, CellRangeAddress regin) {
    if(!checkRegin(regin) || CollectionUtils.isEmpty(selects)) {
      return;
    }
    DataValidationHelper helper = sheet.getDataValidationHelper();
    DataValidationConstraint constraint = helper.createExplicitListConstraint(selects.toArray(new String[]{}));
    CellRangeAddressList regions = new CellRangeAddressList();
    regions.addCellRangeAddress(regin);
    DataValidation validation = helper.createValidation(constraint, regions);
    validation.setShowErrorBox(showErrorBox);
    sheet.addValidationData(validation);
  }
}
