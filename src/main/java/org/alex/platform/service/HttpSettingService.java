package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.pojo.HttpSettingDO;
import org.alex.platform.pojo.HttpSettingDTO;
import org.alex.platform.pojo.HttpSettingVO;

import java.util.List;

public interface HttpSettingService {
    HttpSettingVO findHttpSettingById(Integer settingId);

    PageInfo<HttpSettingVO> findHttpSetting(HttpSettingDTO httpSettingDTO, Integer pageNum, Integer pageSize);

    void saveHttpSetting(HttpSettingDO httpSettingDO);

    void modifyHttpSetting(HttpSettingDO httpSettingDO);

    void removeHttpSetting(Integer settingId);
}
