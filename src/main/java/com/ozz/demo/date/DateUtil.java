package com.ozz.demo.date;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @author ozz
 */
public class DateUtil {

  public static String PATTERN_YEAR = "yyyy";
  public static String PATTERN_DATE = "yyyy-MM-dd";
  public static String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
  public static String[] PATTERNS = new String[]{PATTERN_YEAR, PATTERN_DATE, "yyyy-MM-dd HH:mm",
      PATTERN_DATETIME, "yyyy-MM-dd HH:mm:ss.SSS"};

  public static void main(String[] args) {
    test();
  }

  public static void test() {
    Date date = new Date();
    LocalDateTime localDateTime = dateToLocalDateTime(date);
    System.out.println(localDateTime.format(DateTimeFormatter.ofPattern(PATTERN_DATETIME)));

    localDateTime = LocalDate.now().atStartOfDay();
    Date date2 = localDateTimeToDate(localDateTime);
    System.out.println(DateFormatUtils.format(date2, PATTERN_DATETIME));
  }

  public static LocalDateTime dateToLocalDateTime(Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), getZoneId());
  }

  public static Date localDateTimeToDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(getZoneId()).toInstant());
  }

  public static ZoneId getZoneId() {
    return ZoneId.systemDefault();
  }

  public static Date parseDate(String date) throws ParseException {
    return DateUtils.parseDate(date, PATTERNS);
  }

  public static String getTimeStringByMillis(long millis) {
    String[] modUnits = {"d", "H", "m", "s", "ms"};
    long[] mods = {24, 60, 60, 1000, 1};

    if (millis <= 0) {
      return millis + modUnits[modUnits.length - 1];
    }

    long mod = 1;
    for (long t : mods) {
      mod = mod * t;
    }

    long tmpTime = millis;
    StringBuilder timeString = new StringBuilder();
    int bit = 0;
    for (int i = 0; i < modUnits.length && bit <= 2; i++) {
      long curr = tmpTime / mod;
      tmpTime = tmpTime % mod;
      mod = mod / mods[i];
      if (curr > 0) {
        bit++;
        timeString.append(curr).append(modUnits[i]);
      }
      if (bit > 0) {
        bit++;
      }
    }

    return timeString.toString();
  }

  public static String getStatisticsMess(long startTime, int currentNum, int totalNum) {
    int passedNum = currentNum - 1;
    long costTime = System.currentTimeMillis() - startTime;
    StringBuilder mess =
        new StringBuilder(String.valueOf(currentNum)).append(" of ").append(totalNum)
            .append(", cost ").append(getTimeStringByMillis(costTime));
    if (passedNum <= 0) {
      return mess.toString();
    }

    if (totalNum >= currentNum) {
      mess.append(", left ")
          .append(getTimeStringByMillis((totalNum - passedNum) * (costTime / passedNum)));
    }
    return mess.toString();
  }

}
