package org.alex.platform.pojo;

import java.io.Serializable;

public class InterfaceSuiteProcessorDTO implements Serializable {
    private Integer id;
    private Integer suiteId;
    private Byte processorType;
    private Byte type;
    private String desc;
    private String value;

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
}
