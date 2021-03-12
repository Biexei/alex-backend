package org.alex.platform.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.mapper.RoleMapper;
import org.alex.platform.mapper.UserMapper;
import org.alex.platform.pojo.RoleDO;
import org.alex.platform.pojo.RoleDTO;
import org.alex.platform.pojo.RoleVO;
import org.alex.platform.pojo.UserDO;
import org.alex.platform.service.RoleService;
import org.alex.platform.util.ValidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private static Logger LOG = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    RoleMapper roleMapper;
    @Autowired
    UserMapper userMapper;

    /**
     * 根据id查找角色
     * @param roleId 角色id
     * @return 角色对象信息
     */
    @Override
    public RoleVO findRoleById(Integer roleId) {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * 查看角色列表分页
     * @param roleDTO 入参
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return 角色列表
     */
    @Override
    public PageInfo<RoleVO> findRole(RoleDTO roleDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(roleMapper.selectRole(roleDTO));
    }

    /**
     * 查看所有角色
     * @param roleDTO 入参
     * @return 角色列表
     */
    @Override
    public List<RoleVO> findAllRole(RoleDTO roleDTO) {
        return roleMapper.selectRole(roleDTO);
    }

    /**
     * 新增角色
     * @param roleDO 角色对象
     */
    @Override
    public void saveRole(RoleDO roleDO) throws BusinessException {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName(roleDO.getRoleName());
        if (!findAllRole(roleDTO).isEmpty()) {
            throw new BusinessException("角色名称已存在");
        }
        roleMapper.insertRole(roleDO);
        LOG.info("新增角色");
    }

    /**
     * 修改角色
     * @param roleDO 角色对象
     */
    @Override
    public void modifyRole(RoleDO roleDO) throws BusinessException {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleName(roleDO.getRoleName());
        roleDTO.setRoleId(roleDO.getRoleId());
        if (!checkRoleNameRepeat(roleDTO).isEmpty()) {
            throw new BusinessException("角色名称已存在");
        }
        roleMapper.updateRole(roleDO);
        LOG.info("修改角色");
    }

    /**
     * 删除角色
     * @param roleId 角色id
     */
    @Override
    public void removeRoleById(Integer roleId) throws BusinessException {
        UserDO userDO = new UserDO();
        userDO.setRoleId(roleId);
        if (!userMapper.selectUserList(userDO).isEmpty()) {
            throw new BusinessException("该角色已关联用户");
        }
        roleMapper.deleteRoleById(roleId);
        LOG.info("删除角色");
    }

    /**
     * 检查角色名称的重复性
     * @param roleDTO 查询条件
     * @return 角色列表 为empty则代表名称不重复
     */
    @Override
    public List<RoleVO> checkRoleNameRepeat(RoleDTO roleDTO) {
        return roleMapper.checkRoleNameRepeat(roleDTO);
    }

    /**
     * 查询角色已有权限id
     * @param roleId 角色编号
     * @return 权限编号数组
     */
    @Override
    public JSONArray findPermissionIdArrayByRoleId(Integer roleId) throws ValidException {
        ValidUtil.notNUll(roleId, "参数错误");
        return roleMapper.selectPermissionIdArrayByRoleId(roleId);
    }

    /**
     * 查询角色已有权限code
     * @param roleId 角色编号
     * @return 权限编号数组
     */
    @Override
    public JSONArray findPermissionCodeArrayByRoleId(Integer roleId) {
        // 没角色不给任何权限
        if (roleId == null) {
            return new JSONArray();
        }
        return roleMapper.selectPermissionCodeArrayByRoleId(roleId);
    }

    /**
     * 为角色新增权限
     * @param roleId 角色id
     * @param permissionId 权限id
     */
    @Override
    public void saveRolePermission(Integer roleId, Integer permissionId, Date updateTime) throws ValidException {
        ValidUtil.notNUll(roleId, "参数错误");
        ValidUtil.notNUll(permissionId, "参数错误");
        // 防止关联表重复添加
        if (roleMapper.selectRolePermission(roleId, permissionId).isEmpty()) {
            roleMapper.insertRolePermission(roleId, permissionId, updateTime);
        }
    }

    /**
     * 为角色删除权限
     * @param roleId 角色id
     * @param permissionId 权限id
     * @throws ValidException 参数异常
     */
    @Override
    public void removeRolePermission(Integer roleId, Integer permissionId) throws ValidException {
        ValidUtil.notNUll(roleId, "参数错误");
        ValidUtil.notNUll(permissionId, "参数错误");
        roleMapper.deleteRolePermission(roleId, permissionId);
    }

    /**
     * 删除权限角色关联记录
     * @param permissionId 权限id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removePermissionRoleRef(Integer permissionId) {
        roleMapper.deletePermissionRoleRef(permissionId);
    }
}
