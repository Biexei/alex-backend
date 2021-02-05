package org.alex.platform.util;

import java.util.Random;

public class RandomUtil {
    private static final String illegalString = "!@#$%^&*()_+-/,.\\<>";
    private static final String legalString = "123456790abcdefghijklmnopqrstuvwxyz";

    /**
     * 指定长度区间的的随机数
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

    public static void main(String[] args) {
        System.out.println(RandomUtil.randomIllegalStringByLength(10));
    }
}
