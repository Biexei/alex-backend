package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class InterfaceSuiteLogDO {
    private Integer id;
    private Integer suiteId;
    private String suiteLogNo;
    private Long runTime;
    private Integer totalCase;
    private Integer totalRunCase;
    private Integer totalSkip;
    private Integer totalSuccess;
    private Integer totalFailed;
    private Integer totalError;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Byte executeType;
    private Byte runDev;

    public Byte getExecuteType() {
        return executeType;
    }

    public void setExecuteType(Byte executeType) {
        this.executeType = executeType;
    }

    public Byte getRunDev() {
        return runDev;
    }

    public void setRunDev(Byte runDev) {
        this.runDev = runDev;
    }

    public Integer getTotalCase() {
        return totalCase;
    }

    public void setTotalCase(Integer totalCase) {
        this.totalCase = totalCase;
    }

    public Integer getTotalSkip() {
        return totalSkip;
    }

    public void setTotalSkip(Integer totalSkip) {
        this.totalSkip = totalSkip;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(Integer suiteId) {
        this.suiteId = suiteId;
    }

    public String getSuiteLogNo() {
        return suiteLogNo;
    }

    public void setSuiteLogNo(String suiteLogNo) {
        this.suiteLogNo = suiteLogNo;
    }

    public Long getRunTime() {
        return runTime;
    }

    public void setRunTime(Long runTime) {
        this.runTime = runTime;
    }

    public Integer getTotalRunCase() {
        return totalRunCase;
    }

    public void setTotalRunCase(Integer totalRunCase) {
        this.totalRunCase = totalRunCase;
    }

    public Integer getTotalSuccess() {
        return totalSuccess;
    }

    public void setTotalSuccess(Integer totalSuccess) {
        this.totalSuccess = totalSuccess;
    }

    public Integer getTotalFailed() {
        return totalFailed;
    }

    public void setTotalFailed(Integer totalFailed) {
        this.totalFailed = totalFailed;
    }

    public Integer getTotalError() {
        return totalError;
    }

    public void setTotalError(Integer totalError) {
        this.totalError = totalError;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
