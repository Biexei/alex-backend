package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class StabilityCaseLogDO implements Serializable {
    private Integer stabilityTestId;
    private String stabilityTestDesc;
    private Integer stabilityTestLogId;
    private String stabilityTestLogNo;
    private String logPath;
    private Byte status;
    private Integer executeId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    private Byte runEnv;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private Integer requestCount;
    private Integer successCount;
    private Integer warningCount;
    private Integer failedCount;
    private Integer errorCount;
    private String responseTimeQueue;

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

    public Integer getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Integer requestCount) {
        this.requestCount = requestCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getWarningCount() {
        return warningCount;
    }

    public void setWarningCount(Integer warningCount) {
        this.warningCount = warningCount;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public String getResponseTimeQueue() {
        return responseTimeQueue;
    }

    public void setResponseTimeQueue(String responseTimeQueue) {
        this.responseTimeQueue = responseTimeQueue;
    }

    public Integer getStabilityTestId() {
        return stabilityTestId;
    }

    public void setStabilityTestId(Integer stabilityTestId) {
        this.stabilityTestId = stabilityTestId;
    }

    public String getStabilityTestDesc() {
        return stabilityTestDesc;
    }

    public void setStabilityTestDesc(String stabilityTestDesc) {
        this.stabilityTestDesc = stabilityTestDesc;
    }

    public Integer getStabilityTestLogId() {
        return stabilityTestLogId;
    }

    public void setStabilityTestLogId(Integer stabilityTestLogId) {
        this.stabilityTestLogId = stabilityTestLogId;
    }

    public String getStabilityTestLogNo() {
        return stabilityTestLogNo;
    }

    public void setStabilityTestLogNo(String stabilityTestLogNo) {
        this.stabilityTestLogNo = stabilityTestLogNo;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getExecuteId() {
        return executeId;
    }

    public void setExecuteId(Integer executeId) {
        this.executeId = executeId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Byte getRunEnv() {
        return runEnv;
    }

    public void setRunEnv(Byte runEnv) {
        this.runEnv = runEnv;
    }
}
