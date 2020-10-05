package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.InterfaceAssertDO;
import org.alex.platform.service.InterfaceAssertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InterfaceAssertController {
    @Autowired
    InterfaceAssertService interfaceAssertService;

    /**
     * 新增断言
     *
     * @param interfaceAssertDO interfaceAssertDO
     * @return Result
     */
    @PostMapping("/interface/assert/save")
    public Result saveAssert(@Validated InterfaceAssertDO interfaceAssertDO) throws BusinessException {
        interfaceAssertService.saveAssert(interfaceAssertDO);
        return Result.success("新增成功");
    }

    /**
     * 修改断言
     *
     * @param interfaceAssertDO interfaceAssertDO
     * @return Result
     */
    @PostMapping("/interface/assert/modify")
    public Result modifyAssert(@Validated InterfaceAssertDO interfaceAssertDO) throws BusinessException {
        interfaceAssertService.modifyAssert(interfaceAssertDO);
        return Result.success("修改成功");
    }

    /**
     * 根据断言编号删除断言
     *
     * @param assertId 断言编号
     * @return Result
     */
    @GetMapping("/interface/assert/remove/{assertId}")
    public Result removeAssertByAssertId(@PathVariable Integer assertId) {
        interfaceAssertService.removeAssertByAssertId(assertId);
        return Result.success("删除成功");
    }

    /**
     * 根据用例编号删除断言
     *
     * @param caseId 断言编号
     * @return Result
     */
    @GetMapping("/interface/assert/remove/caseId/{caseId}")
    public Result removeAssertByCaseId(@PathVariable Integer caseId) {
        interfaceAssertService.removeAssertByCaseId(caseId);
        return Result.success("删除成功");
    }
}
