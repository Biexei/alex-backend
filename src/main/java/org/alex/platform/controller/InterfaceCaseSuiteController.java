package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.pojo.InterfaceCaseSuiteDO;
import org.alex.platform.pojo.InterfaceCaseSuiteDTO;
import org.alex.platform.service.InterfaceCaseSuiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class InterfaceCaseSuiteController {
    @Autowired
    InterfaceCaseSuiteService interfaceCaseSuiteService;

    @PostMapping("/interface/suite/save")
    public Result saveInterfaceCaseSuite(@Validated InterfaceCaseSuiteDO interfaceCaseSuiteDO) {
        Date date = new Date();
        interfaceCaseSuiteDO.setCreatedTime(date);
        interfaceCaseSuiteDO.setUpdateTime(date);
        interfaceCaseSuiteService.saveInterfaceCaseSuite(interfaceCaseSuiteDO);
        return Result.success("新增成功");
    }

    @PostMapping("/interface/suite/modify")
    public Result modifyInterfaceCaseSuite(@Validated InterfaceCaseSuiteDO interfaceCaseSuiteDO) {
        interfaceCaseSuiteDO.setUpdateTime(new Date());
        interfaceCaseSuiteService.modifyInterfaceCaseSuite(interfaceCaseSuiteDO);
        return Result.success("修改成功");
    }

    @GetMapping("/interface/suite/remove/{suiteId}")
    public Result removeInterfaceCaseSuiteById(@PathVariable Integer suiteId) {
        interfaceCaseSuiteService.removeInterfaceCaseSuiteById(suiteId);
        return Result.success("删除成功");
    }

    @GetMapping("/interface/suite/{suiteId}")
    public Result findInterfaceCaseSuiteById(@PathVariable Integer suiteId) {
        return Result.success(interfaceCaseSuiteService.findInterfaceCaseSuiteById(suiteId));
    }

    @GetMapping("/interface/suite")
    public Result findInterfaceCaseSuite(InterfaceCaseSuiteDTO interfaceCaseSuiteDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(interfaceCaseSuiteService.findInterfaceCaseSuite(interfaceCaseSuiteDTO, num, size));
    }
}
