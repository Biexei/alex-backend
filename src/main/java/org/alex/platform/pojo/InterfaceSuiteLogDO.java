package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class InterfaceSuiteLogDO {
    private Integer id;
    private Integer suiteId;
    private String suiteLogNo;
    private Long runTime;
    private Integer totalRunCase;
    private Integer totalSuccess;
    private Integer totalFailed;
    private Integer totalError;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

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
