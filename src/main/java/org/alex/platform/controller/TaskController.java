package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.pojo.TaskDO;
import org.alex.platform.pojo.TaskDTO;
import org.alex.platform.pojo.TaskRefDO;
import org.alex.platform.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping("/task/list")
    public Result findTaskList(TaskDTO taskDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(taskService.findTaskList(taskDTO, num, size));
    }

    @GetMapping("/task/{taskId}")
    public Result findTaskById(@PathVariable  Integer taskId) {
        return Result.success(taskService.findTaskById(taskId));
    }

    @PostMapping("/task/save")
    public Result saveTaskAndRef(@RequestBody @Validated TaskRefDO taskRefDO) {
        Date date = new Date();
        taskRefDO.setSuiteType((byte) 0);
        taskRefDO.setCreatedTime(date);
        taskRefDO.setUpdateTime(date);
        taskService.saveTaskAndRef(taskRefDO);
        return Result.success("新增成功");
    }

    @PostMapping("/task/modify")
    public Result modifyTask(@Validated TaskDO taskDO) {
        taskDO.setUpdateTime(new Date());
        taskService.modifyTask(taskDO);
        return Result.success("修改成功");
    }

    @GetMapping("/task/remove/{taskId}")
    public Result removeTask(@PathVariable  Integer taskId) {
        taskService.removeTask(taskId);
        return Result.success("删除成功");
    }
}
