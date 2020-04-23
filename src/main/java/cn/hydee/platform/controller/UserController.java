package cn.hydee.platform.controller;

import cn.hydee.platform.common.Result;
import cn.hydee.platform.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    @ResponseBody
    public Result login(@RequestParam(required=false) String username, String password){
        ArrayList list = userService.getAllUser();
        return Result.success(list);
    }
}
