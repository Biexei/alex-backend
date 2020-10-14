package org.alex.platform.pojo;

import org.hibernate.validator.constraints.URL;
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
    @NotNull(message = "项目域名不能为空")
    @NotEmpty(message = "项目域名不能为空")
    @URL(message = "项目域名格式错误")
    @Size(max = 30, message = "项目域名长度必须小于等于30")
    private String domain;
    @NotNull(message = "开发环境域名不能为空")
    @NotEmpty(message = "开发环境域名不能为空")
    @URL(message = "开发环境域名格式错误")
    @Size(max = 30, message = "开发环境域名长度必须小于等于30")
    private String devDomain;
    @NotNull(message = "测试环境域名不能为空")
    @NotEmpty(message = "测试环境域名不能为空")
    @URL(message = "测试环境域名格式错误")
    @Size(max = 30, message = "测试环境域名长度必须小于等于30")
    private String testDomain;
    @NotNull(message = "预上线环境域名不能为空")
    @NotEmpty(message = "预上线环境域名不能为空")
    @URL(message = "预上线环境域名格式错误")
    @Size(max = 30, message = "预上线环境域名长度必须小于等于30")
    private String stgDomain;
    @NotNull(message = "正式环境域名不能为空")
    @NotEmpty(message = "正式环境域名不能为空")
    @URL(message = "正式环境域名格式错误")
    @Size(max = 30, message = "正式环境域名长度必须小于等于30")
    private String prodDomain;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

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

    @Override
    public String toString() {
        return "ProjectDO{" +
                "projectId=" + projectId +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", domain='" + domain + '\'' +
                ", createdTime=" + createdTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
