package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.pojo.UserDO;
import org.alex.platform.service.UserService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@RestController
public class UserController {

    @Autowired
    UserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    /**
     * 获取用户列表
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
     * 用户信息.getList()
     *
     * @param userId
     * @return
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
     * @param userDO
     * @return
     */
    @PostMapping("/user/update")
    @ResponseBody
    public Result userUpdate(@Validated UserDO userDO) {
        userDO.setUpdateTime(new Date());
        userService.modifyUser(userDO);
        return Result.success("更新成功");
    }

    /**
     * 用户注册
     *
     * @param userDO
     * @return
     */
    @PostMapping("/user/register")
    @ResponseBody
    public Result register(@Validated UserDO userDO) {
        String username = userDO.getUsername();
        String password = userDO.getPassword();
        Byte sex = userDO.getSex();
        Date date = new Date();
        userDO.setCreatedTime(date);
        userDO.setUpdateTime(date);
        if (username == null || password == null || sex == null) {
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

    @PostMapping("/user/login")
    @ResponseBody
    public Result login(@RequestBody String username, String password) {
        if (null == username || "".equals(username) || null == password || "".equals(password)) {
            LOG.error("帐号名或者密码错误");
            return Result.fail("帐号名或者密码错误");
        }
        UserDO userDO = new UserDO();
        userDO.setUsername(username);
        userDO.setPassword(password);
        if (userService.findUserToLogin(userDO) != null) {
            return Result.success("登录成功");
        } else {
            LOG.error("帐号名或者密码错误");
            return Result.fail("帐号名或者密码错误");
        }
    }
}
