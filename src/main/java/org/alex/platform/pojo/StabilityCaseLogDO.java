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
    private Integer executerId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

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

    public Integer getExecuterId() {
        return executerId;
    }

    public void setExecuterId(Integer executerId) {
        this.executerId = executerId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
