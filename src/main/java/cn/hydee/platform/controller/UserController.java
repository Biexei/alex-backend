package cn.hydee.platform.controller;

import cn.hydee.platform.common.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    @GetMapping("/login")
    @ResponseBody
    public Object login(@RequestParam(required=false) String username, String password){
        return Result.fail();
    }
}
