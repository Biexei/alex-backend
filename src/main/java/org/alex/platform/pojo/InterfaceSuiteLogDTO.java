package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class InterfaceSuiteLogDTO {
    private Integer id;
    private Integer suiteId;
    private String suiteName;
    private String suiteDesc;
    private String suiteLogNo;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdStartTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdEndTime;

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

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getSuiteDesc() {
        return suiteDesc;
    }

    public void setSuiteDesc(String suiteDesc) {
        this.suiteDesc = suiteDesc;
    }

    public String getSuiteLogNo() {
        return suiteLogNo;
    }

    public void setSuiteLogNo(String suiteLogNo) {
        this.suiteLogNo = suiteLogNo;
    }

    public Date getCreatedStartTime() {
        return createdStartTime;
    }

    public void setCreatedStartTime(Date createdStartTime) {
        this.createdStartTime = createdStartTime;
    }

    public Date getCreatedEndTime() {
        return createdEndTime;
    }

    public void setCreatedEndTime(Date createdEndTime) {
        this.createdEndTime = createdEndTime;
    }
}
