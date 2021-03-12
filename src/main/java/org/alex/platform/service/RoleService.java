package org.alex.platform.service;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.RoleDO;
import org.alex.platform.pojo.RoleDTO;
import org.alex.platform.pojo.RoleVO;

import java.util.Date;
import java.util.List;

public interface RoleService {
    RoleVO findRoleById(Integer roleId);
    PageInfo<RoleVO> findRole(RoleDTO roleDTO, Integer pageNum, Integer pageSize);
    List<RoleVO> findAllRole(RoleDTO roleDTO);
    void saveRole(RoleDO roleDO) throws BusinessException;
    void modifyRole(RoleDO roleDO) throws BusinessException;
    void removeRoleById(Integer roleId) throws BusinessException;
    List<RoleVO> checkRoleNameRepeat(RoleDTO roleDTO);

    JSONArray findPermissionIdArrayByRoleId(Integer roleId) throws ValidException;
    JSONArray findPermissionCodeArrayByRoleId(Integer roleId);
    void saveRolePermission(Integer roleId, Integer permissionId, Date updateTime) throws ValidException;
    void removeRolePermission(Integer roleId, Integer permissionId) throws ValidException;
    void removePermissionRoleRef(Integer permissionId);
}
