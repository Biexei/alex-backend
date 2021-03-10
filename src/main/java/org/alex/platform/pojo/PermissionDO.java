package org.alex.platform.pojo;

import javax.validation.constraints.*;
import java.io.Serializable;

public class PermissionDO implements Serializable {
    private Integer id;
    @NotNull(message = "权限代码不能为空")
    @NotEmpty(message = "权限代码不能为空")
    @Size(max = 255, message = "权限代码长度必须小于等于255")
    private String permissionCode;
    @NotNull(message = "权限名称不能为空")
    @NotEmpty(message = "权限名称不能为空")
    @Size(max = 255, message = "权限名称长度必须小于等于255")
    private String permissionName;
    private Integer parentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
