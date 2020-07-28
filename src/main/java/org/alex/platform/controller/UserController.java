package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.pojo.User;
import org.alex.platform.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {
    @Autowired
    UserService userService;

    /**
     * 获取用户列表
     */
    @GetMapping("/user/list")
    @ResponseBody
    public Result users(Integer pageNum, Integer pageSize){
        pageNum = pageNum==null?1:pageNum;
        pageSize = pageSize==null?10:pageSize;
        PageInfo pageInfo = userService.listUsers(pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 用户信息.getList()
     * @param userId
     * @return
     */
    @GetMapping("/user/info/{userId}")
    @ResponseBody
    public Result users(@PathVariable Integer userId){
        User userInfo = userService.getUserById(userId);
        return Result.success(userInfo);
    }

    /**
     * 更新个人信息
     * @param user
     * @return
     */
    @PostMapping("/user/update")
    @ResponseBody
    public Result userUpdate(User user){
        return Result.success(userService.updateUser(user));
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/user/register")
    @ResponseBody
    public Result register(User user){
        String username = user.getUsername();
        String password = user.getPassword();
        Integer jobNumber = user.getJobNumber();
        Byte sex = user.getSex();
        if (username==null || password==null || sex==null){
            return Result.fail("请完善注册信息");
        } else{
            if(userService.saveUser(user)){
                return Result.success();
            } else{
                return Result.fail("用户名或工号已存在");
            }
        }
    }
}
