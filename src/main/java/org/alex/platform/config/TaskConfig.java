package org.alex.platform.config;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.SqlException;
import org.alex.platform.mapper.TaskMapper;
import org.alex.platform.pojo.TaskDO;
import org.alex.platform.pojo.TaskDTO;
import org.alex.platform.pojo.TaskVO;
import org.alex.platform.service.InterfaceSuiteCaseRefService;
import org.alex.platform.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Date;
import java.util.List;

@EnableScheduling
@Configuration
public class TaskConfig implements SchedulingConfigurer {
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    InterfaceSuiteCaseRefService interfaceSuiteCaseRefService;
    @Autowired
    MailService mailService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        TaskDTO taskDTO = new TaskDTO();
        List<TaskVO> taskList = taskMapper.selectTaskList(taskDTO);
        for (TaskVO taskVO : taskList) {
            if (taskVO.getStatus() == 0){
                scheduledTaskRegistrar.addTriggerTask(
                        // 执行内容
                        () -> {
                            Integer suiteId = taskVO.getSuiteId();
                            List<String> emailList = taskVO.getEmailList();
                            // 执行测试套件
                            try {
                                interfaceSuiteCaseRefService.executeSuiteCaseById(suiteId);
                            } catch (ParseException | BusinessException | SqlException e) {
                                e.printStackTrace();
                            }
                            // 发送邮件
                            if (emailList != null) {
                                String[] emails = new String[emailList.size()];
                                emailList.toArray(emails);
                                try {
                                    mailService.send("测试套件执行定时任务执行完成", "测试套件执行定时任务执行完成", emails);
                                } catch (BusinessException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        // 执行周期
                        triggerContext -> {
                            String cron = taskVO.getCron();
                            CronTrigger cronTrigger = new CronTrigger(cron);
                            Date date = cronTrigger.nextExecutionTime(triggerContext);
                            TaskDO taskDO = new TaskDO();
                            taskDO.setNextTime(date);
                            taskDO.setTaskId(taskVO.getTaskId());
                            taskDO.setUpdateTime(new Date());
                            taskMapper.updateTaskNextTime(taskDO);
                            return date;
                        }
                );
            }
        }
    }
}
