package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProjectVO implements Serializable {
    private Integer projectId;
    private String name;
    private String desc;

    private String protocol;
    private Integer port;
    private String domain;

    private String devProtocol;
    private Integer devPort;
    private String devDomain;

    private String testProtocol;
    private Integer testPort;
    private String testDomain;

    private String stgProtocol;
    private Integer stgPort;
    private String stgDomain;

    private String prodProtocol;
    private Integer prodPort;
    private String prodDomain;

    private Date createdTime;
    private Date updateTime;
    private List<ModuleDO> modules;

    public String getDevDomain() {
        return devDomain;
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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDevProtocol() {
        return devProtocol;
    }

    public void setDevProtocol(String devProtocol) {
        this.devProtocol = devProtocol;
    }

    public Integer getDevPort() {
        return devPort;
    }

    public void setDevPort(Integer devPort) {
        this.devPort = devPort;
    }

    public void setDevDomain(String devDomain) {
        this.devDomain = devDomain;
    }

    public String getTestProtocol() {
        return testProtocol;
    }

    public void setTestProtocol(String testProtocol) {
        this.testProtocol = testProtocol;
    }

    public Integer getTestPort() {
        return testPort;
    }

    public void setTestPort(Integer testPort) {
        this.testPort = testPort;
    }

    public String getTestDomain() {
        return testDomain;
    }

    public void setTestDomain(String testDomain) {
        this.testDomain = testDomain;
    }

    public String getStgProtocol() {
        return stgProtocol;
    }

    public void setStgProtocol(String stgProtocol) {
        this.stgProtocol = stgProtocol;
    }

    public Integer getStgPort() {
        return stgPort;
    }

    public void setStgPort(Integer stgPort) {
        this.stgPort = stgPort;
    }

    public String getStgDomain() {
        return stgDomain;
    }

    public void setStgDomain(String stgDomain) {
        this.stgDomain = stgDomain;
    }

    public String getProdProtocol() {
        return prodProtocol;
    }

    public void setProdProtocol(String prodProtocol) {
        this.prodProtocol = prodProtocol;
    }

    public Integer getProdPort() {
        return prodPort;
    }

    public void setProdPort(Integer prodPort) {
        this.prodPort = prodPort;
    }

    public String getProdDomain() {
        return prodDomain;
    }

    public void setProdDomain(String prodDomain) {
        this.prodDomain = prodDomain;
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
