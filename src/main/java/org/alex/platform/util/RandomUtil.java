package org.alex.platform.util;

import java.math.BigDecimal;
import java.util.Random;

public class RandomUtil {
    private static final String illegalString = "!@#$%^&*()_+-,.<>";
    private static final String legalString = "123456790abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String numString = "0123456789";

    /**
     * 指定范围的的随机数
     * @param minimum 最小值
     * @param maximum 最大值
     * @return 随机数
     */
    public static int randomInt(int minimum, int maximum) {
        return new Random().nextInt((maximum - minimum) + 1) + minimum;
    }

    /**
     * 指定长度区间的合法字符串
     * @param minLen 最小长度
     * @param maxLen 最大长度
     * @return 随机字符串
     */
    public static String randomLegalString(int minLen, int maxLen) {
        int len = randomInt(minLen, maxLen);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(legalString.charAt(new Random().nextInt(legalString.length())));
        }
        return sb.toString();
    }

    /**
     * 指定长度区间的数字字符串
     * @param minLen 最小长度
     * @param maxLen 最大长度
     * @return 随机字符串
     */
    public static String randomNumString(int minLen, int maxLen) {
        int len = randomInt(minLen, maxLen);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(numString.charAt(new Random().nextInt(numString.length())));
        }
        return sb.toString();
    }

    /**
     * 指定长度区间的非法字符串
     * @param minLen 最小长度
     * @param maxLen 最大长度
     * @return 随机字符串
     */
    public static String randomIllegalString(int minLen, int maxLen) {
        int len = randomInt(minLen, maxLen);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(illegalString.charAt(new Random().nextInt(illegalString.length())));
        }
        return sb.toString();
    }

    /**
     * 指定长度的随机合法字符串
     * @param length 长度
     * @return 指定长度的随机字符串
     */
    public static String randomLegalStringByLength(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(legalString.charAt(new Random().nextInt(legalString.length())));
        }
        return sb.toString();
    }

    /**
     * 指定长度的随机数字字符串
     * @param length 长度
     * @return 指定长度的随机字符串
     */
    public static String randomNumStringByLength(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(numString.charAt(new Random().nextInt(numString.length())));
        }
        return sb.toString();
    }

    /**
     * 指定长度的随机非法字符串
     * @param length 长度
     * @return 指定长度的随机字符串
     */
    public static String randomIllegalStringByLength(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(illegalString.charAt(new Random().nextInt(illegalString.length())));
        }
        return sb.toString();
    }

    /**
     * 生成随机BigDecimal数
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    public static BigDecimal randomBigDecimal(BigDecimal min, BigDecimal max) {
        int minInt = min.intValue();
        int maxInt = max.intValue();

        String minStr = min.toString();
        String maxStr = max.toString();

        int minPointIndex = minStr.indexOf(".");
        int maxPointIndex = maxStr.indexOf(".");

        if (minPointIndex == -1 && maxPointIndex == -1) {
            return BigDecimal.valueOf(RandomUtil.randomInt(min.intValue(), max.intValue()));
        } else {
            int minDecimalLen = minPointIndex == -1 ? 0 : minStr.substring(minPointIndex).length() - 1;
            int maxDecimalLen = maxPointIndex == -1 ? 0 : maxStr.substring(maxPointIndex).length() - 1;

            int decimal = Math.max(minDecimalLen, maxDecimalLen);
            StringBuilder patten = new StringBuilder(".");
            for (int i = 0; i < decimal; i++) {
                patten.append(new Random().nextInt(10));
            }

            int value = RandomUtil.randomInt(minInt, maxInt);
            BigDecimal bigDecimalValue = new BigDecimal(value + patten.toString());
            while (bigDecimalValue.compareTo(min) < 0 || bigDecimalValue.compareTo(max) > 0) {
                patten = new StringBuilder(".");
                for (int i = 0; i < decimal; i++) {
                    patten.append(new Random().nextInt(10));
                }
                value = RandomUtil.randomInt(minInt, maxInt);
                bigDecimalValue = new BigDecimal(value + patten.toString());
            }
            return bigDecimalValue;
        }
    }
}
