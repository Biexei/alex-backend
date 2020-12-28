package org.alex.platform.common;

import org.alex.platform.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Component
public class LoginUserInfo {
    @Autowired
    RedisUtil redisUtil;

    /**
     * 获取登录用户信息
     * @param request request
     * @return 用户信息
     */
    public Object getLoginUserInfo(HttpServletRequest request) {
        String token = request.getHeader("Token");
        if (token == null) {
            token = "";
        }
        return redisUtil.get(token);
    }

    /**
     * 获取登录用户real name
     * @param request request
     * @return 真实姓名
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String getRealName(HttpServletRequest request) {
        HashMap<String, Object> map = (HashMap)getLoginUserInfo(request);
        String executor;
        try {
            executor = map.get("realName").toString();
        } catch (Exception e) {
            executor = "";
        }
        return executor;
    }
}
