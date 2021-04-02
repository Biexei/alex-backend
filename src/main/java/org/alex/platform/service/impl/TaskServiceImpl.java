package org.alex.platform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.alex.platform.mapper.TaskMapper;
import org.alex.platform.pojo.*;
import org.alex.platform.service.TaskEmailRefService;
import org.alex.platform.service.TaskService;
import org.alex.platform.task.TaskCenter;
import org.alex.platform.task.TaskRegistrar;
import org.alex.platform.task.TaskRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    TaskEmailRefService taskEmailRefService;
    @Autowired
    TaskRegistrar taskRegistrar;
    @Autowired
    TaskCenter taskCenter;
    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    /**
     * 查看定时任务列表
     * @param taskDTO taskDTO
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @return PageInfo<TaskVO>
     */
    @Override
    public PageInfo<TaskVO> findTaskList(TaskDTO taskDTO, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(taskMapper.selectTaskList(taskDTO));
    }

    /**
     * 查看定时任务详情
     * @param taskId 编号
     * @return TaskVO
     */
    @Override
    public TaskVO findTaskById(Integer taskId) {
        return taskMapper.selectTaskById(taskId);
    }

    /**
     * 新增定时任务
     * @param taskDO taskDO
     * @return TaskDO
     */
    @Override
    public TaskDO saveTask(TaskDO taskDO) {
        // 插入主表
        taskMapper.insertTask(taskDO);
        return taskDO;
    }

    /**
     * 新增定时任务主表和附表
     * @param taskRefDO taskRefDO
     */
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
        // 执行定时任务
        String cron = taskDO.getCron();
        Byte status = taskDO.getStatus();
        Byte suiteType = taskDO.getSuiteType();
        if (suiteType == 0) { //接口
            if (status == 0) { //启用
                TaskRunnable taskRunnable = new TaskRunnable("taskCenter", "executeInterfaceCaseSuite", taskId);
                taskRegistrar.save(taskRunnable, cron);
            }
        }
    }

    /**
     * 修改定时任务主表和附表
     * @param taskRefDO taskRefDO
     */
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
        // 执行定时任务
        String cron = taskDO.getCron();
        Byte status = taskDO.getStatus();
        Byte suiteType = taskDO.getSuiteType();
        if (suiteType == 0) { //接口
            TaskRunnable taskRunnable = new TaskRunnable("taskCenter", "executeInterfaceCaseSuite", taskId);
            if (status == 0) { //启用
                taskRegistrar.modify(taskRunnable, cron);
            } else { //禁用则移除定时任务
                taskRegistrar.remove(taskRunnable);
            }
        }
    }

    /**
     * 删除定时任务主表和附表
     * @param taskId 编号
     */
    @Override
    public void removeTask(Integer taskId) {
        // 删除主表
        taskMapper.deleteTask(taskId);
        // 删除关联表
        TaskEmailRefDTO taskEmailRefDTO = new TaskEmailRefDTO();
        taskEmailRefDTO.setTaskId(taskId);
        taskEmailRefService.removeTaskEmailRef(taskEmailRefDTO);
        // 删除定时任务
        TaskRunnable taskRunnable = new TaskRunnable("taskCenter", "executeInterfaceCaseSuite", taskId);
        taskRegistrar.remove(taskRunnable);
    }

    /**
     * 手动执行一次定时任务
     * @param taskId 任务id
     */
    @Override
    public void executeTask(Integer taskId) {
        TaskVO task = findTaskById(taskId);
        Byte type = task.getSuiteType();
        if (type == 0) { //接口
            taskCenter.executeInterfaceCaseSuite(taskId);
        }
    }
}
