package com.bomu.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理
 */
public class DateUtils {
    private final static Logger logger = LogManager.getLogger(DateUtils.class);

    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String SIMPLE_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String getDateMonthFormat(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        return sf.format(date);
    }

    public static String getDateSimpleFormat(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat(SIMPLE_DATE_PATTERN);
        return sf.format(date);
    }

    public static String getDateFullFormat(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat(DATE_PATTERN);
        return sf.format(date);
    }

    public static String getDateFormatString(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sf.format(date);
    }

    public static String getFormatString(Date date) {
        SimpleDateFormat sf = new SimpleDateFormat("MMddHHmmss");
        return sf.format(date);
    }

    public static String getDateFormat(Date date, String pattern) {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        return sf.format(date);
    }

    public static Date getDateParse(String date, String pattern) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat(pattern);
            return sf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * yyyy-MM-dd HH:mm:ss格式的时间字符串转时间
     *
     * @param date
     * @return
     */
    public static Date getFullDate(String date) {
        return getDateParse(date, DATE_PATTERN);
    }

    /**
     * 判断两个日期是否同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean hasSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new RuntimeException("date1 and date2 must be not null.");
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        int y1 = cal1.get(Calendar.YEAR);
        int m1 = cal1.get(Calendar.MONTH);
        int d1 = cal1.get(Calendar.DAY_OF_MONTH);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int y2 = cal2.get(Calendar.YEAR);
        int m2 = cal2.get(Calendar.MONTH);
        int d2 = cal2.get(Calendar.DAY_OF_MONTH);

        if (y1 == y2 && m1 == m2 && d1 == d2) {
            return true;
        }
        return false;
    }
}
