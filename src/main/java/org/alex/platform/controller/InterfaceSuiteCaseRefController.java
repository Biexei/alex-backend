package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.pojo.InterfaceSuiteCaseRefDO;
import org.alex.platform.pojo.InterfaceSuiteCaseRefDTO;
import org.alex.platform.service.InterfaceSuiteCaseRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class InterfaceSuiteCaseRefController {
    @Autowired
    InterfaceSuiteCaseRefService refService;

    @PostMapping("/interface/suite/case/save")
    public Result saveSuiteCase(@RequestBody List<InterfaceSuiteCaseRefDO> interfaceSuiteCaseRefDOList) {
        refService.saveSuiteCase(interfaceSuiteCaseRefDOList);
        return Result.success("新增成功");
    }

    @GetMapping("/interface/suite/case/remove/{id}")
    public Result removeSuiteCase(@PathVariable Integer id) {
        refService.removeSuiteCase(id);
        return Result.success("删除成功");
    }

    @GetMapping("/interface/suite/case")
    public Result findSuiteCaseList(InterfaceSuiteCaseRefDTO interfaceSuiteCaseRefDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(refService.findSuiteCaseList(interfaceSuiteCaseRefDTO, num, size));
    }
}
