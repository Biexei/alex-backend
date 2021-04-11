package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.exception.ValidException;
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
import java.util.Date;
import java.util.regex.Pattern;

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
     * 获取配置项列表(超时 即type == 3 || type == 4)
     *
     * @param httpSettingDTO httpSettingDTO
     * @param pageNum        pageNum
     * @param pageSize       pageSize
     * @return PageInfo<HttpSettingVO>
     */
    @Override
    public PageInfo<HttpSettingVO> findHttpSettingTimeout(HttpSettingDTO httpSettingDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(httpSettingMapper.selectHttpSettingTimeout(httpSettingDTO));
    }

    /**
     * 新增配置项
     *
     * @param httpSettingDO httpSettingDO
     */
    @Override
    public void saveHttpSetting(HttpSettingDO httpSettingDO) throws ValidException {
        this.checkDO(httpSettingDO);
        Date date = new Date();
        httpSettingDO.setCreatedTime(date);
        httpSettingDO.setUpdateTime(date);
        Byte type = httpSettingDO.getType();
        if (type == 0 || type ==3 || type == 4) { // 若为代理/ConnectTimeout/ReadTimeout则将状态置为未启用
            httpSettingDO.setStatus((byte) 1);
        } else {
            httpSettingDO.setStatus((byte) 0);
        }
        httpSettingMapper.insertHttpSetting(httpSettingDO);
    }

    /**
     * 修改配置项
     *
     * @param httpSettingDO httpSettingDO
     */
    @Override
    public void modifyHttpSetting(HttpSettingDO httpSettingDO) throws ValidException {
        this.checkDO(httpSettingDO);
        httpSettingDO.setUpdateTime(new Date());
        // 如果是代理 ConnectTimeout ReadTimeout
        Integer settingId = httpSettingDO.getSettingId();
        HttpSettingVO httpSettingVO = httpSettingMapper.selectHttpSettingById(settingId);
        if (httpSettingVO != null) {
            Byte type = httpSettingVO.getType();
            Byte status = httpSettingDO.getStatus();
            if (status == 0) {
                if (type == 0) {
                    httpSettingMapper.closeOtherProxy(settingId);
                } else if (type == 3) {
                    httpSettingMapper.closeOtherConnectTimeout(settingId);
                } else if (type == 4) {
                    httpSettingMapper.closeOtherReadTimeout(settingId);
                }
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

    /**
     * 校验入参
     * @param httpSettingDO httpSettingDO
     * @throws ValidException 校验异常
     */
    private void checkDO(HttpSettingDO httpSettingDO) throws ValidException {
        Byte type = httpSettingDO.getType();
        String value = httpSettingDO.getValue();
        if (type == 0) {
            if (!Pattern.matches("[a-zA-z0-9\\.]+:(\\d+)$", value)) {
                throw new ValidException("代理服务器格式错误");
            }
        } else if (type == 2) { // 邮箱
            if (!Pattern.matches("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?", value)) {
                throw new ValidException("邮箱格式错误");
            }
        } else if (type == 3 || type == 4) { // 3ConnectTimeout 、 4ReadTimeout
            if (!Pattern.matches("(\\d+)$", value)) {
                throw new ValidException("超时时间格式错误");
            }
        } else {
            throw new ValidException("参数非法");
        }
    }
}
