package org.alex.platform.pojo;

import java.io.Serializable;

public class InterfaceCaseListVO implements Serializable {
    private Integer caseId;
    private Integer projectId;
    private String projectName;
    private Integer moduleId;
    private String moduleName;
    private String desc;
    private String url;
    private Byte level;
    private String creater;

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Byte getLevel() {
        return level;
    }

    public void setLevel(Byte level) {
        this.level = level;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    @Override
    public String toString() {
        return "InterfaceCaseListVO{" +
                "caseId=" + caseId +
                ", projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", moduleId=" + moduleId +
                ", moduleName='" + moduleName + '\'' +
                ", desc='" + desc + '\'' +
                ", url='" + url + '\'' +
                ", level=" + level +
                ", creater='" + creater + '\'' +
                '}';
    }
}
