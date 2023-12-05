package com.ozz.demo.excel.easyexcel.convetor;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.util.Objects;

public class MyConverter implements Converter<Object> {

  @Override
  public Class supportJavaTypeKey() {
    return null;
  }

  @Override
  public CellDataTypeEnum supportExcelTypeKey() {
    return null;
  }

  @Override
  public Object convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
                                  GlobalConfiguration globalConfiguration) {
    return String.format("excel->java: %s", Objects.toString(cellData.getStringValue(), StrUtil.EMPTY).trim());
  }

  @Override
  public WriteCellData convertToExcelData(Object value, ExcelContentProperty contentProperty,
                                          GlobalConfiguration globalConfiguration) {
    return new WriteCellData(String.format("java->excel: %s", Objects.toString(value, StrUtil.EMPTY).trim()));
  }
}
