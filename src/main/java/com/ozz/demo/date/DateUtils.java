package com.ozz.demo.date;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
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

    public static String formatBetween(long millis) {
//      return Duration.ofMillis(millis).toString();
        return DateUtil.formatBetween(millis);
    }

    public static String getExpectedTime(long startMillis, long passedCou, long totalCou) {
        Assert.isTrue(passedCou > 0);
        long costTime = System.currentTimeMillis() - startMillis;
        StringBuilder mess =
                new StringBuilder(String.valueOf(passedCou)).append(" of ").append(totalCou)
                        .append(", cost ").append(formatBetween(costTime));

        if (totalCou >= passedCou) {
            long leftCou = totalCou - passedCou;
            mess.append(", left ")
                    .append(formatBetween(costTime * leftCou / passedCou));
        }
        return mess.toString();
    }

}
