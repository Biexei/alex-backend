package org.alex.platform.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    /**
     * 获取当前毫秒
     * @return 毫秒
     */
    public static long now() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前日期
     * @param patten 格式
     * @return 获取当前日期
     */
    public static String date(String patten) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patten);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取当前日期 yyyy-MM-dd HH:mm:ss
     * @return 获取当前日期
     */
    public static String date() {
        return date("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 时间转换
     * @return 时间转换
     */
    public static String format(Date date, String patten) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patten);
        return simpleDateFormat.format(date);
    }

    /**
     * 时间转换
     * @return 时间转换
     */
    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * date 转 时间戳
     * @param date date
     * @return 时间戳
     */
    public static long date2timestamp(Date date) {
        return date.getTime();
    }

    /**
     * date string 转 时间戳
     * @param date date
     * @param patten date对应的patten
     * @return 时间戳
     * @throws ParseException 解析异常
     */
    public static long date2timestamp(String date, String patten) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(patten);
        return format.parse(date).getTime();
    }

    /**
     * date string 转 时间戳
     * @param date date
     * @param format SimpleDateFormat
     * @return 时间戳
     * @throws ParseException 解析异常
     */
    public static long date2timestamp(String date, SimpleDateFormat format) throws ParseException {
        return format.parse(date).getTime();
    }

    /**
     * ms转时分秒
     * @param ms 毫秒
     * @return 时分秒
     */
    public static String convert2hms(long ms) {
        long hours = ms/(3600000);
        long minutes = (ms%3600000)/60000;
        long seconds = (ms%60000)/1000;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
