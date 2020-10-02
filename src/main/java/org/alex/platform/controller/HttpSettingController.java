package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.HttpSettingDO;
import org.alex.platform.pojo.HttpSettingDTO;
import org.alex.platform.service.HttpSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.regex.Pattern;

@RestController
public class HttpSettingController {
    @Autowired
    HttpSettingService httpSettingService;

    @GetMapping("setting/{settingId}")
    public Result findHttpSettingById(@PathVariable Integer settingId) {
        return Result.success(httpSettingService.findHttpSettingById(settingId));
    }

    @GetMapping("setting")
    public Result findHttpSetting(HttpSettingDTO httpSettingDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(httpSettingService.findHttpSetting(httpSettingDTO, num, size));
    }

    @PostMapping("setting/save")
    public Result saveHttpSetting(@Validated HttpSettingDO httpSettingDO) throws BusinessException {
        // 校验代理服务器格式
        if (httpSettingDO.getType() == 0) {
            String domain = httpSettingDO.getValue();
            if (!Pattern.matches("[a-zA-z]+:(\\d+)$", domain)) {
                throw new BusinessException("代理服务器格式错误");
            }
        }

        Date date = new Date();
        httpSettingDO.setStatus((byte) 1);
        httpSettingDO.setCreatedTime(date);
        httpSettingDO.setUpdateTime(date);
        httpSettingService.saveHttpSetting(httpSettingDO);
        return Result.success("新增成功");
    }

    @PostMapping("setting/modify")
    public Result modifyHttpSetting(@Validated HttpSettingDO httpSettingDO) throws BusinessException {
        if (httpSettingDO.getType() == 0) {
            String domain = httpSettingDO.getValue();
            if (!Pattern.matches("[a-zA-z]+:(\\d+)$", domain)) {
                throw new BusinessException("代理服务器格式错误");
            }
        }
        Date date = new Date();
        httpSettingDO.setUpdateTime(date);
        httpSettingService.modifyHttpSetting(httpSettingDO);
        return Result.success("修改成功");
    }

    @GetMapping("setting/remove/{settingId}")
    public Result removeHttpSettingById(@PathVariable Integer settingId) {
        httpSettingService.removeHttpSetting(settingId);
        return Result.success("删除成功");
    }
}
