package org.alex.platform.task;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.pojo.InterfaceSuiteLogVO;
import org.alex.platform.pojo.TaskVO;
import org.alex.platform.service.InterfaceSuiteCaseRefService;
import org.alex.platform.service.InterfaceSuiteLogService;
import org.alex.platform.service.MailService;
import org.alex.platform.service.TaskService;
import org.alex.platform.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 定时任务调度中心
 */
@Component("taskCenter")
public class TaskCenter {

    private static final Logger LOG = LoggerFactory.getLogger(TaskCenter.class);
    @Autowired
    TaskService taskService;
    @Autowired
    InterfaceSuiteCaseRefService interfaceSuiteCaseRefService;
    @Autowired
    MailService mailService;
    @Autowired
    InterfaceSuiteLogService suiteLogService;

    /**
     * 执行接口测试套件
     * @param taskId taskId
     */
    public void executeInterfaceCaseSuite(Integer taskId) {
        TaskVO taskVO = taskService.findTaskById(taskId);
        String time = null;
        String suiteLogNo = null;
        if (taskVO.getSuiteType() == 0) { // 接口
            Integer suiteId = taskVO.getSuiteId();
            List<String> emailList = taskVO.getEmailList();
            // 执行测试套件
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time = format.format(new Date());
                suiteLogNo = interfaceSuiteCaseRefService.executeSuiteCaseById(suiteId, "定时任务");
                LOG.info("定时任务测试套件执行完毕，suiteId={}", suiteId);
            } catch (BusinessException e) {
                e.printStackTrace();
                LOG.error("定时任务测试套件执行失败，suiteId={}，errorMsg={}", suiteId, ExceptionUtil.msg(e));
            }
            // 发送邮件
            if (emailList != null) {
                String[] emails = new String[emailList.size()];
                emailList.toArray(emails);
                if (emails.length > 0) {
                    try {
                        InterfaceSuiteLogVO suiteLog = suiteLogService.findIfSuiteLogByNo(suiteLogNo);
                        Long runTime = suiteLog.getRunTime();
                        Integer totalCase = suiteLog.getTotalCase();
                        Integer totalSuccess = suiteLog.getTotalSuccess();
                        Integer totalFailed = suiteLog.getTotalFailed();
                        Integer totalError= suiteLog.getTotalError();
                        String successRate = totalSuccess/totalCase*100 + "%";
                        String title = "Alex定时任务通知";
                        String text = String.format("1.执行时间：%s\r\n2.耗时：%s\r\n3.执行编号：%s\r\n" +
                                "4.执行类型：接口测试套件\r\n" + "5.总用例数：%s\r\n6.成功数：%s\r\n7.失败数：%s\r\n" +
                                "8.错误数：%s\r\n9.成功率：%s",
                                time, runTime, suiteLogNo, totalCase, totalSuccess, totalFailed, totalError, successRate);
                        mailService.send(title, text, emails);
                        LOG.info("定时任务执行邮件发送成功");
                    } catch (BusinessException e) {
                        LOG.error("定时任务执行邮件发送失败，errorMsg={}", e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
