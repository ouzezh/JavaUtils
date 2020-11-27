package com.ozz.demo.office.excel.reader.base;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

public class MySheetContentsHandler implements SheetContentsHandler {
  private List<Map<String, String>> dataList = new ArrayList<>();
  private Map<String, String> row;
  private int skipRow = 0;

  public MySheetContentsHandler(int skipRow) {
    this.skipRow = skipRow;
  }

  @Override
  public void startRow(int rowNum) {
    if(rowNum >= skipRow) {
      row = new LinkedHashMap<>();
    } else {
      row = null;
    }
  }

  @Override
  public void endRow(int rowNum) {
    if(row != null) {
      dataList.add(row);
    }
  }

  @Override
  public void cell(String cellReference, String formattedValue, XSSFComment comment) {
    if(row != null) {
      if(formattedValue != null) {
        row.put(cellReference.replaceFirst("\\d+$", ""), formattedValue.trim());
      }
    }
  }

  public List<Map<String, String>> getDataList() {
    return dataList;
  }
}
