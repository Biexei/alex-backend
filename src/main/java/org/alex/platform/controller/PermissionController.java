package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.PermissionDO;
import org.alex.platform.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PermissionController {
    @Autowired
    PermissionService permissionService;

    @GetMapping("/permission")
    public Result findAllPermission() {
        // 0代表父节点
        return Result.success(permissionService.findPermissionByParentId(0));
    }

    @GetMapping("/permission/{id}")
    public Result findPermissionById(@PathVariable Integer id) {
        return Result.success(permissionService.findPermissionById(id));
    }

    @PostMapping("/permission/save")
    public Result savePermission(PermissionDO permissionDO) throws BusinessException {
        Integer parentId = permissionDO.getParentId();
        parentId = parentId == null ? 0 : parentId;
        permissionDO.setParentId(parentId);
        permissionService.savePermission(permissionDO);
        return Result.success("新增成功");
    }

    @PostMapping("/permission/modify")
    public Result modifyPermission(PermissionDO permissionDO) throws BusinessException {
        permissionService.modifyPermission(permissionDO);
        return Result.success("修改成功");
    }

    @GetMapping("/permission/remove/{id}")
    public Result removePermissionById(@PathVariable Integer id) throws BusinessException {
        permissionService.removePermissionById(id);
        return Result.success("删除成功");
    }
}
