package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class InterfaceSuiteInfoVO implements Serializable {
    private Integer suiteId;
    private String suiteName;
    private String desc;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String creator;
    private Byte executeType;
    private Byte runDev;
    private Byte isRetry;
    private Byte progress;

    public Byte getProgress() {
        return progress;
    }

    public void setProgress(Byte progress) {
        this.progress = progress;
    }

    private List<InterfaceSuiteProcessorVO> suiteProcessors;

    public List<InterfaceSuiteProcessorVO> getSuiteProcessors() {
        return suiteProcessors;
    }

    public void setSuiteProcessors(List<InterfaceSuiteProcessorVO> suiteProcessors) {
        this.suiteProcessors = suiteProcessors;
    }

    public Integer getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(Integer suiteId) {
        this.suiteId = suiteId;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

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

    public Byte getIsRetry() {
        return isRetry;
    }

    public void setIsRetry(Byte isRetry) {
        this.isRetry = isRetry;
    }
}
