package org.alex.platform.util;

import org.alex.platform.common.RelyMethod;

import java.util.UUID;

public class NoUtil {
    /**
     * 生成测试套件执行日志编号
     *
     * @return 测试套件执行日志编号
     */
    public static String genSuiteLogNo() {
        RelyMethod relyMethod = new RelyMethod();
        String prefix = relyMethod.now("yyyyMMddHHmmss");
        String mid = relyMethod.randomUpper("5");
        String suffix = relyMethod.randomInt("5");
        return prefix + mid + suffix;
    }

    /**
     * 生成测试日志调用链表redis key
     *
     * @return 生成测试日志调用链表redis key
     */
    public static String genChainNo() {
        return "CN" + UUID.randomUUID().toString();
    }
}
