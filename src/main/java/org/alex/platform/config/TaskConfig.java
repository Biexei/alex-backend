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
import org.alex.platform.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(TaskConfig.class);

    /**
     * 定时任务
     *
     * @param scheduledTaskRegistrar SpringBoot
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        LOG.info("---------------------------------------定时任务开始调度---------------------------------------");
        TaskDTO taskDTO = new TaskDTO();
        List<TaskVO> taskList = taskMapper.selectTaskList(taskDTO);
        for (TaskVO taskVO : taskList) {
            if (taskVO.getStatus() == 0) {
                scheduledTaskRegistrar.addTriggerTask(
                        // 执行内容
                        () -> {
                            Integer suiteId = taskVO.getSuiteId();
                            List<String> emailList = taskVO.getEmailList();
                            // 执行测试套件
                            try {
                                interfaceSuiteCaseRefService.executeSuiteCaseById(suiteId, "定时任务");
                                LOG.info("定时任务测试套件执行完毕，suiteId={}", suiteId);
                            } catch (BusinessException e) {
                                e.printStackTrace();
                                LOG.error("定时任务测试套件执行失败，suiteId={}，errorMsg={}", suiteId, ExceptionUtil.msg(e));
                            }
                            // 发送邮件
                            if (emailList != null) {
                                String[] emails = new String[emailList.size()];
                                emailList.toArray(emails);
                                try {
                                    mailService.send("测试套件执行定时任务执行完成", "测试套件执行定时任务执行完成", emails);
                                    LOG.info("定时任务执行邮件发送成功");
                                } catch (BusinessException e) {
                                    LOG.error("定时任务执行邮件发送失败，errorMsg={}", e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        },
                        // 执行周期
                        triggerContext -> {
                            String cron = taskVO.getCron();
                            Integer taskId = taskVO.getTaskId();
                            CronTrigger cronTrigger = new CronTrigger(cron);
                            Date date = cronTrigger.nextExecutionTime(triggerContext);
                            LOG.info("定时任务执行完成，taskId={}，cron={}", taskId, cron);
                            TaskDO taskDO = new TaskDO();
                            taskDO.setNextTime(date);
                            taskDO.setTaskId(taskId);
                            taskDO.setUpdateTime(new Date());
                            taskMapper.updateTaskNextTime(taskDO);
                            return date;
                        }
                );
            }
        }
    }
}
