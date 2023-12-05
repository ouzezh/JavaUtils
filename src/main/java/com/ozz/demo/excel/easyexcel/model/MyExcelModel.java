package com.ozz.demo.excel.easyexcel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.BorderStyleEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import com.ozz.demo.excel.easyexcel.convetor.MyConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.RoundingMode;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
// EasyExcel不可以使用注解 @Accessors(chain = true)
@HeadFontStyle(fontHeightInPoints = 15)
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER
    , borderTop = BorderStyleEnum.THIN, borderBottom = BorderStyleEnum.THIN, borderLeft = BorderStyleEnum.THIN, borderRight = BorderStyleEnum.THIN
    , fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 22)
@ContentFontStyle(fontHeightInPoints = 12)
@ContentStyle(locked = BooleanEnum.TRUE, wrapped = BooleanEnum.FALSE
    , horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER
    , borderTop = BorderStyleEnum.THIN, borderBottom = BorderStyleEnum.THIN, borderLeft = BorderStyleEnum.THIN, borderRight = BorderStyleEnum.THIN)
public class MyExcelModel {

  /**
   * 我想所有的 字符串起前面加上"自定义："三个字
   */
  @ContentLoopMerge(eachRow = 2) //  // 这一列 每隔2行 合并单元格
  @ExcelProperty(value = "字符串标题", converter = MyConverter.class)
  @ColumnWidth(20)
  private String str;
  /**
   * 我想写到excel 用年月日的格式
   */
  @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
  @ExcelProperty({"G", "日期标题"})
  @ColumnWidth(30)
  private Date date;
  /**
   * 我想写到excel 用百分比表示
   */
  @NumberFormat(value = "0.00%", roundingMode = RoundingMode.HALF_UP)
  @ExcelProperty(value = {"G", "数字标题"})
  private Double doubleData;

}
