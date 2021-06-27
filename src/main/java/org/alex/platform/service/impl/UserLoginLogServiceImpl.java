package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.mapper.UserLoginLogMapper;
import org.alex.platform.pojo.UserLoginLogDO;
import org.alex.platform.pojo.UserLoginLogDTO;
import org.alex.platform.pojo.UserLoginLogVO;
import org.alex.platform.service.UserLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserLoginLogServiceImpl implements UserLoginLogService {
    @Autowired
    UserLoginLogMapper userLoginLogMapper;
    @Autowired
    LoginUserInfo loginUserInfo;
    /**
     * 查询用户登录日志列表
     * @param userLoginLogDTO 查询表单对象
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return PageInfo<UserLoginLogVO>
     */
    @Override
    public PageInfo<UserLoginLogVO> findUserLoginLogList(UserLoginLogDTO userLoginLogDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(userLoginLogMapper.selectUserLoginLogList(userLoginLogDTO));
    }

    /**
     * 新增用户登录日志
     * @param userLoginLogDO 登录日志表对象
     */
    @Override
    public void saveUserLoginLog(UserLoginLogDO userLoginLogDO) {
        userLoginLogMapper.insertUserLoginLog(userLoginLogDO);
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     *
     * @return ip
     */
    @Override
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if(ip.contains(",")){
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
