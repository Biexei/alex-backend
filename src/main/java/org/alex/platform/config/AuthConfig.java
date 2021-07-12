package org.alex.platform.config;

import org.alex.platform.exception.LoginException;
import org.alex.platform.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
    @Autowired
    RedisUtil redisUtil;

    /**
     * 用户登录拦截器
     * @param registry registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptor = registry.addInterceptor(new HandlerInterceptor() {
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws LoginException {
                if (!request.getMethod().equalsIgnoreCase("OPTIONS")) {
                    String token = request.getHeader("Token");
                    if ("".equals(token) || null == token) {
                        throw new LoginException("登录已经过期，请重新登录");
                    }
                    Object userInfo = redisUtil.get(token);
                    if (null == userInfo) {
                        throw new LoginException("登录已经过期，请重新登录");
                    }
                    return true;
                }
                return true;
            }
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                String token = request.getHeader("Token");
                if (token != null) {
                    redisUtil.expire(token, 30*60);
                }
            }
        });
        interceptor.addPathPatterns("/**").excludePathPatterns("/user/login", "/user/register", "/interface/template/download/*", "/stability/log/download/*");
    }
}
