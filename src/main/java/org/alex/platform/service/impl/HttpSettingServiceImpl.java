package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.mapper.HttpSettingMapper;
import org.alex.platform.pojo.HttpSettingDO;
import org.alex.platform.pojo.HttpSettingDTO;
import org.alex.platform.pojo.HttpSettingVO;
import org.alex.platform.service.HttpSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class HttpSettingServiceImpl implements HttpSettingService {
    @Autowired
    HttpSettingMapper httpSettingMapper;

    @Override
    public HttpSettingVO findHttpSettingById(Integer settingId) {
        return httpSettingMapper.selectHttpSettingById(settingId);
    }

    @Override
    public PageInfo<HttpSettingVO> findHttpSetting(HttpSettingDTO httpSettingDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(httpSettingMapper.selectHttpSetting(httpSettingDTO));
    }

    @Override
    public void saveHttpSetting(HttpSettingDO httpSettingDO) {
        httpSettingMapper.insertHttpSetting(httpSettingDO);
    }

    @Override
    public void modifyHttpSetting(HttpSettingDO httpSettingDO) {
        // 如果是代理，则将其它的代理项关闭
        Integer settingId = httpSettingDO.getSettingId();
        HttpSettingVO httpSettingVO = httpSettingMapper.selectHttpSettingById(settingId);
        if (httpSettingVO != null) {
            if (httpSettingVO.getType() == 0 && httpSettingDO.getStatus() == 0) { // 0为代理
                httpSettingMapper.closeOtherProxy(settingId);
            }
        }
        httpSettingMapper.updateHttpSetting(httpSettingDO);
    }

    @Override
    public void removeHttpSetting(Integer settingId) {
        httpSettingMapper.deleteHttpSetting(settingId);
    }

    @Override
    public ArrayList<String> findAllEmail() {
        return httpSettingMapper.selectAllEmail();
    }
}
