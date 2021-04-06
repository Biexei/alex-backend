package org.alex.platform.task;

import org.alex.platform.mapper.TaskMapper;
import org.alex.platform.pojo.TaskDTO;
import org.alex.platform.pojo.TaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 启动应用时手动唤醒已启用的定时任务
 */
@Component
public class TaskStarter implements CommandLineRunner {
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    TaskRegistrar taskRegistrar;

    @Override
    public void run(String... args) throws Exception {
        startIfSuiteTask();
    }

    /**
     * 启动所有接口测试套件定时任务
     */
    private void startIfSuiteTask() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setStatus((byte)0); //状态 0启用
        taskDTO.setSuiteType((byte)0); //类型 0接口
        List<TaskVO> taskList = taskMapper.selectTaskList(taskDTO);
        taskList.parallelStream().forEach(taskVO -> {
            // 将每个任务加入定时任务池
            Integer taskId = taskVO.getTaskId();
            String cron = taskVO.getCron();
            TaskRunnable taskRunnable = new TaskRunnable("taskCenter", "executeInterfaceCaseSuite", taskId);
            taskRegistrar.save(taskRunnable, cron);
        });
    }
}
