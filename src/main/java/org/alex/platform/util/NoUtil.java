package org.alex.platform.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class NoUtil {

    // 当接口未包含在测试套件时，且定义了后置处理器，全局都使用该名称作为缓存key
    public static final String TEMP_POST_PROCESSOR_NO = "TEMP_POST_PROCESSOR_NO";

    // 重置密码默认密码
    public static final String DEFAULT_PWD = "123456";

    // 用户密码盐
    public static final String PWD_SALT = "66CD1CF5$C062#401C@8C21!13A94F950955";

    /**
     * 生成测试套件执行日志编号
     *
     * @return 测试套件执行日志编号
     */
    public static String genSuiteLogNo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String prefix = format.format(new Date());
        String mid = RandomUtil.randomLegalStringByLength(5).toUpperCase();
        String suffix = RandomUtil.randomNumStringByLength(5);
        return "SN" + prefix + mid + suffix;
    }

    /**
     * 生成测试套件执行日志编号进度缓存key
     * 每执行一个测试套件用例，都根据该编号去调整缓存中的执行状态
     * @return 测试套件执行日志进度编号
     */
    public static String genSuiteLogProgressNo(String suiteLogNo) {

        return "PROGRESS-" + suiteLogNo;
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
     *
     * @return 综合用例的前置用例缓存redis hash key
     */
    public static String genCasePreNo() {
        return "PC" + UUID.randomUUID().toString();
    }

    /**
     * 接口测试用例导入编号
     * @return 生成导入编号
     */
    public static String genIfImportNo() {
        return "IFI" + UUID.randomUUID().toString();
    }

    /**
     * 调用python正交算法
     * @return 生成redis key
     */
    public static String genPyOrtNo() {
        return "PY-ORT" + UUID.randomUUID().toString();
    }

    /**
     * 生成稳定性测试日志的编号
     * @return 生成稳定性测试日志的编号
     */
    public static String genStabilityLogNo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String prefix = format.format(new Date());
        String mid = RandomUtil.randomLegalStringByLength(5).toUpperCase();
        String suffix = RandomUtil.randomNumStringByLength(5);
        return "SPL" + prefix + mid + suffix;
    }

    /**
     * 稳定性日志近期日志缓存key
     * @param stabilityTestLogNo stabilityTestLogNo
     * @return 稳定性日志近期日志缓存key
     */
    public static String genStabilityLogLast10No(String stabilityTestLogNo) {
        return "LOG-LAST-" + stabilityTestLogNo;
    }

    /**
     * 稳定性响应时间缓存key
     * @param stabilityTestLogNo stabilityTestLogNo
     * @return 稳定性响应时间缓存key
     */
    public static String genStabilityLogResponseTimeQueueNo(String stabilityTestLogNo) {
        return "RESPONSE-TIME-QUEUE-" + stabilityTestLogNo;
    }
}
