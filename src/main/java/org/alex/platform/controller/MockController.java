package org.alex.platform.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mock.MockServerPool;
import org.mockserver.integration.ClientAndServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
@RestController
public class MockController {

    private final HashMap<String, ClientAndServer> serverPool = MockServerPool.getInstance();

    @GetMapping("/mock/server/get/{port}")
    public Result getMockServer(@PathVariable Integer port) throws BusinessException {
        MockServerPool.get(port);
        return Result.success(server());
    }

    @GetMapping("/mock/server/info/{port}")
    public Result infoMockServer(@PathVariable Integer port) throws BusinessException {
        ClientAndServer server = MockServerPool.get(port);
        return Result.success(server.toString());
    }

    @GetMapping("/mock/server/start/{port}")
    public Result startMockServer(@PathVariable Integer port) throws BusinessException {
        MockServerPool.start(port);
        return Result.success(server());
    }

    @GetMapping("/mock/server/stop/{port}")
    public Result stopMockServer(@PathVariable Integer port) {
        MockServerPool.stop(port);
        return Result.success(server());
    }

    @GetMapping("/mock/server/clear/")
    public Result clearMockServer() {
        MockServerPool.clear();
        return Result.success(server());
    }

    @GetMapping("/mock/server/status/{port}")
    public Result mockServerStatus(@PathVariable Integer port) {
        boolean isRunning = MockServerPool.isRunning(port);
        JSONObject result = new JSONObject();
        if (isRunning) {
            result.put("status", 0);
            result.put("msg", "启动");
        } else {
            result.put("status", 1);
            result.put("msg", "停用");
        }
        return Result.success(result);
    }

    private String server() {
        return JSON.toJSONString(serverPool.keySet());
    }
}
