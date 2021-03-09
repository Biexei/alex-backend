package org.alex.platform.pojo;

import java.io.Serializable;

public class RoleVO implements Serializable {
    private Integer roleId;
    private String roleNme;
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
