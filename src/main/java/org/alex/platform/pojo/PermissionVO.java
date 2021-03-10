package org.alex.platform.pojo;

import java.io.Serializable;
import java.util.List;

public class PermissionVO implements Serializable {
    private Integer id;
    private String permissionCode;
    private String permissionName;
    private Integer parentId;
    private List<PermissionVO> nodeList;

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

    public List<PermissionVO> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<PermissionVO> nodeList) {
        this.nodeList = nodeList;
    }
}
