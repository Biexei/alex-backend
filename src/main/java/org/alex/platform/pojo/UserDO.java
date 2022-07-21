package org.alex.platform.pojo;


import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

public class UserDO implements Serializable {
    private Integer userId;
    @Size(min = 1, max = 20, message = "用户名长度必须为1~20")
    @NotNull(message = "用户名不能为空")
    private String username;
    private String password;
    @Pattern(regexp = "\\d{0,10}$", message = "工号必须为数字")
    private String jobNumber;
    @DecimalMax(value = "1", message = "性别必须为0|1")
    private Byte sex;
    @DecimalMax(value = "1", message = "状态必须为0|1")
    private Byte isEnable;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @Size(max = 20, message = "昵称长度必须小于20")
    @NotNull(message = "昵称不能为空")
    private String realName;
    private Integer roleId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public Byte getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Byte isEnable) {
        this.isEnable = isEnable;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String toString() {
        return "UserDO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", jobNumber='" + jobNumber + '\'' +
                ", sex=" + sex +
                ", isEnable=" + isEnable +
                ", createdTime=" + createdTime +
                ", updateTime=" + updateTime +
                ", realName='" + realName + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}
