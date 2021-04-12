package org.alex.platform.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.mock.MockServerPool;
import org.mockserver.integration.ClientAndServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

@RestController
public class MockController {

    private final Vector<ClientAndServer> serverPool = MockServerPool.getInstance();

    @GetMapping("/mock/server/get/{port}")
    public Result getMockServer(@PathVariable Integer port) throws BusinessException {
        MockServerPool.get(port);
        return Result.success();
    }

    @GetMapping("/mock/server/start/{port}")
    public Result startMockServer(@PathVariable Integer port) throws BusinessException {
        MockServerPool.start(port);
        return Result.success();
    }

    @GetMapping("/mock/server/stop/{port}")
    public Result stopMockServer(@PathVariable Integer port) {
        MockServerPool.stop(port);
        return Result.success();
    }

    @GetMapping("/mock/server/clear/")
    public Result clearMockServer() {
        MockServerPool.clear();
        return Result.success();
    }

    @GetMapping("/mock/server/summary")
    private Result mockServerSummary() {
        JSONObject result = new JSONObject();
        if (serverPool == null || serverPool.isEmpty()) {
            result.put("count", 0);
            result.put("ports", new JSONArray());
            return Result.success(result);
        }
        List<Integer> ports = serverPool.stream().map(ClientAndServer::getPort).collect(Collectors.toList());
        result.put("count", serverPool.size());
        result.put("ports", ports);
        return Result.success(result);
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
}
