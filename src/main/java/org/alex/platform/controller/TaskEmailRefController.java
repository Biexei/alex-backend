package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.pojo.TaskEmailRefDO;
import org.alex.platform.pojo.TaskEmailRefDTO;
import org.alex.platform.service.TaskEmailRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskEmailRefController {
    @Autowired
    TaskEmailRefService taskEmailRefService;

    @PostMapping("/task/ref/save")
    public Result saveTaskEmailRef(TaskEmailRefDO taskEmailRefDO) {
        taskEmailRefService.saveTaskEmailRef(taskEmailRefDO);
        return Result.success("新增成功");
    }

    @GetMapping("/task/ref/remove")
    public Result removeTaskEmailRef(TaskEmailRefDTO taskEmailRefDTO) {
        taskEmailRefService.removeTaskEmailRef(taskEmailRefDTO);
        return Result.success("删除成功");
    }
}
