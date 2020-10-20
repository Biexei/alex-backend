package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

public class InterfaceCaseSuiteDO implements Serializable {
    private Integer suiteId;
    @NotEmpty(message = "名称不允许为空")
    private String suiteName;
    private String desc;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String creator;
    @NotNull(message = "执行方式不允许为空")
    private Byte executeType;
    @NotNull(message = "运行环境不允许为空")
    @Max(value = 4, message = "运行环境设置错误")
    @Min(value = 0, message = "运行环境设置错误")
    private Byte runDev;
    @NotNull(message = "失败重试不能为空")
    @Max(value = 1, message = "失败重试设置错误")
    @Min(value = 0, message = "失败重试设置错误")
    private Byte isRetry;

    public Byte getIsRetry() {
        return isRetry;
    }

    public void setIsRetry(Byte isRetry) {
        this.isRetry = isRetry;
    }

    public Byte getRunDev() {
        return runDev;
    }

    public void setRunDev(Byte runDev) {
        this.runDev = runDev;
    }

    public Integer getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(Integer suiteId) {
        this.suiteId = suiteId;
    }

    public Byte getExecuteType() {
        return executeType;
    }

    public void setExecuteType(Byte executeType) {
        this.executeType = executeType;
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
}
