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
    @Size(min = 1, max = 100, message = "URL长度必须为[1,100]")
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
