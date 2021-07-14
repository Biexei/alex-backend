package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

public class StabilityCaseDO implements Serializable {
    private Integer stabilityTestId;
    @NotNull(message = "请输入用例描述")
    @NotEmpty(message = "请输入用例描述")
    @Size(max = 30, message = "请输入长度小于30的用例描述")
    private String desc;
    @NotNull(message = "请选择协议")
    private Byte protocol;
    @NotNull(message = "请选择用例编号")
    private Integer caseId;
    @NotNull(message = "请选择执行方式")
    private Byte executeType;
    @Min(value = 60, message = "请重新输入执行间隔")
    @Max(value = 8640, message = "请重新输入执行间隔")
    private Integer step;
    private Integer executeTimes;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date executeEndTime;
    @NotNull(message = "请选择出错时处理方案")
    private Byte onErrorStop;
    @NotNull(message = "请选择失败时处理方案")
    private Byte onFailedStop;
    @Size(max = 255, message = "请输入长度小于255的邮件地址")
    private String emailAddress;
    @Max(value = 2, message = "日志记录内容参数错误")
    @Min(value = 0, message = "日志记录内容参数错误")
    private Byte logRecordContent;
    private Integer creatorId;
    private String creatorName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @NotNull(message = "请选择运行环境")
    @Max(value = 4, message = "运行环境参数错误")
    @Min(value = 0, message = "运行环境参数错误")
    private Byte runEnv;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastExecuteTime;

    @Override
    public String toString() {
        return "StabilityCaseDO{" +
                "stabilityTestId=" + stabilityTestId +
                ", desc='" + desc + '\'' +
                ", protocol=" + protocol +
                ", caseId=" + caseId +
                ", executeType=" + executeType +
                ", step=" + step +
                ", executeTimes=" + executeTimes +
                ", executeEndTime=" + executeEndTime +
                ", onErrorStop=" + onErrorStop +
                ", onFailedStop=" + onFailedStop +
                ", emailAddress='" + emailAddress + '\'' +
                ", logRecordContent=" + logRecordContent +
                ", creatorId=" + creatorId +
                ", creatorName='" + creatorName + '\'' +
                ", createdTime=" + createdTime +
                ", updateTime=" + updateTime +
                ", runEnv=" + runEnv +
                ", lastExecuteTime=" + lastExecuteTime +
                '}';
    }

    public Date getLastExecuteTime() {
        return lastExecuteTime;
    }

    public void setLastExecuteTime(Date lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }

    public Byte getRunEnv() {
        return runEnv;
    }

    public void setRunEnv(Byte runEnv) {
        this.runEnv = runEnv;
    }

    public Integer getStabilityTestId() {
        return stabilityTestId;
    }

    public void setStabilityTestId(Integer stabilityTestId) {
        this.stabilityTestId = stabilityTestId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Byte getProtocol() {
        return protocol;
    }

    public void setProtocol(Byte protocol) {
        this.protocol = protocol;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Byte getExecuteType() {
        return executeType;
    }

    public void setExecuteType(Byte executeType) {
        this.executeType = executeType;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Integer getExecuteTimes() {
        return executeTimes;
    }

    public void setExecuteTimes(Integer executeTimes) {
        this.executeTimes = executeTimes;
    }

    public Date getExecuteEndTime() {
        return executeEndTime;
    }

    public void setExecuteEndTime(Date executeEndTime) {
        this.executeEndTime = executeEndTime;
    }

    public Byte getOnErrorStop() {
        return onErrorStop;
    }

    public void setOnErrorStop(Byte onErrorStop) {
        this.onErrorStop = onErrorStop;
    }

    public Byte getOnFailedStop() {
        return onFailedStop;
    }

    public void setOnFailedStop(Byte onFailedStop) {
        this.onFailedStop = onFailedStop;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Byte getLogRecordContent() {
        return logRecordContent;
    }

    public void setLogRecordContent(Byte logRecordContent) {
        this.logRecordContent = logRecordContent;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
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
}
