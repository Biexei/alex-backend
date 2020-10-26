package org.alex.platform.util;

import org.alex.platform.common.ReflectMethod;

import java.util.UUID;

public class NoUtil {
    /**
     * 生成测试套件执行日志编号
     *
     * @return 测试套件执行日志编号
     */
    public static String genSuiteLogNo() {
        ReflectMethod reflectMethod = new ReflectMethod();
        String prefix = reflectMethod.now("yyyyMMddHHmmss");
        String mid = reflectMethod.randomUpper("5");
        String suffix = reflectMethod.randomInt("5");
        return "SN" + prefix + mid + suffix;
    }

    /**
     * 生成测试套件执行日志细节编号，主要用于调用依赖case标记
     *
     * @param suiteLogNo 测试套件执行日志编号
     * @return 测试套件执行日志detail编号
     */
    public static String genSuiteLogDetailNo(String suiteLogNo) {
        String prefix = "SND";
        String suffix = suiteLogNo.substring(2);
        return prefix + suffix;
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
