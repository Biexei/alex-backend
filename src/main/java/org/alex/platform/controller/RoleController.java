package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.RoleDO;
import org.alex.platform.pojo.RoleDTO;
import org.alex.platform.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RoleController {
    @Autowired
    RoleService roleService;
    @GetMapping("/role/{roleId}")
    public Result findRoleById(@PathVariable Integer roleId) {
        return Result.success(roleService.findRoleById(roleId));
    }

    @GetMapping("/role")
    public Result findRole(RoleDTO roleDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 0 : pageNum;
        int size = pageSize == null ? 0 : pageSize;
        return Result.success(roleService.findRole(roleDTO, num, size));
    }

    @GetMapping("/role/all")
    public Result findAllRole(RoleDTO roleDTO) {
        return Result.success(roleService.findAllRole(roleDTO));
    }
    @PostMapping("/role/save")
    public Result saveRole(@Validated RoleDO roleDO) throws BusinessException {
        roleService.saveRole(roleDO);
        return Result.success("新增成功");
    }
    @PostMapping("/role/modify")
    public Result modifyRole(@Validated RoleDO roleDO) throws BusinessException {
        roleService.modifyRole(roleDO);
        return Result.success("修改成功");
    }

    @GetMapping("/role/remove/{roleId}")
    public Result removeRoleById(@PathVariable Integer roleId) throws BusinessException {
        roleService.removeRoleById(roleId);
        return Result.success("删除成功");
    }
}
