package org.alex.platform.mapper;

import com.alibaba.fastjson.JSONArray;
import org.alex.platform.pojo.RoleDO;
import org.alex.platform.pojo.RoleDTO;
import org.alex.platform.pojo.RoleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public interface RoleMapper {
    RoleVO selectRoleById(Integer roleId);
    List<RoleVO> selectRole(RoleDTO roleDTO);
    void insertRole(RoleDO roleDO);
    void updateRole(RoleDO roleDO);
    void deleteRoleById(Integer roleId);
    List<RoleVO> checkRoleNameRepeat(RoleDTO roleDTO);

    JSONArray selectPermissionIdArrayByRoleId(Integer roleId);
    JSONArray selectPermissionCodeArrayByRoleId(Integer roleId);
    void insertRolePermission(@Param("roleId") Integer roleId, @Param("permissionId") Integer permissionId, @Param("updateTime") Date updateTime);
    void deleteRolePermission(@Param("roleId") Integer roleId, @Param("permissionId") Integer permissionId);
    List<HashMap> selectRolePermission(@Param("roleId") Integer roleId, @Param("permissionId") Integer permissionId);
    void deletePermissionRoleRef(Integer permissionId);
}
