package org.alex.platform.controller;

import org.alex.platform.common.LoginUserInfo;
import org.alex.platform.common.Result;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.UserDO;
import org.alex.platform.service.UserService;
import com.github.pagehelper.PageInfo;
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
        PageInfo pageInfo = userService.findUserList(userDO, pageNum, pageSize);
        return Result.success(pageInfo);
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
        UserDO userDOInfo = userService.findUserById(userId);
        return Result.success(userDOInfo);
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
        Integer userId = userDO.getUserId();
        UserDO user = userService.findUserById(userId);
        if (user.getRoleId() == 0) {
            throw new BusinessException("超级管理员禁止修改");
        }
        userDO.setUpdateTime(new Date());
        userService.modifyUser(userDO);
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
    public Result saveUser(@Validated UserDO userDO) {
        String username = userDO.getUsername();
        String password = userDO.getPassword();
        String realName = userDO.getRealName();
        Byte sex = userDO.getSex();
        Date date = new Date();
        userDO.setCreatedTime(date);
        userDO.setUpdateTime(date);
        userDO.setIsEnable((byte) 1);
        userDO.setRoleId(2);
        if (username == null || password == null || sex == null || realName == null) {
            LOG.error("请完善注册信息");
            return Result.fail("请完善注册信息");
        } else {
            if (userService.saveUser(userDO)) {
                return Result.success("注册成功");
            } else {
                LOG.error("用户名已存在");
                return Result.fail("用户名已存在");
            }
        }
    }

    /**
     * 用户注册
     *
     * @param userDO userDO
     * @return Result
     */
    @PostMapping("/user/register")
    @ResponseBody
    public Result registerUser(@Validated UserDO userDO) {
        String username = userDO.getUsername();
        String password = userDO.getPassword();
        String realName = userDO.getRealName();
        Date date = new Date();
        userDO.setCreatedTime(date);
        userDO.setUpdateTime(date);
        userDO.setIsEnable((byte) 1);
        userDO.setRoleId(2);
        userDO.setSex((byte) 1);
        if (username == null || password == null || realName == null) {
            LOG.error("请完善注册信息");
            return Result.fail("请完善注册信息");
        } else {
            if (userService.saveUser(userDO)) {
                return Result.success("注册成功");
            } else {
                LOG.error("用户名已存在");
                return Result.fail("用户名已存在");
            }
        }
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
    public Result login(String username, String password) {
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
            redisUtil.set(token, userMap, 60*30);
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
     * @return Result
     */
    @GetMapping("user/remove/{userId}")
    @ResponseBody
    public Result removeUser(@PathVariable Integer userId) throws BusinessException {
        UserDO user = userService.findUserById(userId);
        if (user.getRoleId() == 0) {
            throw new BusinessException("超级管理员禁止删除");
        }
        userService.removeUserById(userId);
        return Result.success();
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
}
