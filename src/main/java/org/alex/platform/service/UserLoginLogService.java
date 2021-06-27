package org.alex.platform.service;

import com.github.pagehelper.PageInfo;
import org.alex.platform.pojo.UserLoginLogDO;
import org.alex.platform.pojo.UserLoginLogDTO;
import org.alex.platform.pojo.UserLoginLogVO;

import javax.servlet.http.HttpServletRequest;

public interface UserLoginLogService {
    PageInfo<UserLoginLogVO> findUserLoginLogList(UserLoginLogDTO userLoginLogDTO, Integer pageNum, Integer pageSize);

    void saveUserLoginLog(UserLoginLogDO userLoginLogDO);

    String getIpAddr(HttpServletRequest request);
}
