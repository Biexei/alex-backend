package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.Date;

public class InterfaceCaseExecuteLogListDTO implements Serializable {
    private Integer projectId;
    private String projectName;
    private Integer moduleId;
    private Integer moduleName;
    private Integer caseId;
    private String caseDesc;
    private Date createdStartTime;
    private Date createdEndTime;
    private Byte status;

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

    public Integer getModuleName() {
        return moduleName;
    }

    public void setModuleName(Integer moduleName) {
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
