package org.alex.platform.pojo;

public class TaskDTO {
    private String desc;
    private Integer suiteId;
    private String suiteName;
    private String emailAddress;
    private Byte status;
    private Byte suiteType;

    public Byte getSuiteType() {
        return suiteType;
    }

    public void setSuiteType(Byte suiteType) {
        this.suiteType = suiteType;
    }

    public Integer getSuiteId() {
        return suiteId;
    }

    public void setSuiteId(Integer suiteId) {
        this.suiteId = suiteId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
