package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.pojo.InterfaceCaseExecuteLogListDTO;
import org.alex.platform.service.InterfaceCaseExecuteLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InterfaceCaseExecuteLogController {
    @Autowired
    InterfaceCaseExecuteLogService executeLogService;

    /**
     * 获取接口执行日志详情
     *
     * @param executeId 执行编号
     * @return Result
     */
    @GetMapping("/interface/log/{executeId}")
    public Result findInterfaceCaseExecuteLog(@PathVariable Integer executeId) {
        return Result.success(executeLogService.findExecute(executeId));
    }

    /**
     * 获取接口执行日志列表
     *
     * @param executeLogListDTO executeLogListDTO
     * @param pageNum           pageNum
     * @param pageSize          pageSize
     * @return Result
     */
    @GetMapping("/interface/log/list")
    public Result findInterfaceCaseExecuteLogList(InterfaceCaseExecuteLogListDTO executeLogListDTO, Integer pageNum,
                                                  Integer pageSize) {
        Integer num = pageNum == null ? 1 : pageNum;
        Integer size = pageSize == null ? 10 : pageSize;
        return Result.success(executeLogService.findExecuteList(executeLogListDTO, num, size));
    }

    /**
     * 获取执行用例的调用链
     *
     * @param executeId executeId
     * @return Result
     */
    @GetMapping("/interface/log/chain/{executeId}")
    public Result caseExecuteLogChain(@PathVariable Integer executeId) {
        return Result.success(executeLogService.caseExecuteLogChain(executeId));
    }
}
