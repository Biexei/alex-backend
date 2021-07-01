package org.alex.platform.pojo.param;

import com.github.javafaker.Bool;

import java.util.HashMap;

/**
 * interfaceCaseId 用例编号
 * executor 执行人，若作为依赖数据应用则应该传参‘系统调度’
 * suiteLogNo 测试套件日志编号 主要为调用入口为测试套件时使用，否则传参null
 * chainNo 调用链路跟踪 每次调用均会将自增日志编号写入缓存，再序列化
 * suiteId 测试套件编号，主要用于调用入口为测试套件时确定运行环境，否则应该传参null
 * isFailedRetry 判断该执行日志是否为失败重试，0是1否，主要用于测试报告统计断言情况
 * suiteLogDetailNo suiteLogNo仅记录重跑和真正运行的，suiteLogDetailNo会包括用例所依赖的case
 * globalHeaders 全局headers
 * globalParams 全局params
 * globalData 全局data
 * source 来源（0用例调试 1依赖调试 2运行整个测试套件 3测试套件单个用例调试 4依赖解析 5综合用例-前置用例）
 * casePreNo 前置用例的参数缓存用的key，为了防止异步执行用例时，tempPostProcessor key被覆盖， 仅前置用例执行时，需要该参数
 * skipPreCase 是否跳过执行前置用例，当用例作为前置用例执行时，应该跳过前置用例的前置用例，仅执行自身
 */
@SuppressWarnings("rawtypes")
public class ExecuteInterfaceCaseParam {
    private Integer interfaceCaseId;
    private String executor;
    private String suiteLogNo;
    private String chainNo;
    private Integer suiteId;
    private Byte isFailedRetry;
    private String suiteLogDetailNo;
    private HashMap globalHeaders;
    private HashMap globalParams;
    private HashMap globalData;
    private Byte source;
    private String casePreNo;
    private Boolean skipPreCase;

    public ExecuteInterfaceCaseParam(Integer interfaceCaseId, String executor, String suiteLogNo, String chainNo,
                                     Integer suiteId, Byte isFailedRetry, String suiteLogDetailNo,
                                     HashMap globalHeaders, HashMap globalParams, HashMap globalData, Byte source,
                                     String casePreNo, Boolean skipPreCase) {
        this.interfaceCaseId = interfaceCaseId;
        this.executor = executor;
        this.suiteLogNo = suiteLogNo;
        this.chainNo = chainNo;
        this.suiteId = suiteId;
        this.isFailedRetry = isFailedRetry;
        this.suiteLogDetailNo = suiteLogDetailNo;
        this.globalHeaders = globalHeaders;
        this.globalParams = globalParams;
        this.globalData = globalData;
        this.source = source;
        this.casePreNo = casePreNo;
        this.skipPreCase = skipPreCase;
    }

    public Boolean getSkipPreCase() {
        return skipPreCase;
    }

    public void setSkipPreCase(Boolean skipPreCase) {
        this.skipPreCase = skipPreCase;
    }

    public String getCasePreNo() {
        return casePreNo;
    }

    public void setCasePreNo(String casePreNo) {
        this.casePreNo = casePreNo;
    }

    public ExecuteInterfaceCaseParam() {

    }

    public Byte getSource() {
        return source;
    }

    public void setSource(Byte source) {
        this.source = source;
    }

    public HashMap getGlobalHeaders() {
        return globalHeaders;
    }

    public void setGlobalHeaders(HashMap globalHeaders) {
        this.globalHeaders = globalHeaders;
    }

    public HashMap getGlobalParams() {
        return globalParams;
    }

    public void setGlobalParams(HashMap globalParams) {
        this.globalParams = globalParams;
    }

    public HashMap getGlobalData() {
        return globalData;
    }

    public void setGlobalData(HashMap globalData) {
        this.globalData = globalData;
    }

    public Integer getInterfaceCaseId() {
        return interfaceCaseId;
    }

    public void setInterfaceCaseId(Integer interfaceCaseId) {
        this.interfaceCaseId = interfaceCaseId;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getSuiteLogNo() {
        return suiteLogNo;
    }

    public void setSuiteLogNo(String suiteLogNo) {
        this.suiteLogNo = suiteLogNo;
    }

    public String getChainNo() {
        return chainNo;
    }

    public void setChainNo(String chainNo) {
        this.chainNo = chainNo;
    }

    public Integer getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(Integer suiteId) {
        this.suiteId = suiteId;
    }

    public Byte getIsFailedRetry() {
        return isFailedRetry;
    }

    public void setIsFailedRetry(Byte isFailedRetry) {
        this.isFailedRetry = isFailedRetry;
    }

    public String getSuiteLogDetailNo() {
        return suiteLogDetailNo;
    }

    public void setSuiteLogDetailNo(String suiteLogDetailNo) {
        this.suiteLogDetailNo = suiteLogDetailNo;
    }
}
