package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.mapper.HttpSettingMapper;
import org.alex.platform.pojo.HttpSettingDO;
import org.alex.platform.pojo.HttpSettingDTO;
import org.alex.platform.pojo.HttpSettingVO;
import org.alex.platform.service.HttpSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class HttpSettingServiceImpl implements HttpSettingService {
    @Autowired
    HttpSettingMapper httpSettingMapper;
    private static final Logger LOG = LoggerFactory.getLogger(HttpSettingServiceImpl.class);

    /**
     * 获取配置项详情
     *
     * @param settingId 配置项编号
     * @return HttpSettingVO
     */
    @Override
    public HttpSettingVO findHttpSettingById(Integer settingId) {
        return httpSettingMapper.selectHttpSettingById(settingId);
    }

    /**
     * 获取配置项列表
     *
     * @param httpSettingDTO httpSettingDTO
     * @param pageNum        pageNum
     * @param pageSize       pageSize
     * @return PageInfo<HttpSettingVO>
     */
    @Override
    public PageInfo<HttpSettingVO> findHttpSetting(HttpSettingDTO httpSettingDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(httpSettingMapper.selectHttpSetting(httpSettingDTO));
    }

    /**
     * 新增配置项
     *
     * @param httpSettingDO httpSettingDO
     */
    @Override
    public void saveHttpSetting(HttpSettingDO httpSettingDO) {
        httpSettingMapper.insertHttpSetting(httpSettingDO);
    }

    /**
     * 修改配置项
     *
     * @param httpSettingDO httpSettingDO
     */
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

    /**
     * 删除配置项
     *
     * @param settingId 配置编号
     */
    @Override
    public void removeHttpSetting(Integer settingId) {
        httpSettingMapper.deleteHttpSetting(settingId);
    }

    /**
     * 查询所有已启用邮箱地址
     *
     * @return 邮箱地址集合
     */
    @Override
    public ArrayList<String> findAllEmail() {
        return httpSettingMapper.selectAllEmail();
    }
}
