package org.alex.platform.mapper;

import org.alex.platform.pojo.RoleDO;
import org.alex.platform.pojo.RoleDTO;
import org.alex.platform.pojo.RoleVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {
    RoleVO selectRoleById(Integer roleId);
    List<RoleVO> selectRole(RoleDTO roleDTO);
    void insertRole(RoleDO roleDO);
    void updateRole(RoleDO roleDO);
    void deleteRoleById(Integer roleId);
    List<RoleVO> checkRoleNameRepeat(RoleDTO roleDTO);
}
