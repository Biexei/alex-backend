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

    /**
     * 获取配置项详情
     *
     * @param settingId 配置编号
     * @return Result
     */
    @GetMapping("setting/{settingId}")
    public Result findHttpSettingById(@PathVariable Integer settingId) {
        return Result.success(httpSettingService.findHttpSettingById(settingId));
    }

    /**
     * 获取配置项列表
     *
     * @param httpSettingDTO httpSettingDTO
     * @param pageNum        pageNum
     * @param pageSize       pageSize
     * @return Result
     */
    @GetMapping("setting")
    public Result findHttpSetting(HttpSettingDTO httpSettingDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(httpSettingService.findHttpSetting(httpSettingDTO, num, size));
    }

    /**
     * 新增配置项
     *
     * @param httpSettingDO httpSettingDO
     * @return Result
     * @throws BusinessException 代理服务器/邮箱地址格式校验
     */
    @PostMapping("setting/save")
    public Result saveHttpSetting(@Validated HttpSettingDO httpSettingDO) throws BusinessException {
        httpSettingDO.setStatus((byte) 0);
        // 校验代理服务器格式
        if (httpSettingDO.getType() == 0) {
            httpSettingDO.setStatus((byte) 1);
            String domain = httpSettingDO.getValue();
            if (!Pattern.matches("[a-zA-z0-9\\.]+:(\\d+)$", domain)) {
                throw new BusinessException("代理服务器格式错误");
            }
        } else if (httpSettingDO.getType() == 2) { // 邮箱
            String email = httpSettingDO.getValue();
            if (!Pattern.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?", email)) {
                throw new BusinessException("邮箱格式错误");
            }
        }

        Date date = new Date();
        httpSettingDO.setCreatedTime(date);
        httpSettingDO.setUpdateTime(date);
        httpSettingService.saveHttpSetting(httpSettingDO);
        return Result.success("新增成功");
    }

    /**
     * 修改配置项
     *
     * @param httpSettingDO httpSettingDO
     * @return Result
     * @throws BusinessException 代理服务器/邮箱地址格式校验
     */
    @PostMapping("setting/modify")
    public Result modifyHttpSetting(@Validated HttpSettingDO httpSettingDO) throws BusinessException {
        if (httpSettingDO.getType() == 0) {
            String domain = httpSettingDO.getValue();
            if (!Pattern.matches("[a-zA-z0-9\\.]+:(\\d+)$", domain)) {
                throw new BusinessException("代理服务器格式错误");
            }
        } else if (httpSettingDO.getType() == 2) { // 邮箱
            String email = httpSettingDO.getValue();
            if (!Pattern.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?", email)) {
                throw new BusinessException("邮箱格式错误");
            }
        }

        Date date = new Date();
        httpSettingDO.setUpdateTime(date);
        httpSettingService.modifyHttpSetting(httpSettingDO);
        return Result.success("修改成功");
    }

    /**
     * 删除配置项
     *
     * @param settingId 配置编号
     * @return Result
     */
    @GetMapping("setting/remove/{settingId}")
    public Result removeHttpSettingById(@PathVariable Integer settingId) {
        httpSettingService.removeHttpSetting(settingId);
        return Result.success("删除成功");
    }

    /**
     * 获取所有已启用邮箱列表
     *
     * @return Result
     */
    @GetMapping("setting/email/all")
    public Result findAllEmail() {
        return Result.success(httpSettingService.findAllEmail());
    }
}
