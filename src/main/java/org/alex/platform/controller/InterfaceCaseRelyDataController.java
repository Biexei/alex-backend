package org.alex.platform.controller;

import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.pojo.InterfaceCaseRelyDataDO;
import org.alex.platform.pojo.InterfaceCaseRelyDataDTO;
import org.alex.platform.service.InterfaceCaseRelyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;

@RestController
public class InterfaceCaseRelyDataController {
    @Autowired
    InterfaceCaseRelyDataService ifCaseService;
    @Autowired
    LoginUserInfo loginUserInfo;

    /**
     * 新增数据依赖
     *
     * @param ifRelyDataDO ifRelyDataDO
     * @return Result
     * @throws BusinessException BusinessException
     */
    @PostMapping("/interface/rely/save")
    public Result saveIfRelyData(@Validated InterfaceCaseRelyDataDO ifRelyDataDO, HttpServletRequest request) throws BusinessException {
        int userId = loginUserInfo.getUserId(request);
        String realName = loginUserInfo.getRealName(request);
        Date date = new Date();
        ifRelyDataDO.setCreatedTime(date);
        ifRelyDataDO.setUpdateTime(date);
        ifRelyDataDO.setCreatorId(userId);
        ifRelyDataDO.setCreatorName(realName);
        ifCaseService.saveIfRelyData(ifRelyDataDO);
        return Result.success("新增成功");
    }

    /**
     * 修改数据依赖
     *
     * @param ifRelyDataDO ifRelyDataDO
     * @return Result
     * @throws BusinessException BusinessException
     */
    @PostMapping("/interface/rely/modify")
    public Result modifyIfRelyData(@Validated InterfaceCaseRelyDataDO ifRelyDataDO, HttpServletRequest request) throws BusinessException {
        ifRelyDataDO.setUpdateTime(new Date());
        ifCaseService.modifyIfRelyData(ifRelyDataDO, request);
        return Result.success("修改成功");
    }

    /**
     * 获取数据依赖详情
     *
     * @param relyId 依赖编号
     * @return Result
     */
    @GetMapping("/interface/rely/{relyId}")
    public Result findIfRelyData(@PathVariable Integer relyId) {
        return Result.success(ifCaseService.findIfRelyData(relyId));
    }

    /**
     * 获取数据依赖列表
     *
     * @param ifRelyDataDTO ifRelyDataDTO
     * @param pageNum       pageNum
     * @param pageSize      pageSize
     * @return Result
     */
    @GetMapping("/interface/rely")
    public Result findIfRelyDataList(InterfaceCaseRelyDataDTO ifRelyDataDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(ifCaseService.findIfRelyDataList(ifRelyDataDTO, num, size));
    }

    /**
     * 删除数据依赖
     *
     * @param relyId 依赖编号
     * @return Result
     */
    @GetMapping("/interface/rely/remove/{relyId}")
    public Result removeIfRelyData(@PathVariable Integer relyId, HttpServletRequest request) throws BusinessException {
        ifCaseService.removeIfRelyData(relyId, request);
        return Result.success("删除成功");
    }

    /**
     * 预检
     *
     * @param relyId 依赖编号
     * @return Result
     * @throws ParseException    ParseException
     * @throws BusinessException BusinessException
     * @throws SqlException      SqlException
     */
    @GetMapping("/interface/rely/check/{relyId}")
    public Result checkIfRelyData(@PathVariable Integer relyId, HttpServletRequest request) throws ParseException, BusinessException, SqlException {
        String executor = loginUserInfo.getRealName(request);
        String result = ifCaseService.checkRelyResult(relyId, executor);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("result", result);
        return Result.success(hashMap);
    }
}
