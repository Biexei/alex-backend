package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javafx.concurrent.Task;
import org.alex.platform.mapper.TaskEmailRefMapper;
import org.alex.platform.mapper.TaskMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    TaskEmailRefMapper taskEmailRefMapper;

    @Override
    public PageInfo<TaskVO> findTaskList(TaskDTO taskDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(taskMapper.selectTaskList(taskDTO));
    }

    @Override
    public TaskVO findTaskById(Integer taskId) {
        return taskMapper.selectTaskById(taskId);
    }

    @Override
    public TaskDO saveTask(TaskDO taskDO) {
        // 插入主表
        taskMapper.insertTask(taskDO);
        return taskDO;
    }

    @Override
    public void saveTaskAndRef(TaskRefDO taskRefDO) {
        TaskDO taskDO = new TaskDO();
        taskDO.setDesc(taskRefDO.getDesc());
        taskDO.setCron(taskRefDO.getCron());
        taskDO.setDesc(taskRefDO.getDesc());
        taskDO.setStatus(taskRefDO.getStatus());
        taskDO.setSuiteType(taskRefDO.getSuiteType());
        taskDO.setSuiteId(taskRefDO.getSuiteId());
        taskDO.setCreatedTime(taskRefDO.getCreatedTime());
        taskDO.setUpdateTime(taskRefDO.getUpdateTime());
        // 插入主表
        this.saveTask(taskDO);
        Integer taskId = taskDO.getTaskId();
        // 插入关联表
        ArrayList<Integer> emailList = taskRefDO.getEmailId();
        for (int emailId:
             emailList) {
            TaskEmailRefDO taskEmailRefDO = new TaskEmailRefDO();
            taskEmailRefDO.setEmailId(emailId);
            taskEmailRefDO.setTaskId(taskId);
            taskEmailRefMapper.insertTaskEmailRef(taskEmailRefDO);
        }
    }

    @Override
    public void modifyTask(TaskDO taskDO) {
        taskMapper.updateTask(taskDO);
    }

    @Override
    public void removeTask(Integer taskId) {
        // 删除主表
        taskMapper.deleteTask(taskId);
        // 删除关联表
        TaskEmailRefDTO taskEmailRefDTO = new TaskEmailRefDTO();
        taskEmailRefDTO.setTaskId(taskId);
        taskEmailRefMapper.deleteTaskEmailRef(taskEmailRefDTO);
    }
}
