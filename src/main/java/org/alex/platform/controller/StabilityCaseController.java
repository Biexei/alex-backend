package org.alex.platform.controller;

import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.StabilityCaseDO;
import org.alex.platform.pojo.StabilityCaseDTO;
import org.alex.platform.service.StabilityCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class StabilityCaseController {
    @Autowired
    StabilityCaseService stabilityCaseService;
    @Autowired
    LoginUserInfo loginUserInfo;

    @PostMapping("/stability/case/save")
    Result saveStabilityCase(@Validated StabilityCaseDO stabilityCaseDO, HttpServletRequest request) throws ValidException {
        String realName = loginUserInfo.getRealName(request);
        int userId = loginUserInfo.getUserId(request);
        stabilityCaseDO.setCreatorId(userId);
        stabilityCaseDO.setCreatorName(realName);
        stabilityCaseService.saveStabilityCase(stabilityCaseDO);
        return Result.success("新增成功");
    }

    @PostMapping("/stability/case/modify")
    Result modifyStabilityCase(@Validated StabilityCaseDO stabilityCaseDO) throws ValidException {
        stabilityCaseService.modifyStabilityCase(stabilityCaseDO);
        return Result.success("修改成功");
    }

    @GetMapping("/stability/case/info/{id}")
    Result findStabilityCaseById(@PathVariable Integer id) {
        return Result.success(stabilityCaseService.findStabilityCaseById(id));
    }

    @GetMapping("/stability/case/list")
    Result findStabilityCaseList(StabilityCaseDTO stabilityCaseDTO, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return Result.success(stabilityCaseService.findStabilityCaseList(stabilityCaseDTO, pageNum, pageSize));
    }

    @GetMapping("/stability/case/remove/{id}")
    Result removeStabilityCaseById(@PathVariable Integer id) {
        stabilityCaseService.removeStabilityCaseById(id);
        return Result.success("删除成功");
    }

    @GetMapping("/stability/case/stop/{id}")
    Result stopStabilityCaseById(@PathVariable Integer id, HttpServletRequest request) throws BusinessException {
        int userId = loginUserInfo.getUserId(request);
        stabilityCaseService.stopStabilityCaseByLogId(id, userId);
        return Result.success("已停止");
    }

    @GetMapping("/stability/case/execute/{id}")
    Result executeStabilityCaseById(@PathVariable Integer id, HttpServletRequest request) throws Exception {
        int userId = loginUserInfo.getUserId(request);
        stabilityCaseService.executable(id);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.execute(() -> {
            try {
                stabilityCaseService.executeStabilityCaseById(id, userId);
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        });
        return Result.success("开始执行");
    }
}
