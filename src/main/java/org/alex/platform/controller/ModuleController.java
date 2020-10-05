package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.ModuleDO;
import org.alex.platform.pojo.ModuleDTO;
import org.alex.platform.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class ModuleController {

    @Autowired
    ModuleService moduleService;

    /**
     * 根据模块名称、模块id、项目名称、项目id查询模块列表
     *
     * @param moduleDto 模块名称、模块id、项目名称、项目id
     * @param pageNum
     * @param pageSize  pageSize
     * @return
     */
    @RequestMapping("/module/list")
    public Result findProjectModuleList(ModuleDTO moduleDto, Integer pageNum, Integer pageSize) {
        Integer num = pageNum == null ? 1 : pageNum;
        Integer size = pageNum == null ? 10 : pageSize;
        return Result.success(moduleService.findModuleList(moduleDto, num, size));
    }

    /**
     * 新增模块
     *
     * @param moduleDO moduleDO
     * @return Result
     * @throws BusinessException BusinessException
     */
    @RequestMapping(path = "/module/save", method = RequestMethod.POST)
    public Result saveModule(@Validated ModuleDO moduleDO) throws BusinessException {
        Date date = new Date();
        moduleDO.setCreatedTime(date);
        moduleDO.setUpdateTime(date);
        moduleService.saveModule(moduleDO);
        return Result.success("新增成功");
    }

    /**
     * 修改模块信息
     *
     * @param moduleDO moduleDO
     * @return Result
     */
    @PostMapping("/module/modify")
    public Result modifyModule(@Validated ModuleDO moduleDO) {
        moduleDO.setUpdateTime(new Date());
        moduleService.modifyModule(moduleDO);
        return Result.success("修改成功");
    }

    /**
     * 删除模块
     *
     * @param moduleId 模块编号
     * @return Result
     * @throws BusinessException BusinessException
     */
    @GetMapping("/module/remove/{moduleId}")
    public Result removeModule(@PathVariable Integer moduleId) throws BusinessException {
        moduleService.removeModuleById(moduleId);
        return Result.success("删除成功");
    }
}
