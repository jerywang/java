/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.dubbo.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Comment of DateUtils
 *
 */
public class DateUtils {
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String format(Date date, String format) {
        if (date == null) {
            return null;
        }
        DateFormat format1 = new SimpleDateFormat(format);
        String str = format1.format(date);
        return str;
    }

    public static String format(Date date) {
        return format(date, DEFAULT_FORMAT);
    }

    public static Date parse(String dateStr, String format) {
        if (dateStr == null) {
            return null;
        }
        DateFormat format1 = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = format1.parse(dateStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        return date;
    }

    public static Date parse(String dateStr) {
        return parse(dateStr, DEFAULT_FORMAT);
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month - 1, day);
        return cal.getTime();
    }

    public static Date getDate(int year, int month, int day, int hour,
                               int minute, int second) {
        Calendar cal = new GregorianCalendar(year, month - 1, day, hour,
                minute, second);
        return cal.getTime();
    }

    public static Date newDate(int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month - 1, day);
        return cal.getTime();
    }

    public static Date getDayDate(Date d) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        return newDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH));
    }

    public static Date getNextNDay(Date dt, int n) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return new GregorianCalendar(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH) + n)
                .getTime();
    }

    public static String getTimeInterval(Date time1, Date time2) {
        long secondInterval = (time2.getTime() - time1.getTime()) / 1000;
        if (secondInterval < 60) {
            return "1分钟以内";
        }
        if (secondInterval < 3600) {
            long min = secondInterval / 60;
            return min + "分钟前";
        }
        if (secondInterval < 86400) {
            long hour = secondInterval / 3600;
            return hour + "小时前";
        }
        return format(time1, "yyyy年MM月dd日");
    }

}
