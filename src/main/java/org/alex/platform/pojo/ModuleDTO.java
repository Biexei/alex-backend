package org.alex.platform.pojo;

import java.io.Serializable;

public class ModuleDTO implements Serializable {
    private Integer moduleId;
    private Integer projectId;
    private String moduleName;
    private String projectName;

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "ModuleDto{" +
                "moduleId=" + moduleId +
                ", projectId=" + projectId +
                ", moduleName='" + moduleName + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}
