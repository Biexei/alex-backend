package org.alex.platform.generator;

import org.springframework.stereotype.Component;

/**
 * 根据约束信息生成标题
 */
@Component
public class Description {
    public String desc4Null(String key, String desc) {
        return String.format("%s<%s>为null ", desc, key);
    }

    public String desc4NotNull(String key, String desc) {
        return String.format("%s<%s>非null ", desc, key);
    }

    public String desc4Empty(String key, String desc) {
        return String.format("%s<%s>为空字符串 ", desc, key);
    }

    public String desc4NotEmpty(String key, String desc) {
        return String.format("%s<%s>非空字符串 ", desc, key);
    }

    public String desc4Length(String key, String desc, int minLen, int maxLen) {
        return String.format("%s<%s>长度在区间[%s,%s]内 ", desc, key, minLen, maxLen);
    }

    public String desc4LengthRepeat(String key, String desc, int minLen, int maxLen) {
        return String.format("%s<%s>长度在区间[%s,%s]内-重复 ", desc, key, minLen, maxLen);
    }

    public String desc4LessLength(String key, String desc, int minLen, int step) {
        return String.format("%s<%s>长度为最小长度<%s>-%s ", desc, key, minLen, step);
    }

    public String desc4GreaterLength(String key, String desc, int maxLen, int step) {
        return String.format("%s<%s>长度为最大长度<%s>+%s ", desc, key, maxLen, step);
    }

    public String desc4EqualsMinLength(String key, String desc, int minLen) {
        return String.format("%s<%s>长度恰好为最小长度<%s> ", desc, key, minLen);
    }

    public String desc4EqualsMaxLength(String key, String desc, int maxLen) {
        return String.format("%s<%s>长度恰好为最大长度<%s> ", desc, key, maxLen);
    }

    public static void main(String[] args) {
        Description description = new Description();
        System.out.println(description.desc4EqualsMinLength("name", "名称", 10));
    }
}
