package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class InterfaceSuiteSummaryDTO {
    private String suiteLogNo;
    private Integer projectId;
    private Integer moduleId;

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

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }
}
