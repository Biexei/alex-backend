package org.alex.platform.controller;

import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.MockServerDO;
import org.alex.platform.pojo.MockServerDTO;
import org.alex.platform.service.MockServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/mock/server")
public class MockServerController {
    @Autowired
    MockServerService mockServerService;
    @Autowired
    LoginUserInfo loginUserInfo;

    @PostMapping("/save")
    Result saveMockServer(MockServerDO mockServerDO, HttpServletRequest request) throws ValidException {
        int userId = loginUserInfo.getUserId(request);
        String realName = loginUserInfo.getRealName(request);
        mockServerDO.setCreatorId(userId);
        mockServerDO.setCreatorName(realName);
        mockServerService.saveMockServer(mockServerDO);
        return Result.success("新增成功");
    }

    @PostMapping("/modify")
    Result modifyMockServer(MockServerDO mockServerDO, HttpServletRequest request) throws ValidException {
        int userId = loginUserInfo.getUserId(request);
        String realName = loginUserInfo.getRealName(request);
        mockServerDO.setCreatorId(userId);
        mockServerDO.setCreatorName(realName);
        mockServerService.modifyMockServer(mockServerDO);
        return Result.success();
    }

    @GetMapping("/all")
    Result findAllMockServer(MockServerDTO mockServerDTO) {
        return Result.success(mockServerService.findAllMockServer(mockServerDTO));
    }

    @GetMapping("")
    Result findMockServer(MockServerDTO mockServerDTO, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return Result.success(mockServerService.findMockServer(mockServerDTO, pageNum, pageSize));
    }

    @GetMapping("/{serverId}")
    Result findMockServerById(@PathVariable Integer serverId) {
        return Result.success(mockServerService.findMockServerById(serverId));
    }

    @GetMapping("/remove/{serverId}")
    Result removeMockServer(@PathVariable Integer serverId) throws BusinessException {
        mockServerService.removeMockServer(serverId);
        return Result.success();
    }

    @GetMapping("/stop/{serverId}")
    Result stopMockServer(@PathVariable Integer serverId) {
        mockServerService.stopMockServer(serverId);
        return Result.success();
    }

    @GetMapping("/start/{serverId}")
    Result startMockServer(@PathVariable Integer serverId) throws BusinessException {
        mockServerService.startMockServer(serverId);
        return Result.success();
    }

    @GetMapping("/force/start/{serverId}")
    Result forceStartMockServer(@PathVariable Integer serverId) throws BusinessException {
        mockServerService.forceStartMockServer(serverId);
        return Result.success();
    }

    @GetMapping("/stop")
    Result stopAllMockServer() {
        mockServerService.stopAllMockServer();
        return Result.success();
    }

    @GetMapping("/start")
    Result startAllMockServer() throws BusinessException {
        mockServerService.startAllMockServer();
        return Result.success();
    }
}
