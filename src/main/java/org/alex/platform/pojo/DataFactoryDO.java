package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

public class DataFactoryDO implements Serializable {
    private Integer id;
    @Size(message = "名称长度不能超过100")
    private String name;
    @NotNull(message = "类型不能为空")
    @Min(value = 0, message = "类型只能为0~2")
    @Max(value = 2, message = "类型只能为0~2")
    private Byte type;
    @NotNull(message = "执行次数不能为空")
    @Min(value = 1, message = "执行次数只能为1~1000")
    @Max(value = 1000, message = "执行次数只能为1~1000")
    private Integer times;
    @NotNull(message = "失败重试不能为空")
    @Min(value = 0, message = "失败重试只能为0~1")
    @Max(value = 1, message = "失败重试只能为0~1")
    private Byte failedStop;
    @Min(value = 0, message = "SQL运行环境只能为0~4")
    @Max(value = 4, message = "SQL运行环境只能为0~4")
    private Byte sqlRunDev;
    private Integer sqlDbId;
    private String sqlStr;
    private Integer interfaceSuiteId;
    private Integer uiSuiteId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @NotNull(message = "执行方式不能为空")
    @Min(value = 0, message = "执行方式只能为0~1")
    @Max(value = 1, message = "执行方式只能为0~1")
    private Byte executeType;

    public Byte getExecuteType() {
        return executeType;
    }

    public void setExecuteType(Byte executeType) {
        this.executeType = executeType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Byte getFailedStop() {
        return failedStop;
    }

    public void setFailedStop(Byte failedStop) {
        this.failedStop = failedStop;
    }

    public Byte getSqlRunDev() {
        return sqlRunDev;
    }

    public void setSqlRunDev(Byte sqlRunDev) {
        this.sqlRunDev = sqlRunDev;
    }

    public Integer getSqlDbId() {
        return sqlDbId;
    }

    public void setSqlDbId(Integer sqlDbId) {
        this.sqlDbId = sqlDbId;
    }

    public String getSqlStr() {
        return sqlStr;
    }

    public void setSqlStr(String sqlStr) {
        this.sqlStr = sqlStr;
    }

    public Integer getInterfaceSuiteId() {
        return interfaceSuiteId;
    }

    public void setInterfaceSuiteId(Integer interfaceSuiteId) {
        this.interfaceSuiteId = interfaceSuiteId;
    }

    public Integer getUiSuiteId() {
        return uiSuiteId;
    }

    public void setUiSuiteId(Integer uiSuiteId) {
        this.uiSuiteId = uiSuiteId;
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
