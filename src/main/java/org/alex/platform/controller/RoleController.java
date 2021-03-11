package org.alex.platform.controller;

import com.alibaba.fastjson.JSONObject;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.RoleDO;
import org.alex.platform.pojo.RoleDTO;
import org.alex.platform.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;


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

    @GetMapping("/role/permission/{roleId}")
    public Result findPermissionIdArrayByRoleId(@PathVariable Integer roleId) throws ValidException {
        return Result.success(roleService.findPermissionIdArrayByRoleId(roleId));
    }

    @PostMapping("/role/permission/save")
    public Result saveRolePermission(@RequestParam Integer roleId, @RequestParam Integer permissionId) throws ValidException {
        roleService.saveRolePermission(roleId, permissionId, new Date());
        return Result.success("新增成功");
    }

    @PostMapping("/role/permission/remove")
    public Result removeRolePermission(@RequestParam Integer roleId, @RequestParam Integer permissionId) throws ValidException {
        roleService.removeRolePermission(roleId, permissionId);
        return Result.success("删除成功");
    }
}
