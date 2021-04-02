package org.alex.platform.controller;

import com.alibaba.fastjson.JSON;
import org.alex.platform.common.Result;
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

    /**
     * 获取定时任务列表
     *
     * @param taskDTO  taskDTO
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @return Result
     */
    @GetMapping("/task/list")
    public Result findTaskList(TaskDTO taskDTO, Integer pageNum, Integer pageSize) {
        int num = pageNum == null ? 1 : pageNum;
        int size = pageSize == null ? 10 : pageSize;
        return Result.success(taskService.findTaskList(taskDTO, num, size));
    }

    /**
     * 获取定时任务详情
     *
     * @param taskId 任务编号
     * @return Result
     */
    @GetMapping("/task/{taskId}")
    public Result findTaskById(@PathVariable Integer taskId) {
        return Result.success(taskService.findTaskById(taskId));
    }

    /**
     * 手动触发执行一次定时任务内容
     *
     * @param taskId 任务编号
     * @return Result
     */
    @GetMapping("/task/execute/{taskId}")
    public Result executeTask(@PathVariable Integer taskId) {
        taskService.executeTask(taskId);
        return Result.success("执行成功");
    }

    /**
     * 新增定时任务
     *
     * @param taskRefDO taskRefDO
     * @return Result
     */
    @PostMapping("/task/save")
    public Result saveTaskAndRef(@RequestBody @Validated TaskRefDO taskRefDO) {
        Date date = new Date();
        taskRefDO.setSuiteType((byte) 0);
        taskRefDO.setCreatedTime(date);
        taskRefDO.setUpdateTime(date);
        taskService.saveTaskAndRef(taskRefDO);
        return Result.success("新增成功");
    }

    /**
     * 修改定时任务
     *
     * @param taskRefDO taskRefDO
     * @return Result
     */
    @PostMapping("/task/modify")
    public Result modifyTask(@RequestBody @Validated TaskRefDO taskRefDO) {
        taskRefDO.setUpdateTime(new Date());
        taskService.modifyTaskAndRef(taskRefDO);
        return Result.success("修改成功");
    }

    /**
     * 删除定时任务
     *
     * @param taskId 任务编号
     * @return Result
     */
    @GetMapping("/task/remove/{taskId}")
    public Result removeTask(@PathVariable Integer taskId) {
        taskService.removeTask(taskId);
        return Result.success("删除成功");
    }
}
