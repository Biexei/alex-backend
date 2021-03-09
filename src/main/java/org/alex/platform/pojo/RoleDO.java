package org.alex.platform.pojo;

import javax.validation.constraints.*;
import java.io.Serializable;

public class RoleDO implements Serializable {
    private Integer roleId;
    @NotNull(message = "角色名称不能为空")
    @NotEmpty(message = "角色名称不能为空")
    @Size(max = 20, message = "角色名称长度必须小于等于20")
    private String roleNme;
    @NotNull(message = "状态不能为空")
    @Max(value = 1, message = "状态必须为0~1")
    @Min(value = 0, message = "状态必须为0~1")
    private Byte status;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleNme() {
        return roleNme;
    }

    public void setRoleNme(String roleNme) {
        this.roleNme = roleNme;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
