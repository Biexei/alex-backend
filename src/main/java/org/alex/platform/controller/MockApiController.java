package org.alex.platform.controller;

import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.MockApiAndPolicyDO;
import org.alex.platform.pojo.MockApiDTO;
import org.alex.platform.service.MockApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/mock/api")
public class MockApiController {
    @Autowired
    LoginUserInfo loginUserInfo;
    @Autowired
    MockApiService mockApiService;

    @PostMapping("/save")
    public Result saveMockApiAndPolicy(@RequestBody MockApiAndPolicyDO policyDO, HttpServletRequest request) throws BusinessException, ParseException, SqlException {
        int userId = loginUserInfo.getUserId(request);
        String realName = loginUserInfo.getRealName(request);
        policyDO.setCreatorId(userId);
        policyDO.setCreatorName(realName);
        mockApiService.saveMockApiAndPolicy(policyDO);
        return Result.success("新增成功");
    }

    @PostMapping("/modify")
    public Result modifyMockApiAndPolicy(@RequestBody MockApiAndPolicyDO policyDO) throws BusinessException, ParseException, SqlException {
        mockApiService.modifyMockApiAndPolicy(policyDO);
        return Result.success("修改成功");
    }

    @GetMapping("/list")
    public Result findMockApiList(MockApiDTO mockApiDTO, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1: pageNum;
        pageSize = pageSize == null ? 10: pageSize;
        return Result.success(mockApiService.findMockApiList(mockApiDTO, pageNum, pageSize));
    }

    @GetMapping("/{apiId}")
    public Result findMockApiById(@PathVariable Integer apiId) {
        return Result.success(mockApiService.findMockApiById(apiId));
    }

    @GetMapping("/remove/{apiId}")
    public Result removeMockApiById(@PathVariable Integer apiId) {
        mockApiService.removeMockApiById(apiId);
        return Result.success();
    }

    @GetMapping("/stop/{apiId}")
    public Result stopApi(@PathVariable Integer apiId) {
        mockApiService.stopApi(apiId);
        return Result.success();
    }

    @GetMapping("/restart/{apiId}")
    public Result restartApi(@PathVariable Integer apiId) throws BusinessException, ParseException, SqlException {
        mockApiService.restartApi(apiId);
        return Result.success();
    }
}
