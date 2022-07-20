package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

public class DbDO implements Serializable {
    private Integer id;
    @NotEmpty(message = "名称不能为空")
    @Size(min = 1, max = 20, message = "名称长度必须为[1,20]")
    private String name;
    @NotNull(message = "类型不能为空")
    private Byte type;
    private String desc;
    @NotEmpty(message = "URL不能为空")
    @Size(min = 1, max = 200, message = "URL长度必须为[1,200]")
    private String url;
    @NotEmpty(message = "帐号不能为空")
    @Size(min = 1, max = 100, message = "帐号长度必须为[1,100]")
    private String username;
    @NotEmpty(message = "密码不能为空")
    @Size(min = 1, max = 100, message = "密码长度必须为[1,100]")
    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @NotNull(message = "状态不能为空")
    private Byte status;

    @NotEmpty(message = "开发环境URL不能为空")
    @Size(min = 1, max = 200, message = "开发环境URL长度必须为[1,200]")
    private String devUrl;
    @NotEmpty(message = "开发环境帐号不能为空")
    @Size(min = 1, max = 100, message = "开发环境帐号长度必须为[1,100]")
    private String devUsername;
    @NotEmpty(message = "开发环境密码不能为空")
    @Size(min = 1, max = 100, message = "开发环境密码长度必须为[1,100]")
    private String devPassword;

    @NotEmpty(message = "测试环境URL不能为空")
    @Size(min = 1, max = 200, message = "测试环境URL长度必须为[1,200]")
    private String testUrl;
    @NotEmpty(message = "测试环境帐号不能为空")
    @Size(min = 1, max = 100, message = "测试环境帐号长度必须为[1,100]")
    private String testUsername;
    @NotEmpty(message = "测试环境密码不能为空")
    @Size(min = 1, max = 100, message = "测试环境密码长度必须为[1,100]")
    private String testPassword;

    @NotEmpty(message = "预发环境URL不能为空")
    @Size(min = 1, max = 200, message = "预发环境URL长度必须为[1,200]")
    private String stgUrl;
    @NotEmpty(message = "预发环境帐号不能为空")
    @Size(min = 1, max = 100, message = "预发环境帐号长度必须为[1,100]")
    private String stgUsername;
    @NotEmpty(message = "预发环境密码不能为空")
    @Size(min = 1, max = 100, message = "预发环境密码长度必须为[1,100]")
    private String stgPassword;

    @NotEmpty(message = "线上环境URL不能为空")
    @Size(min = 1, max = 200, message = "线上环境URL长度必须为[1,200]")
    private String prodUrl;
    @NotEmpty(message = "线上环境帐号不能为空")
    @Size(min = 1, max = 100, message = "线上环境帐号长度必须为[1,100]")
    private String prodUsername;
    @NotEmpty(message = "线上环境密码不能为空")
    @Size(min = 1, max = 100, message = "线上环境密码长度必须为[1,100]")
    private String prodPassword;

    public String getDevUrl() {
        return devUrl;
    }

    public void setDevUrl(String devUrl) {
        this.devUrl = devUrl;
    }

    public String getDevUsername() {
        return devUsername;
    }

    public void setDevUsername(String devUsername) {
        this.devUsername = devUsername;
    }

    public String getDevPassword() {
        return devPassword;
    }

    public void setDevPassword(String devPassword) {
        this.devPassword = devPassword;
    }

    public String getTestUrl() {
        return testUrl;
    }

    public void setTestUrl(String testUrl) {
        this.testUrl = testUrl;
    }

    public String getTestUsername() {
        return testUsername;
    }

    public void setTestUsername(String testUsername) {
        this.testUsername = testUsername;
    }

    public String getTestPassword() {
        return testPassword;
    }

    public void setTestPassword(String testPassword) {
        this.testPassword = testPassword;
    }

    public String getStgUrl() {
        return stgUrl;
    }

    public void setStgUrl(String stgUrl) {
        this.stgUrl = stgUrl;
    }

    public String getStgUsername() {
        return stgUsername;
    }

    public void setStgUsername(String stgUsername) {
        this.stgUsername = stgUsername;
    }

    public String getStgPassword() {
        return stgPassword;
    }

    public void setStgPassword(String stgPassword) {
        this.stgPassword = stgPassword;
    }

    public String getProdUrl() {
        return prodUrl;
    }

    public void setProdUrl(String prodUrl) {
        this.prodUrl = prodUrl;
    }

    public String getProdUsername() {
        return prodUsername;
    }

    public void setProdUsername(String prodUsername) {
        this.prodUsername = prodUsername;
    }

    public String getProdPassword() {
        return prodPassword;
    }

    public void setProdPassword(String prodPassword) {
        this.prodPassword = prodPassword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
