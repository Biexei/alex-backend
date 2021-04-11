package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.ValidException;
import org.alex.platform.pojo.HttpSettingDO;
import org.alex.platform.pojo.HttpSettingDTO;
import org.alex.platform.pojo.HttpSettingVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface HttpSettingService {
    HttpSettingVO findHttpSettingById(Integer settingId);

    PageInfo<HttpSettingVO> findHttpSetting(HttpSettingDTO httpSettingDTO, Integer pageNum, Integer pageSize);

    PageInfo<HttpSettingVO> findHttpSettingTimeout(HttpSettingDTO httpSettingDTO, Integer pageNum, Integer pageSize);

    void saveHttpSetting(HttpSettingDO httpSettingDO) throws ValidException;

    void modifyHttpSetting(HttpSettingDO httpSettingDO) throws ValidException;

    void removeHttpSetting(Integer settingId);

    ArrayList<String> findAllEmail();

}
