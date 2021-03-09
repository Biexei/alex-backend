package org.alex.platform.mapper;

import org.alex.platform.pojo.PermissionDO;
import org.alex.platform.pojo.PermissionDTO;
import org.alex.platform.pojo.PermissionVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper {
    PermissionVO selectPermissionById(Integer roleId);
    List<PermissionVO> selectPermission(PermissionDTO permissionDTO);
    void insertPermission(PermissionDO permissionDO);
    void updatePermission(PermissionDO permissionDO);
    void removePermissionById(Integer permissionId);
}
