package org.alex.platform.service.impl;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.mapper.PermissionMapper;
import org.alex.platform.pojo.PermissionDO;
import org.alex.platform.pojo.PermissionDTO;
import org.alex.platform.pojo.PermissionVO;
import org.alex.platform.service.PermissionService;
import org.alex.platform.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {
    private static final Logger LOG = LoggerFactory.getLogger(PermissionServiceImpl.class);
    @Autowired
    PermissionMapper permissionMapper;
    @Autowired
    RoleService roleService;

    /**
     * 动态查询节点
     * @param permissionDTO 入参
     * @return 节点集合
     */
    @Override
    public List<PermissionVO> findPermission(PermissionDTO permissionDTO) {
        return permissionMapper.selectPermission(permissionDTO);
    }

    /**
     * 检查permissionCode的唯一性
     * @param id 节点编号
     * @param permissionCode 节点code
     * @return 节点集合 为空时代表permissionCode 未被使用
     */
    @Override
    public List<PermissionVO> checkPermissionCodeRepeat(Integer id, String permissionCode) {
        return permissionMapper.checkPermissionCodeRepeat(id, permissionCode);
    }

    /**
     * 查看节点信息
     * @param id 节点id
     * @return 节点信息
     */
    @Override
    public PermissionVO findPermissionById(Integer id) {
        return permissionMapper.selectPermissionById(id);
    }

    /**
     * 递归查找父节点下的所有子节点
     * @param parentId 父节点
     * @return 节点列表
     */
    @Override
    public List<PermissionVO> findPermissionByParentId(Integer parentId) {
        return permissionMapper.selectPermissionByParentId(parentId);
    }

    /**
     * 新增节点
     * @param permissionDO 节点对象
     */
    @Override
    public void savePermission(PermissionDO permissionDO) throws BusinessException {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setPermissionCode(permissionDO.getPermissionCode());
        if (!findPermission(permissionDTO).isEmpty()) {
            throw new BusinessException("权限代码重复");
        }
        permissionMapper.insertPermission(permissionDO);
        LOG.info("新增权限成功");
    }

    /**
     * 修改节点
     * @param permissionDO 节点对象
     */
    @Override
    public void modifyPermission(PermissionDO permissionDO) throws BusinessException {
        Integer id = permissionDO.getId();
        String permissionCode = permissionDO.getPermissionCode();
        if (!checkPermissionCodeRepeat(id, permissionCode).isEmpty()) {
            throw new BusinessException("权限代码重复");
        }
        permissionMapper.updatePermission(permissionDO);
        LOG.info("修改限成功");
    }

    /**
     * 删除节点
     * @param id 删除节点
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removePermissionById(Integer id) throws BusinessException {
        PermissionDTO permissionDTO = new PermissionDTO();
        permissionDTO.setParentId(id);
        if (!findPermission(permissionDTO).isEmpty()) {
            throw new BusinessException("请先删除相关子节点");
        }
        permissionMapper.deletePermissionById(id);
        // 同时删除角色权限中间表
        roleService.removePermissionRoleRef(id);
        LOG.info("删除权限成功");
    }
}
