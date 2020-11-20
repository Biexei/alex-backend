package org.alex.platform.util;

import org.alex.platform.common.ReflectMethod;

import java.util.UUID;

public class NoUtil {

    // 当接口未包含在测试套件时，且定义了后置处理器，全局都使用该名称作为缓存key
    public static final String TEMP_POST_PROCESSOR_NO = "tempPostProcessor";

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

    /**
     * 综合用例的前置用例缓存redis hash key
     * @param caseId 用例编号
     * @return 综合用例的前置用例缓存redis hash key
     */
    public static String genCasePreNo(Integer caseId) {
        return "PC" + caseId + UUID.randomUUID().toString();
    }
}
