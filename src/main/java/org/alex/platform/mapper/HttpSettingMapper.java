package org.alex.platform.mapper;

import org.alex.platform.pojo.HttpSettingDO;
import org.alex.platform.pojo.HttpSettingDTO;
import org.alex.platform.pojo.HttpSettingVO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public interface HttpSettingMapper {
    HttpSettingVO selectHttpSettingById(Integer settingId);

    List<HttpSettingVO> selectHttpSetting(HttpSettingDTO httpSettingDTO);

    List<HttpSettingVO> selectHttpSettingTimeout(HttpSettingDTO httpSettingDTO);

    void insertHttpSetting(HttpSettingDO httpSettingDO);

    void updateHttpSetting(HttpSettingDO httpSettingDO);

    void deleteHttpSetting(Integer settingId);

    void closeOtherProxy(Integer settingId);

    void closeOtherConnectTimeout(Integer settingId);

    void closeOtherReadTimeout(Integer settingId);

    ArrayList<String> selectAllEmail();
}
