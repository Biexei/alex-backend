package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.pojo.UserLoginLogDTO;
import org.alex.platform.service.UserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserLoginLogController {
    @Autowired
    UserLoginLogService userLoginLogService;

    @GetMapping("/login/log")
    public Result findUserLoginLogList(UserLoginLogDTO userLoginLogDTO, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return Result.success(userLoginLogService.findUserLoginLogList(userLoginLogDTO, pageNum, pageSize));
    }
}
