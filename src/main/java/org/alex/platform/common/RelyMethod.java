package org.alex.platform.common;

import org.alex.platform.util.MD5Util;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class RelyMethod {
    /**
     * md5
     * @param s 原文
     * @return md5加密后结果
     */
    public String md5(String s) {
        return MD5Util.MD5Encode(s, "utf-8");
    }

    /**
     * 返回uuid
     * @return 返回uuid
     */
    public String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 返回base64编码后结果
     * @param s 原文
     * @return 返回base64编码后结果
     */
    public String base64(String s) {
        try {
            return Base64.getEncoder().encodeToString(s.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当前时间戳
     * @return 返回当前时间戳
     */
    public String timestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 返回当前时间
     * @param format 时间格式
     * @return 返回当前时间
     */
    public String now(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    /**
     * 返回当前时间昨天
     * @param format 时间格式
     * @return 返回当前时间昨天
     */
    public String yesterday(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(DateUtils.addDays(new Date(), -1));
    }

    /**
     * 返回当前时间上个月
     * @param format 时间格式
     * @return 返回当前时间上个月
     */
    public String lastMonth(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(DateUtils.addMonths(new Date(), -1));
    }

    /**
     * 返回当前时间下个月
     * @param format 时间格式
     * @return 返回当前时间下个月
     */
    public String nextMonth(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(DateUtils.addMonths(new Date(), 1));
    }

    /**
     * 返回当前时间去年
     * @param format 时间格式
     * @return 返回当前时间去年
     */
    public String lastYear(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(DateUtils.addYears(new Date(), -1));
    }

    /**
     * 返回当前时间明年
     * @param format 时间格式
     * @return 返回当前时间明年
     */
    public String nextYear(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(DateUtils.addYears(new Date(), 1));
    }

    /**
     * 返回指定日期的时间
     * @param operator 年月日时分秒
     * @param amount 数量
     * @param format 时间格式
     * @return 返回指定日期的时间
     */
    public String time(String operator, String amount, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        int amt;
        try {
            amt = Integer.parseInt(amount);
        } catch (NumberFormatException e) { // 类转换异常中则将amount设置成0
            amt = 0;
        }
        if (operator.equals("y")) { // 年
            return sdf.format(DateUtils.addYears(new Date(), amt));
        } else if (operator.equals("M")) { // 月
            return sdf.format(DateUtils.addMonths(new Date(), amt));
        } else if (operator.equals("d")) { // 日
            return sdf.format(DateUtils.addDays(new Date(), amt));
        } else if (operator.equals("h")) { // 时
            return sdf.format(DateUtils.addHours(new Date(), amt));
        } else if (operator.equals("m")) { // 分
            return sdf.format(DateUtils.addMinutes(new Date(), amt));
        } else if (operator.equals("s")) { // 秒
            return sdf.format(DateUtils.addSeconds(new Date(), amt));
        } else { // 没有匹配的操作时间则返回当前时间
            return sdf.format(new Date());
        }
    }

    /**
     * 返回整形随机数
     * @param length 长度
     * @return 返回整形随机数
     */
    public String randomInt(String length) {
        int len;
        try {
            len = Integer.parseInt(length);
        } catch (NumberFormatException e) {
            return "0";
        }
        if (len <= 0) {
            return "0";
        }
        return RandomStringUtils.random(len, 48, 57, false, false);
    }

    /**
     * 返回大写随机数
     * @param length 长度
     * @return 返回大写随机数
     */
    public String randomUpper(String length) {
        int len;
        try {
            len = Integer.parseInt(length);
        } catch (NumberFormatException e) {
            return "A";
        }
        if (len <= 0) {
            return "A";
        }
        return RandomStringUtils.random(len, 65, 90, false, false);
    }

    /**
     * 返回小写随机数
     * @param length 长度
     * @return 返回小写随机数
     */
    public String randomLower(String length) {
        int len;
        try {
            len = Integer.parseInt(length);
        } catch (NumberFormatException e) {
            return "a";
        }
        if (len <= 0) {
            return "a";
        }
        return RandomStringUtils.random(len, 97, 122, false, false);
    }

    /**
     * 返回英文随机数
     * @param length 长度
     * @return 返回英文随机数
     */
    public String randomEn(String length) {
        String s = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        int len;
        try {
            len = Integer.parseInt(length);
        } catch (NumberFormatException e) {
            return "a";
        }
        if (len <= 0) {
            return "a";
        }
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(s.charAt(random.nextInt(s.length())));
        }
        return sb.toString();
    }

    /**
     * 返回英文随机数
     * @param length 长度
     * @return 返回英文随机数
     */
    public String randomIllegal(String length) {
        String s = "!@#$%^&*()_+-/,.\\ <>";
        int len;
        try {
            len = Integer.parseInt(length);
        } catch (NumberFormatException e) {
            return "#";
        }
        if (len <= 0) {
            return "#";
        }
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(s.charAt(random.nextInt(s.length())));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        RelyMethod rm = new RelyMethod();
        System.out.println(rm.randomIllegal("100"));
    }
}
