package org.alex.platform.common;

import org.alex.platform.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginUserInfo {
    @Autowired
    RedisUtil redisUtil;
    public Object getLoginUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Token");
        if (token == null) {
            token = "";
        }
        return redisUtil.get(token);
    }
}
