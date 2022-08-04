package org.alex.platform.controller;

import com.alibaba.fastjson.JSONArray;
import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.UserDO;
import org.alex.platform.pojo.UserLoginLogDO;
import org.alex.platform.pojo.UserVO;
import org.alex.platform.service.RoleService;
import org.alex.platform.service.UserLoginLogService;
import org.alex.platform.service.UserService;
import org.alex.platform.util.MD5Util;
import org.alex.platform.util.NoUtil;
import org.alex.platform.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


@RestController
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    LoginUserInfo loginUserInfo;
    @Autowired
    RoleService roleService;
    @Autowired
    UserLoginLogService userLoginLogService;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    /**
     * 获取用户列表
     *
     * @param userDO   userDO
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return Result
     */
    @GetMapping("/user/list")
    @ResponseBody
    public Result users(UserDO userDO, Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        return Result.success(userService.findUserList(userDO, pageNum, pageSize));
    }

    /**
     * 用户信息详情
     *
     * @param userId 用户编号
     * @return Result
     */
    @GetMapping("/user/info/{userId}")
    @ResponseBody
    public Result user(@PathVariable Integer userId) {
        UserVO userVO = userService.findUserById(userId);
        return Result.success(userVO);
    }

    /**
     * 更新个人信息
     *
     * @param userDO userDO
     * @return Result
     */
    @PostMapping("/user/update")
    @ResponseBody
    public Result userUpdate(@Validated UserDO userDO) throws BusinessException {
        userService.modifyUser(userDO);
        return Result.success("更新成功");
    }

    /**
     * 修改密码
     * @param request request
     * @param oldPwd oldPwd
     * @param newPwd newPwd
     * @return Result
     * @throws BusinessException BusinessException
     */
    @PostMapping("/user/pwd/change")
    @ResponseBody
    public Result changePwd(HttpServletRequest request, String oldPwd, String newPwd) throws BusinessException {
        userService.changePwd(request, oldPwd, newPwd);
        return Result.success("更新成功");
    }

    /**
     * 新增用户
     *
     * @param userDO userDO
     * @return Result
     */
    @PostMapping("/user/save")
    @ResponseBody
    public Result saveUser(@Validated UserDO userDO) throws BusinessException {
        userService.saveUser(userDO);
        return Result.success("新增成功");
    }

    /**
     * 用户注册
     *
     * @param userDO userDO
     * @return Result
     */
    @PostMapping("/user/register")
    @ResponseBody
    public Result registerUser(@RequestBody UserDO userDO) throws BusinessException {
        userService.registerUser(userDO);
        return Result.success("注册成功");
    }

    /**
     * 用户登录
     *
     * @param username 账号
     * @param password 密码
     * @return Result
     */
    @PostMapping("/user/login")
    @ResponseBody
    public Result login(String username, String password, HttpServletRequest request) {
        if (null == username || "".equals(username) || null == password || "".equals(password)) {
            LOG.error("帐号名或者密码错误");
            return Result.fail("帐号名或者密码错误");
        }
        UserDO userDO = new UserDO();
        userDO.setUsername(username);
        userDO.setPassword(password);
        UserDO userInfo = userService.findUserToLogin(userDO);

        if (userInfo != null) {
            String token = UUID.randomUUID().toString();
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("token", token);
            userMap.put("userId", userInfo.getUserId());
            userMap.put("username", userInfo.getUsername());
            userMap.put("realName", userInfo.getRealName());
            userMap.put("isEnable", userInfo.getIsEnable());
            Integer roleId = userInfo.getRoleId();
            // 写入权限
            JSONArray permissionCodeArray = roleService.findPermissionCodeArrayByRoleId(roleId);
            userMap.put("permission", permissionCodeArray);
            // 写入token
            redisUtil.set(token, userMap, 60*30);
            // 写入登录日志
            UserLoginLogDO userLoginLogDO = new UserLoginLogDO();
            userLoginLogDO.setUserId(userInfo.getUserId());
            userLoginLogDO.setUserName(userInfo.getUsername());
            userLoginLogDO.setIp(userLoginLogService.getIpAddr(request));
            userLoginLogDO.setLoginTime(new Date());
            userLoginLogService.saveUserLoginLog(userLoginLogDO);
            return Result.success("登录成功", userMap);
        } else {
            LOG.error("帐号名或者密码错误");
            return Result.fail("帐号名或者密码错误");
        }
    }

    /**
     * 删除用户
     *
     * @param userId 用户编号
     * @param request request
     * @return Result
     */
    @GetMapping("user/remove/{userId}")
    @ResponseBody
    public Result removeUser(HttpServletRequest request, @PathVariable Integer userId) throws BusinessException {
        userService.removeUserById(request, userId);
        return Result.success("删除成功");
    }

    /**
     * 退出登录
     * @param request 请求对象
     * @return Result
     */
    @GetMapping("/user/logout")
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader("Token");
        redisUtil.del(token);
        return Result.success("退出成功");
    }

    /**
     * 重置密码
     * @param userId 用户编号
     * @return Result
     * @throws BusinessException BusinessException
     */
    @GetMapping("/user/reset/{userId}")
    public Result pwdReset(@PathVariable Integer userId) throws BusinessException {
        userService.pwdReset(userId, MD5Util.md5ForLoginPassword(NoUtil.DEFAULT_PWD));
        return Result.success("操作成功");
    }
}
