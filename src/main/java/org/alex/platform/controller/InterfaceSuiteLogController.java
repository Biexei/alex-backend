package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.pojo.InterfaceSuiteLogDO;
import org.alex.platform.pojo.InterfaceSuiteLogDTO;
import org.alex.platform.service.InterfaceSuiteLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InterfaceSuiteLogController {
    @Autowired
    InterfaceSuiteLogService interfaceSuiteLogService;

    /**
     * 查询测试套件执行日志列表
     *
     * @param interfaceSuiteLogDTO interfaceSuiteLogDTO
     * @param pageNum                  pageNum
     * @param pageSize                 pageSize
     * @return Result
     */
    @GetMapping("/suite/log")
    public Result findIfSuiteLog(InterfaceSuiteLogDTO interfaceSuiteLogDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(interfaceSuiteLogService.findIfSuiteLog(interfaceSuiteLogDTO, num, size));
    }

    /**
     * 根据编号查询测试套件执行日志
     *
     * @param suiteLogNo suiteLogNo
     * @return Result
     */
    @GetMapping("/suite/log/no/{suiteLogNo}")
    public Result findIfSuiteLogByNo(@PathVariable String suiteLogNo) {
        return Result.success(interfaceSuiteLogService.findIfSuiteLogByNo(suiteLogNo));
    }

    /**
     * 根据测试套件执行日志编号查询日志详情
     *
     * @param id id
     * @return Result
     */
    @GetMapping("/suite/log/{id}")
    public Result findIfSuiteLogById(@PathVariable Integer id) {
        return Result.success(interfaceSuiteLogService.findIfSuiteLogById(id));
    }

    /**
     * 根据项目 模块统计执行情况
     * @param suiteLogNo suiteLogNo
     * @return Result
     */
    @GetMapping("/suite/summary/{suiteLogNo}")
    public Result findSuiteLogSummary(@PathVariable String suiteLogNo) {
        return Result.success(interfaceSuiteLogService.findSuiteLogSummary(suiteLogNo));
    }


    /**
     * 统计测试报告首页测试套件执行情况
     *
     * @param suiteLogNo 测试套件日志编号
     * @return HashMap
     */
    @GetMapping("/suite/assert/log/{suiteLogNo}")
    public Result findSuiteReportAssert(@PathVariable String suiteLogNo) {
        return Result.success(interfaceSuiteLogService.findSuiteReportAssert(suiteLogNo));
    }
}
