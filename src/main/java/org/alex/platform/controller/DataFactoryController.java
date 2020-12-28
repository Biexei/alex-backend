package org.alex.platform.controller;

import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.DataFactoryDO;
import org.alex.platform.pojo.DataFactoryDTO;
import org.alex.platform.service.DataFactoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
public class DataFactoryController {
    @Autowired
    DataFactoryService service;
    @Autowired
    LoginUserInfo loginUserInfo;

    /**
     * 新增数据工厂
     * @param dataFactoryDO dataFactoryDO
     * @return saveDataFactory
     * @throws ValidException ValidException
     */
    @PostMapping("/data/factory/save")
    Result saveDataFactory(@Validated DataFactoryDO dataFactoryDO) throws ValidException {
        service.saveDataFactory(dataFactoryDO);
        return Result.success("新增成功");
    }

    /**
     * 修改数据工厂
     * @param dataFactoryDO dataFactoryDO
     * @return modifyDataFactory
     * @throws ValidException ValidException
     */
    @PostMapping("/data/factory/modify")
    Result modifyDataFactory(@Validated DataFactoryDO dataFactoryDO) throws ValidException {
        service.modifyDataFactory(dataFactoryDO);
        return Result.success("修改成功");
    }

    /**
     * 移除数据工厂
     * @param id id
     * @return removeDataFactoryById
     */
    @GetMapping("/data/factory/remove/{id}")
    Result removeDataFactoryById(@PathVariable Integer id) {
        service.removeDataFactoryById(id);
        return Result.success("删除成功");
    }

    /**
     * 获取数据工厂详情
     * @param id id
     * @return findDataFactoryById
     */
    @GetMapping("/data/factory/{id}")
    Result findDataFactoryById(@PathVariable Integer id) {
        return Result.success(service.findDataFactoryById(id));
    }

    /**
     * 获取数据工厂列表
     * @param dataFactoryDTO dataFactoryDTO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return findDataFactoryList
     */
    @GetMapping("/data/factory")
    Result findDataFactoryList(DataFactoryDTO dataFactoryDTO, Integer pageNum, Integer pageSize) {
        Integer num = pageNum == null ? 1 : pageNum;
        Integer size = pageSize == null ? 10 : pageSize;
        return Result.success(service.findDataFactoryList(dataFactoryDTO, num, size));
    }

    /**
     * 执行数据工程
     * @param id id
     * @param request request
     * @return 总耗时ms
     * @throws BusinessException BusinessException
     */
    @GetMapping("/data/factory/execute/{id}")
    Result executeDataFactory(@PathVariable Integer id, HttpServletRequest request) throws BusinessException {
        String executor = loginUserInfo.getRealName(request);
        long runTime = service.executeDataFactory(id, executor);
        return Result.success(runTime);
    }
}
