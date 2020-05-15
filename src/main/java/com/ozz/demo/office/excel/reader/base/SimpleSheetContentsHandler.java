package com.ozz.demo.office.excel.reader.base;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

public class SimpleSheetContentsHandler implements SheetContentsHandler {
//  private Logger log = LoggerFactory.getLogger(getClass());

  private List<Map<String, String>> data = new ArrayList<>();
  private Map<String, String> row;

  @Override
  public void startRow(int rowNum) {
    row = new LinkedHashMap<>();
  }

  @Override
  public void endRow(int rowNum) {
    data.add(row);
  }

  @Override
  public void cell(String cellReference, String formattedValue, XSSFComment comment) {
    row.put(cellReference, formattedValue);
  }

  @Override
  public void headerFooter(String text, boolean isHeader, String tagName) {}

  public List<Map<String, String>> getData() {
    return data;
  }
}
