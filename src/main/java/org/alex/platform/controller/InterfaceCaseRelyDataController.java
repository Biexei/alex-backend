package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.InterfaceCaseRelyDataDO;
import org.alex.platform.pojo.InterfaceCaseRelyDataDTO;
import org.alex.platform.service.InterfaceCaseRelyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class InterfaceCaseRelyDataController {
    @Autowired
    InterfaceCaseRelyDataService ifCaseService;

    @PostMapping("/interface/rely/save")
    public Result saveIfRelyData(@Validated InterfaceCaseRelyDataDO ifRelyDataDO) throws BusinessException {
        Date date = new Date();
        ifRelyDataDO.setCreatedTime(date);
        ifRelyDataDO.setUpdateTime(date);
        ifCaseService.saveIfRelyData(ifRelyDataDO);
        return Result.success("新增成功");
    }

    @PostMapping("/interface/rely/modify")
    public Result modifyIfRelyData(@Validated InterfaceCaseRelyDataDO ifRelyDataDO) throws BusinessException {
        ifRelyDataDO.setUpdateTime(new Date());
        ifCaseService.modifyIfRelyData(ifRelyDataDO);
        return Result.success("修改成功");
    }

    @GetMapping("/interface/rely/{relyId}")
    public Result findIfRelyData(@PathVariable Integer relyId) {
        return Result.success(ifCaseService.findIfRelyData(relyId));
    }

    @GetMapping("/interface/rely")
    public Result findIfRelyDataList(InterfaceCaseRelyDataDTO ifRelyDataDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(ifCaseService.findIfRelyDataList(ifRelyDataDTO, num, size));
    }

    @GetMapping("/interface/rely/remove/{relyId}")
    public Result removeIfRelyData(@PathVariable Integer relyId) {
        ifCaseService.removeIfRelyData(relyId);
        return Result.success("删除成功");
    }
}
