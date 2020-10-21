package org.alex.platform.common;

import com.github.javafaker.Faker;
import org.alex.platform.util.MD5Util;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReflectMethod {

    // faker 单例
    private static class SingleFaker{
        private static final Faker INSTANCE = new Faker(Locale.SIMPLIFIED_CHINESE);
    }

    public static Faker getInstance() {
        return SingleFaker.INSTANCE;
    }

    /**
     * md5
     *
     * @param s 原文
     * @return md5加密后结果
     */
    public String md5(String s) {
        return MD5Util.MD5Encode(s, "utf-8");
    }

    /**
     * 返回uuid
     *
     * @return 返回uuid
     */
    public String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 返回base64编码后结果
     *
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
     *
     * @return 返回当前时间戳
     */
    public String timestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 返回当前时间
     *
     * @param format 时间格式
     * @return 返回当前时间
     */
    public String now(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    /**
     * 返回当前时间
     *
     * @return String
     */
    public String now() {
        return now("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 返回当前时间昨天
     *
     * @param format 时间格式
     * @return 返回当前时间昨天
     */
    public String yesterday(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(DateUtils.addDays(new Date(), -1));
    }

    /**
     * 返回当前时间昨天
     *
     * @return 返回当前时间昨天
     */
    public String yesterday() {
        return yesterday("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 返回当前时间上个月
     *
     * @param format 时间格式
     * @return 返回当前时间上个月
     */
    public String lastMonth(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(DateUtils.addMonths(new Date(), -1));
    }

    /**
     * 返回当前时间上个月
     *
     * @return 返回当前时间上个月
     */
    public String lastMonth() {
        return lastMonth("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 返回当前时间下个月
     *
     * @param format 时间格式
     * @return 返回当前时间下个月
     */
    public String nextMonth(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(DateUtils.addMonths(new Date(), 1));
    }

    /**
     * 返回当前时间下个月
     *
     * @return 返回当前时间下个月
     */
    public String nextMonth() {
        return nextMonth("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 返回当前时间去年
     *
     * @param format 时间格式
     * @return 返回当前时间去年
     */
    public String lastYear(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(DateUtils.addYears(new Date(), -1));
    }

    /**
     * 返回当前时间去年
     *
     * @return 返回当前时间去年
     */
    public String lastYear() {
        return lastYear("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 返回当前时间明年
     *
     * @param format 时间格式
     * @return 返回当前时间明年
     */
    public String nextYear(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(DateUtils.addYears(new Date(), 1));
    }

    /**
     * 返回当前时间明年
     *
     * @return 返回当前时间明年
     */
    public String nextYear() {
        return nextYear("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 返回指定日期的时间
     *
     * @param operator 年月日时分秒
     * @param amount   数量
     * @param format   时间格式
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
        switch (operator) {
            case "y":  // 年
                return sdf.format(DateUtils.addYears(new Date(), amt));
            case "M":  // 月
                return sdf.format(DateUtils.addMonths(new Date(), amt));
            case "d":  // 日
                return sdf.format(DateUtils.addDays(new Date(), amt));
            case "h":  // 时
                return sdf.format(DateUtils.addHours(new Date(), amt));
            case "m":  // 分
                return sdf.format(DateUtils.addMinutes(new Date(), amt));
            case "s":  // 秒
                return sdf.format(DateUtils.addSeconds(new Date(), amt));
            default:  // 没有匹配的操作时间则返回当前时间
                return sdf.format(new Date());
        }
    }

    /**
     * 返回指定日期的时间
     * @param operator 年月日时分秒
     * @param amount 数量
     * @return  返回指定日期的时间
     */
    public String time(String operator, String amount) {
        return time(operator, amount, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 返回整形随机数
     *
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
     *
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
     *
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
     *
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
     *
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

    /**
     * 随机获取城市
     * @return 随机城市
     */
    public String city() {
        return getInstance().address().city();
    }


    /**
     * 获取随机省份
     * @return 随机省份
     */
    public String province() {
        return getInstance().address().state();
    }

    /**
     * 获取随机国家
     * @return 随机国家
     */
    public String country() {
        return getInstance().address().country();
    }

    /**
     * 获取随机手机号
     * @return 随机手机号
     */
    public String phone() {
        return getInstance().phoneNumber().cellPhone();
    }

    /**
     * 获取随机邮箱
     * @return 随机邮箱
     */
    public String email() {
        Faker faker = new Faker(Locale.ENGLISH);
        return faker.internet().emailAddress();
    }

    /**
     * 获取随机mac地址
     * @return 随机mac地址
     */
    public String mac() {
        return getInstance().internet().macAddress();
    }

    /**
     * 获取随机书籍
     * @return 随机书籍
     */
    public String book() {
        return getInstance().book().title();
    }

    /**
     * 获取随机名字
     * @return 随机名称
     */
    public String name() {
        return getInstance().name().fullName();
    }

    /**
     * 随机ipv4
     * @return ipv4
     */
    public String ipv4() {
        return getInstance().internet().ipV4Address();
    }

    /**
     * 随机私有ipv4
     * @return 随机私有ipv4
     */
    public String privateIpv4() {
        return getInstance().internet().privateIpV4Address();
    }

    /**
     * 随机公有ipv4
     * @return 随机公有ipv4
     */
    public String publicIpv4() {
        return getInstance().internet().publicIpV4Address();
    }

    /**
     * 随机ipv6
     * @return ipv6
     */
    public String ipv6() {
        return getInstance().internet().ipV6Address();
    }

    public static void main(String[] args) {
        System.out.println(new ReflectMethod().publicIpv4());
    }
}
