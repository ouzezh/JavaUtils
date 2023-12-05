package com.ozz.demo.date;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.SneakyThrows;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

  public static String format(LocalDateTime ldt) {
      return LocalDateTimeUtil.formatNormal(ldt);
  }

  public static LocalDateTime toLocalDateTime(Date date) {
    return DateUtil.toLocalDateTime(date);
  }

  public static Date toDate(LocalDateTime localDateTime) {
    return new Date(LocalDateTimeUtil.toEpochMilli(localDateTime));
  }

  public static ZoneId getZoneId() {
    return ZoneId.systemDefault();
  }

  @SneakyThrows
  public static Date parseDate(String dateString) {
    return DateUtil.parseDate(dateString);
  }

  public static long between(LocalDate d1, LocalDate d2) {
      return d2.toEpochDay() - d1.toEpochDay();
  }
  public static Duration between(LocalDateTime d1, LocalDateTime d2) {
      return LocalDateTimeUtil.between(d1, d2);
  }

  public static String toString(long millis) {
      return Duration.ofMillis(millis).toString();
  }

  public static String getExpectedTime(long startTime, int currentNum, int totalNum) {
    int passedNum = currentNum - 1;
    long costTime = System.currentTimeMillis() - startTime;
    StringBuilder mess =
        new StringBuilder(String.valueOf(currentNum)).append(" of ").append(totalNum)
            .append(", cost ").append(toString(costTime));
    if (passedNum <= 0) {
      return mess.toString();
    }

    if (totalNum >= currentNum) {
      mess.append(", left ")
          .append(toString((totalNum - passedNum) * (costTime / passedNum)));
    }
    return mess.toString();
  }

}
