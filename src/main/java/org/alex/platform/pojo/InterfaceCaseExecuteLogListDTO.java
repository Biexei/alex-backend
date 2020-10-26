package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class InterfaceCaseExecuteLogListDTO implements Serializable {
    private Integer projectId;
    private String projectName;
    private Integer moduleId;
    private String moduleName;
    private Integer caseId;
    private String caseDesc;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdStartTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdEndTime;
    private Byte status;
    private String suiteLogNo;
    private String suiteLogDetailNo;
    private Byte isFailedRetry;

    public String getSuiteLogDetailNo() {
        return suiteLogDetailNo;
    }

    public void setSuiteLogDetailNo(String suiteLogDetailNo) {
        this.suiteLogDetailNo = suiteLogDetailNo;
    }

    public Byte getIsFailedRetry() {
        return isFailedRetry;
    }

    public void setIsFailedRetry(Byte isFailedRetry) {
        this.isFailedRetry = isFailedRetry;
    }

    public String getSuiteLogNo() {
        return suiteLogNo;
    }

    public void setSuiteLogNo(String suiteLogNo) {
        this.suiteLogNo = suiteLogNo;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String caseDesc) {
        this.caseDesc = caseDesc;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
