package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class InterfaceSuiteProcessorDO implements Serializable {
    private Integer id;
    private Integer suiteId;
    private Byte processorType;
    private Byte type;
    private String desc;
    private String value;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

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

    public Byte getProcessorType() {
        return processorType;
    }

    public void setProcessorType(Byte processorType) {
        this.processorType = processorType;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
