package org.alex.platform.mapper;

import org.alex.platform.pojo.PermissionDO;
import org.alex.platform.pojo.PermissionDTO;
import org.alex.platform.pojo.PermissionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper {
    PermissionVO selectPermissionById(Integer id);
    List<PermissionVO> selectPermissionByParentId(Integer parentId);
    List<PermissionVO> selectPermission(PermissionDTO permissionDTO);
    List<PermissionVO> checkPermissionCodeRepeat(@Param("id") Integer id, @Param("permissionCode") String permissionCode);
    void insertPermission(PermissionDO permissionDO);
    void updatePermission(PermissionDO permissionDO);
    void deletePermissionById(Integer id);
}
