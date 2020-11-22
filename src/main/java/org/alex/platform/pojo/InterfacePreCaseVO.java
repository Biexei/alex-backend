package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class InterfacePreCaseVO {
    private Integer id;
    private Integer preCaseId;
    private String preCaseDesc;
    private Integer order;
    private Byte status;
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

    public Integer getPreCaseId() {
        return preCaseId;
    }

    public void setPreCaseId(Integer preCaseId) {
        this.preCaseId = preCaseId;
    }

    public String getPreCaseDesc() {
        return preCaseDesc;
    }

    public void setPreCaseDesc(String preCaseDesc) {
        this.preCaseDesc = preCaseDesc;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
