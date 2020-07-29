package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.pojo.User;
import org.alex.platform.service.UserService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public Result users(User user, Integer pageNum, Integer pageSize){
        pageNum = pageNum==null?1:pageNum;
        pageSize = pageSize==null?10:pageSize;
        PageInfo pageInfo = userService.findUserList(user, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 用户信息.getList()
     * @param userId
     * @return
     */
    @GetMapping("/user/info/{userId}")
    @ResponseBody
    public Result user(@PathVariable Integer userId){
        User userInfo = userService.findUserById(userId);
        return Result.success(userInfo);
    }

    /**
     * 更新个人信息
     * @param user
     * @return
     */
    @PostMapping("/user/update")
    @ResponseBody
    public Result userUpdate(@Validated User user){
        userService.modifyUser(user);
        return Result.success("更新成功");
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/user/register")
    @ResponseBody
    public Result register(@Validated User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        Byte sex = user.getSex();
        Date date = new Date();
        user.setCreatedTime(date);
        user.setUpdateTime(date);
        if (username==null || password==null || sex==null){
            LOG.error("请完善注册信息");
            return Result.fail("请完善注册信息");
        } else{
            if(userService.saveUser(user)){
                return Result.success("注册成功");
            } else{
                LOG.error("用户名已存在");
                return Result.fail("用户名已存在");
            }
        }
    }
    @PostMapping("/user/login")
    @ResponseBody
    public Result login(User user){
        String username = user.getUsername();
        String password = user.getPassword();
        if (null == username || "".equals(username) ||null == password || "".equals(password)){
            LOG.error("帐号名或者密码错误");
            return Result.fail("帐号名或者密码错误");
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        if (userService.findUserToLogin(user) != null){
            return Result.success("登录成功");
        } else {
            LOG.error("帐号名或者密码错误");
            return Result.fail("帐号名或者密码错误");
        }
    }
}
