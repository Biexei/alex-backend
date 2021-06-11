package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

public class ProjectDO implements Serializable {
    private Integer projectId;
    @NotNull(message = "项目名称不能为空")
    @Size(min = 1, max = 10, message = "项目名称长度必须为1~10")
    private String name;
    @Size(max = 100, message = "项目描述长度必须小于等于100")
    private String desc;

    @NotNull(message = "项目host不能为空")
    @NotEmpty(message = "项目host不能为空")
    @Size(max = 30, message = "项目host长度必须小于等于30")
    private String domain;
    @NotNull(message = "项目protocol不能为空")
    @NotEmpty(message = "项目protocol不能为空")
    @Size(max = 30, message = "项目protocol长度必须小于等于30")
    private String protocol;
    private Integer port;

    @NotNull(message = "开发环境host不能为空")
    @NotEmpty(message = "开发环境host不能为空")
    @Size(max = 30, message = "开发环境host长度必须小于等于30")
    private String devDomain;
    @NotNull(message = "开发环境protocol不能为空")
    @NotEmpty(message = "开发环境protocol不能为空")
    @Size(max = 30, message = "开发环境protocol长度必须小于等于30")
    private String devProtocol;
    private Integer devPort;

    @NotNull(message = "测试环境host不能为空")
    @NotEmpty(message = "测试环境host不能为空")
    @Size(max = 30, message = "测试环境host长度必须小于等于30")
    private String testDomain;
    @NotNull(message = "测试环境protocol不能为空")
    @NotEmpty(message = "测试环境protocol不能为空")
    @Size(max = 30, message = "测试环境protocol长度必须小于等于30")
    private String testProtocol;
    private Integer testPort;

    @NotNull(message = "预上线环境host不能为空")
    @NotEmpty(message = "预上线环境host不能为空")
    @Size(max = 30, message = "预上线环境host长度必须小于等于30")
    private String stgDomain;
    @NotNull(message = "预上线环境protocol不能为空")
    @NotEmpty(message = "预上线环境protocol不能为空")
    @Size(max = 30, message = "预上线环境protocol长度必须小于等于30")
    private String stgProtocol;
    private Integer stgPort;


    @NotNull(message = "正式环境host不能为空")
    @NotEmpty(message = "正式环境host不能为空")
    @Size(max = 30, message = "正式环境host长度必须小于等于30")
    private String prodDomain;
    @NotNull(message = "正式环境protocol不能为空")
    @NotEmpty(message = "正式环境protocol不能为空")
    @Size(max = 30, message = "正式环境protocol长度必须小于等于30")
    private String prodProtocol;
    private Integer prodPort;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

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

    public String getDevDomain() {
        return devDomain;
    }

    public void setDevDomain(String devDomain) {
        this.devDomain = devDomain;
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

    public String getTestDomain() {
        return testDomain;
    }

    public void setTestDomain(String testDomain) {
        this.testDomain = testDomain;
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

    public String getStgDomain() {
        return stgDomain;
    }

    public void setStgDomain(String stgDomain) {
        this.stgDomain = stgDomain;
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

    public String getProdDomain() {
        return prodDomain;
    }

    public void setProdDomain(String prodDomain) {
        this.prodDomain = prodDomain;
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

    @Override
    public String toString() {
        return "ProjectDO{" +
                "projectId=" + projectId +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", domain='" + domain + '\'' +
                ", protocol='" + protocol + '\'' +
                ", port=" + port +
                ", devDomain='" + devDomain + '\'' +
                ", devProtocol='" + devProtocol + '\'' +
                ", devPort=" + devPort +
                ", testDomain='" + testDomain + '\'' +
                ", testProtocol='" + testProtocol + '\'' +
                ", testPort=" + testPort +
                ", stgDomain='" + stgDomain + '\'' +
                ", stgProtocol='" + stgProtocol + '\'' +
                ", stgPort=" + stgPort +
                ", prodDomain='" + prodDomain + '\'' +
                ", prodProtocol='" + prodProtocol + '\'' +
                ", prodPort=" + prodPort +
                ", createdTime=" + createdTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
