package org.alex.platform.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class UserLoginLogDTO implements Serializable {
    private Integer id;
    private Integer userId;
    private String userName;
    private String ip;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginStartTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loginEndTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLoginStartTime() {
        return loginStartTime;
    }

    public void setLoginStartTime(Date loginStartTime) {
        this.loginStartTime = loginStartTime;
    }

    public Date getLoginEndTime() {
        return loginEndTime;
    }

    public void setLoginEndTime(Date loginEndTime) {
        this.loginEndTime = loginEndTime;
    }
}
