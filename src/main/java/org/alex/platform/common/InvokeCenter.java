package org.alex.platform.common;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.javafaker.Faker;
import org.alex.platform.enums.ResultType;
import org.alex.platform.pojo.DbVO;
import org.alex.platform.pojo.entity.DbConnection;
import org.alex.platform.util.JdbcUtil;
import org.alex.platform.util.MD5Util;
import org.alex.platform.util.RandomUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class InvokeCenter {
    // 返回boolean类型的统一处理成String返回
    public final static String TRUE_STR = "true";
    public final static String FALSE_STR = "false";

    byte runEnv;

    public InvokeCenter(byte runEnv) {
        this.runEnv = runEnv;
    }

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
     * 随机挑选一个参数列表的值
     *
     * @return 参数列表任意值
     */
    public String pick(String... args) {
        int len = args.length;
        if (len == 0) {
            return "";
        }
        return args[RandomUtil.randomInt(0, len - 1)];
    }

    /**
     * 随机反选一个参数列表的值
     *
     * @return 不在参数列表的任意值
     */
    public String inversePick(String... args) {
        if (args.length == 0) {
            return "";
        }
        int sumLen = 0;
        int avgLen;
        boolean isNum = true;
        Pattern pattern = Pattern.compile("[^0-9]+");
        for (String arg : args) {
            if (pattern.matcher(arg).find()) {
                isNum = false;
                break;
            }
        }
        for (String arg : args) {
            sumLen += arg.length();
        }
        avgLen = sumLen/args.length;
        String result;
        if (isNum) {
            result = RandomUtil.randomNumStringByLength(avgLen);
            while (ArrayUtils.contains(args, RandomUtil.randomNumStringByLength(avgLen))) {
                result = RandomUtil.randomNumStringByLength(avgLen);
            }
        } else {
            result = RandomUtil.randomLegalStringByLength(avgLen);
            while (ArrayUtils.contains(args, RandomUtil.randomLegalStringByLength(avgLen))) {
                result = RandomUtil.randomLegalStringByLength(avgLen);
            }
        }
        return result;
    }

    /**
     * 获取当前时间戳
     *
     * @return 返回当前时间戳
     */
    public String timestamps() {
        return String.valueOf(System.currentTimeMillis()/1000);
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
     * 返回当前时间明天
     *
     * @param format 时间格式
     * @return 返回当前时间明天
     */
    public String tomorrow(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(DateUtils.addDays(new Date(), 1));
    }

    /**
     * 返回当前时间明天
     *
     * @return 返回当前时间明天
     */
    public String tomorrow() {
        return tomorrow("yyyy-MM-dd HH:mm:ss");
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
     * 返回整形随机数
     *
     * @param minLen 最小长度
     * @param maxLen 最大长度
     * @return 返回整形随机数
     */
    public String randomInt(String minLen, String maxLen) {
        int min;
        int max;
        try {
            min = Integer.parseInt(minLen);
            max = Integer.parseInt(maxLen);
        } catch (NumberFormatException e) {
            return "0";
        }
        if (min <= 0 || max <= 0 || min > max) {
            return "0";
        }
        return RandomUtil.randomNumString(min, max);
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
     * 返回大写随机数
     *
     * @param minLen 最小长度
     * @param maxLen 最大长度
     * @return 返回大写随机数
     */
    public String randomUpper(String minLen, String maxLen) {
        int min;
        int max;
        try {
            min = Integer.parseInt(minLen);
            max = Integer.parseInt(maxLen);
        } catch (NumberFormatException e) {
            return "A";
        }
        if (min <= 0 || max <= 0 || min > max) {
            return "A";
        }
        int len = RandomUtil.randomInt(min, max);
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
     * 返回小写随机数
     *
     * @param minLen 最小长度
     * @param maxLen 最大长度
     * @return 返回小写随机数
     */
    public String randomLower(String minLen, String maxLen) {
        int min;
        int max;
        try {
            min = Integer.parseInt(minLen);
            max = Integer.parseInt(maxLen);
        } catch (NumberFormatException e) {
            return "A";
        }
        if (min <= 0 || max <= 0 || min > max) {
            return "A";
        }
        int len = RandomUtil.randomInt(min, max);
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
            return "q";
        }
        if (len <= 0) {
            return "q";
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
     * @param minLen 最小长度
     * @param maxLen 最大长度
     * @return 返回英文随机数
     */
    public String randomEn(String minLen, String maxLen) {
        String s = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
        int min;
        int max;
        try {
            min = Integer.parseInt(minLen);
            max = Integer.parseInt(maxLen);
        } catch (NumberFormatException e) {
            return "q";
        }
        if (min <= 0 || max <= 0 || min > max) {
            return "q";
        }
        int len = RandomUtil.randomInt(min, max);
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(s.charAt(random.nextInt(s.length())));
        }
        return sb.toString();
    }

    /**
     * 返回随机数字字母
     *
     * @param length 长度
     * @return 返回英文随机数
     */
    public String randomLegal(String length) {
        String s = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        int len;
        try {
            len = Integer.parseInt(length);
        } catch (NumberFormatException e) {
            return "q";
        }
        if (len <= 0) {
            return "q";
        }
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(s.charAt(random.nextInt(s.length())));
        }
        return sb.toString();
    }

    /**
     * 返回随机数字字母
     *
     * @param minLen 最小长度
     * @param maxLen 最大长度
     * @return 返回英文随机数
     */
    public String randomLegal(String minLen, String maxLen) {
        String s = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        int min;
        int max;
        try {
            min = Integer.parseInt(minLen);
            max = Integer.parseInt(maxLen);
        } catch (NumberFormatException e) {
            return "q";
        }
        if (min <= 0 || max <= 0 || min > max) {
            return "q";
        }
        int len = RandomUtil.randomInt(min, max);
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
        String s = "!@#$%^&*()_+-,.<>";
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
     * 返回英文随机数
     *
     * @param minLen 最小长度
     * @param maxLen 最大长度
     * @return 返回英文随机数
     */
    public String randomIllegal(String minLen, String maxLen) {
        String s = "!@#$%^&*()_+-,.<>";
        int min;
        int max;
        try {
            min = Integer.parseInt(minLen);
            max = Integer.parseInt(maxLen);
        } catch (NumberFormatException e) {
            return "#";
        }
        if (min <= 0 || max <= 0 || min > max) {
            return "#";
        }
        int len = RandomUtil.randomInt(min, max);
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            sb.append(s.charAt(random.nextInt(s.length())));
        }
        return sb.toString();
    }

    /**
     * 返回随机BigDecimal
     *
     * @param minSize 最小值
     * @param maxSize 最大值
     * @return 返回BigDecimal
     */
    public String nextNum(String minSize, String maxSize) {
        BigDecimal min;
        BigDecimal max;
        try {
            min = new BigDecimal(minSize);
            max = new BigDecimal(maxSize);
        } catch (Exception e) {
            return "0.0";
        }
        try {
            return RandomUtil.randomBigDecimal(min, max).toString();
        } catch (Exception e) {
            return "0.0";
        }
    }

    /**
     * 返回随机int
     *
     * @param minSize 最小值
     * @param maxSize 最大值
     * @return 返回BigDecimal
     */
    public String nextInt(String minSize, String maxSize) {
        int min;
        int max;
        try {
            min = Integer.parseInt(minSize);
            max = Integer.parseInt(maxSize);
        } catch (Exception e) {
            return "0";
        }
        try {
            return String.valueOf(RandomUtil.randomInt(min, max));
        } catch (Exception e) {
            return "0";
        }
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

    /**
     * 随机任选查询结果
     * @param dbId 数据源编号
     * @param sql sql语句
     * @param returnType 返回类型
     * @return 任意随机结果
     * @throws Exception 异常
     */
    public String select(String dbId, String sql, String returnType) throws Exception {
        int id = Integer.parseInt(dbId);
        EnvWithoutSpring env = new EnvWithoutSpring();
        DbConnection datasource = env.datasource(id, runEnv);
        String url = datasource.getUrl();
        String username = datasource.getUsername();
        String password = datasource.getPassword();
        String resultType = ResultType.getResultType(returnType).toLowerCase();
        if (resultType.equals("integer")) {
            List<Integer> list = JdbcUtil.queryForList(url, username, password, sql, Integer.class);
            return String.valueOf(list.get(new Random().nextInt(list.size())));
        } else if (returnType.equals("float")) {
            List<Float> list = JdbcUtil.queryForList(url, username, password, sql, Float.class);
            return String.valueOf(list.get(new Random().nextInt(list.size())));
        } else if (returnType.equals("double")) {
            List<Double> list = JdbcUtil.queryForList(url, username, password, sql, Double.class);
            return String.valueOf(list.get(new Random().nextInt(list.size())));
        } else if (resultType.equals("number")){
            List<BigDecimal> list = JdbcUtil.queryForList(url, username, password, sql, BigDecimal.class);
            return list.get(new Random().nextInt(list.size())).toString();
        } else {
            List<String> list = JdbcUtil.queryForList(url, username, password, sql, String.class);
            return list.get(new Random().nextInt(list.size()));
        }
    }

    /**
     * 随机反选查询结果
     * @param dbId 数据源编号
     * @param sql sql语句
     * @param returnType 返回类型
     * @return 任意随机结果
     * @throws Exception 异常
     */
    public String inverseSelect(String dbId, String sql, String returnType) throws Exception {
        int id = Integer.parseInt(dbId);
        EnvWithoutSpring env = new EnvWithoutSpring();
        DbConnection datasource = env.datasource(id, runEnv);
        String url = datasource.getUrl();
        String username = datasource.getUsername();
        String password = datasource.getPassword();
        String resultType = ResultType.getResultType(returnType).toLowerCase();
        if (resultType.equals("integer")) {
            List<Integer> list = JdbcUtil.queryForList(url, username, password, sql, Integer.class);
            int randomRow = list.get(new Random().nextInt(list.size()));
            int min = 0;
            int max = 999999;
            int invalidInt = RandomUtil.randomInt(min, max);
            while (randomRow == invalidInt) {
                invalidInt = RandomUtil.randomInt(min, max);
            }
            return String.valueOf(invalidInt);
        } else if (returnType.equals("float")) {
            List<Float> list = JdbcUtil.queryForList(url, username, password, sql, Float.class);
            Float randomRow = list.get(new Random().nextInt(list.size()));
            BigDecimal min = new BigDecimal("0.00");
            BigDecimal max = new BigDecimal("99999.99");
            BigDecimal invalidFloat = RandomUtil.randomBigDecimal(min, max);
            while (randomRow.equals(invalidFloat.floatValue())) {
                invalidFloat = RandomUtil.randomBigDecimal(min, max);
            }
            return String.valueOf(invalidFloat);
        } else if (returnType.equals("double")) {
            List<Double> list = JdbcUtil.queryForList(url, username, password, sql, Double.class);
            Double randomRow = list.get(new Random().nextInt(list.size()));
            BigDecimal min = new BigDecimal("0.00");
            BigDecimal max = new BigDecimal("99999.99");
            BigDecimal invalidDouble = RandomUtil.randomBigDecimal(min, max);
            while (randomRow.equals(invalidDouble.doubleValue())) {
                invalidDouble = RandomUtil.randomBigDecimal(min, max);
            }
            return String.valueOf(invalidDouble);
        } else if (resultType.equals("number")){
            List<BigDecimal> list = JdbcUtil.queryForList(url, username, password, sql, BigDecimal.class);
            BigDecimal randomRow = list.get(new Random().nextInt(list.size()));
            BigDecimal min = new BigDecimal("0.00");
            BigDecimal max = new BigDecimal("99999.99");
            BigDecimal invalidNum = RandomUtil.randomBigDecimal(min, max);
            while (randomRow.equals(invalidNum)) {
                invalidNum = RandomUtil.randomBigDecimal(min, max);
            }
            return String.valueOf(invalidNum);
        } else {
            List<String> list = JdbcUtil.queryForList(url, username, password, sql, String.class);
            String randomRow = list.get(new Random().nextInt(list.size()));
            String invalidStr = RandomUtil.randomLegalStringByLength(randomRow.length());
            while (randomRow.equals(invalidStr)) {
                invalidStr = RandomUtil.randomLegalStringByLength(randomRow.length());
            }
            return invalidStr;
        }
    }

    // -------------------------------------hutools--------------------------------------

    /**
     * 类似python的语法，支持负数
     * @param str 字符串
     * @param fromIndex fromIndex
     * @param toIndex toIndex
     * @return subString
     */
    public String subString(String str, String fromIndex, String toIndex) {
        int fromIndexInt;
        int toIndexInt;
        try {
            fromIndexInt = Integer.parseInt(fromIndex);
        } catch (NumberFormatException e) {
            fromIndexInt = 0;
        }
        try {
            toIndexInt = Integer.parseInt(toIndex);
        } catch (NumberFormatException e) {
            toIndexInt = -1;
        }
        return StrUtil.sub(str, fromIndexInt, toIndexInt);
    }

    /**
     * 判断是否字符串是否为数字
     * @param str 字符串
     * @return isNum
     */
    public String isNum(String str) {
        return StrUtil.isNumeric(str) ? TRUE_STR : FALSE_STR;
    }

    /**
     * 判断是否字符串是否为小写字母
     * @param str 字符串
     * @return isLowerCase
     */
    public String isLowerCase(String str) {
        return StrUtil.isLowerCase(str) ? TRUE_STR : FALSE_STR;
    }

    /**
     * 判断是否字符串是否为大写字母
     * @param str 字符串
     * @return isUpperCase
     */
    public String isUpperCase(String str) {
        return StrUtil.isUpperCase(str) ? TRUE_STR : FALSE_STR;
    }

    /**
     * 字符串中包含子串的数量
     * @param str 字符串
     * @param subStr 子串
     * @return count
     */
    public String count(String str, String subStr) {
        return String.valueOf(StrUtil.count(str, subStr));
    }

    /**
     * 判断字符串是否以xx开头
     * @param str str
     * @param subStr subStr
     * @return startsWith
     */
    public String startsWith(String str, String subStr) {
        return str.startsWith(subStr) ? TRUE_STR : FALSE_STR;
    }

    /**
     * 判断字符串是否以xx结尾
     * @param str str
     * @param subStr subStr
     * @return endsWith
     */
    public String endsWith(String str, String subStr) {
        return str.endsWith(subStr) ? TRUE_STR : FALSE_STR;
    }

    /**
     * MongoDB唯一ID生成策略
     * @return objectId
     */
    public String objectId() {
        return IdUtil.objectId();
    }

    /**
     * UUID不带-
     * @return simpleUUID
     */
    public String simpleUUID() {
        return IdUtil.simpleUUID();
    }

    /**
     * base64解码
     * @param str str
     * @return base64Decode
     */
    public String base64Decode(String str) {
        return cn.hutool.core.codec.Base64.decodeStr(str);
    }
}
