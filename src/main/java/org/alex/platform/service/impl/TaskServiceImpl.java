package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.alex.platform.mapper.TaskEmailRefMapper;
import org.alex.platform.mapper.TaskMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.TaskEmailRefService;
import org.alex.platform.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    TaskEmailRefService taskEmailRefService;

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
        ArrayList<String> emailList = taskRefDO.getEmailList();
        if (emailList != null) {
            for (String email:
                    emailList) {
                TaskEmailRefDO taskEmailRefDO = new TaskEmailRefDO();
                taskEmailRefDO.setEmailAddress(email);
                taskEmailRefDO.setTaskId(taskId);
                taskEmailRefService.saveTaskEmailRef(taskEmailRefDO);
            }
        }
    }

    @Override
    public void modifyTaskAndRef(TaskRefDO taskRefDO) {
        TaskDO taskDO = new TaskDO();
        Integer taskId = taskRefDO.getTaskId();
        taskDO.setTaskId(taskId);
        taskDO.setDesc(taskRefDO.getDesc());
        taskDO.setCron(taskRefDO.getCron());
        taskDO.setDesc(taskRefDO.getDesc());
        taskDO.setStatus(taskRefDO.getStatus());
        taskDO.setSuiteType(taskRefDO.getSuiteType());
        taskDO.setSuiteId(taskRefDO.getSuiteId());
        taskDO.setUpdateTime(taskRefDO.getUpdateTime());
        // 修改主表
        taskMapper.updateTask(taskDO);
        // 删除附表所有邮件地址
        TaskEmailRefDTO taskEmailRefDTO = new TaskEmailRefDTO();
        taskEmailRefDTO.setTaskId(taskId);
        taskEmailRefService.removeTaskEmailRef(taskEmailRefDTO);
        // 修改附表
        if (taskRefDO.getEmailList() != null) {
            ArrayList<String> emailAddressList = taskRefDO.getEmailList();
            for (String email: emailAddressList) {
                TaskEmailRefDO taskEmailRefDO = new TaskEmailRefDO();
                taskEmailRefDO.setTaskId(taskId);
                taskEmailRefDO.setEmailAddress(email);
                taskEmailRefService.saveTaskEmailRef(taskEmailRefDO);
            }
        }
    }

    @Override
    public void removeTask(Integer taskId) {
        // 删除主表
        taskMapper.deleteTask(taskId);
        // 删除关联表
        TaskEmailRefDTO taskEmailRefDTO = new TaskEmailRefDTO();
        taskEmailRefDTO.setTaskId(taskId);
        taskEmailRefService.removeTaskEmailRef(taskEmailRefDTO);
    }
}
