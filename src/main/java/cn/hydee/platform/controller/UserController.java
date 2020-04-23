package cn.hydee.platform.controller;

import cn.hydee.platform.common.Result;
import cn.hydee.platform.pojo.User;
import cn.hydee.platform.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/user/list")
    @ResponseBody
    public Result users(Integer pageNum, Integer pageSize){
        pageNum = pageNum==null?1:pageNum;
        pageSize = pageSize==null?10:pageSize;
        PageInfo pageInfo = userService.listUsers(pageNum, pageSize);
        return Result.success(pageInfo.getList());
    }

    @GetMapping("/user/info/{userId}")
    @ResponseBody
    public Result users(@PathVariable Integer userId){
        User userInfo = userService.getUserById(userId);
        return Result.success(userInfo);
    }

    @PostMapping("/user/update")
    @ResponseBody
    public Result userUpdate(User user){
        return Result.success(userService.updateUser(user));
    }

    @PostMapping("/user/register")
    @ResponseBody
    public Result register(User user){
        String username = user.getUsername();
        String password = user.getPassword();
        Integer jobNumber = user.getJobNumber();
        Byte sex = user.getSex();
        if (username==null || password==null || jobNumber==null || sex==null){
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
