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
    @NotNull(message = "菜单代码不能为空")
    @NotEmpty(message = "菜单代码不能为空")
    @Size(max = 255, message = "菜单代码长度必须小于等于255")
    private String menuCode;
    @NotNull(message = "菜单名称不能为空")
    @NotEmpty(message = "菜单名称不能为空")
    @Size(max = 255, message = "菜单名称长度必须小于等于255")
    private String menuName;
    @NotNull(message = "状态不能为空")
    @Max(value = 1, message = "状态必须为0~1")
    @Min(value = 0, message = "状态必须为0~1")
    private Byte required;

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

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Byte getRequired() {
        return required;
    }

    public void setRequired(Byte required) {
        this.required = required;
    }
}
