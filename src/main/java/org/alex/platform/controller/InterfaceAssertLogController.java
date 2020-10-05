package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.pojo.InterfaceAssertLogDTO;
import org.alex.platform.service.InterfaceAssertLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InterfaceAssertLogController {
    @Autowired
    InterfaceAssertLogService interfaceAssertLogService;

    /**
     * 查看断言日志列表
     * @param interfaceAssertLogDTO interfaceAssertLogDTO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return Result
     */
    @GetMapping("/interface/assert/log")
    public Result listInterfaceAssertLog(InterfaceAssertLogDTO interfaceAssertLogDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(interfaceAssertLogService.findInterfaceAssertLogList(interfaceAssertLogDTO, num, size));
    }
}
