package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProjectVO implements Serializable {
    private Integer projectId;
    private String name;
    private String desc;
    private String domain;
    private String devDomain;
    private String testDomain;
    private String stgDomain;
    private String prodDomain;
    private Date createdTime;
    private Date updateTime;
    private List<ModuleDO> modules;

    public String getDevDomain() {
        return devDomain;
    }

    public void setDevDomain(String devDomain) {
        this.devDomain = devDomain;
    }

    public String getTestDomain() {
        return testDomain;
    }

    public void setTestDomain(String testDomain) {
        this.testDomain = testDomain;
    }

    public String getStgDomain() {
        return stgDomain;
    }

    public void setStgDomain(String stgDomain) {
        this.stgDomain = stgDomain;
    }

    public String getProdDomain() {
        return prodDomain;
    }

    public void setProdDomain(String prodDomain) {
        this.prodDomain = prodDomain;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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

    public List<ModuleDO> getModules() {
        return modules;
    }

    public void setModules(List<ModuleDO> modules) {
        this.modules = modules;
    }
}
