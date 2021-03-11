package org.alex.platform.service;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.PermissionDO;
import org.alex.platform.pojo.PermissionDTO;
import org.alex.platform.pojo.PermissionVO;

import java.util.List;

public interface PermissionService {
    List<PermissionVO> findPermission(PermissionDTO permissionDTO);
    List<PermissionVO> checkPermissionCodeRepeat(Integer id,String permissionCode);
    PermissionVO findPermissionById(Integer id);
    List<PermissionVO> findPermissionByParentId(Integer parentId);
    void savePermission(PermissionDO permissionDO) throws BusinessException;
    void modifyPermission(PermissionDO permissionDO) throws BusinessException;
    void removePermissionById(Integer id) throws BusinessException;
}
