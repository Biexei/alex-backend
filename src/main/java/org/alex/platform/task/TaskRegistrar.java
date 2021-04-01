package org.alex.platform.task;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 添加定时任务注册类，用来增加、删除定时任务
 */
@Component
public class TaskRegistrar implements DisposableBean {

    private Map<Runnable, TaskFuture> scheduledTasks = new ConcurrentHashMap<>(16);

    @Autowired
    private TaskScheduler taskScheduler;

    public TaskScheduler getScheduler() {
        return this.taskScheduler;
    }

    /**
     * 新增定时任务
     * @param task 定时任务
     * @param cronExpression cron
     */
    public void save(Runnable task, String cronExpression) {
        save(new CronTask(task, cronExpression));
    }

    /**
     * 修改定时任务
     * TaskRunnable重写了equals方法，且save方法做了删除操作。可以把新增理解为修改
     * @param task 定时任务
     * @param cronExpression cron
     */
    public void modify(Runnable task, String cronExpression) {
        save(task, cronExpression);
    }

    /**
     * 删除定时任务
     * @param task 定时任务
     */
    public void remove(Runnable task) {
        TaskFuture taskFuture = this.scheduledTasks.remove(task);
        if (taskFuture != null)
            taskFuture.cancel();
    }

    /**
     * 清空定时任务
     */
    @Override
    public void destroy() {
        for (TaskFuture task : this.scheduledTasks.values()) {
            task.cancel();
        }
        this.scheduledTasks.clear();
    }

    private void save(CronTask cronTask) {
        if (cronTask != null) {
            Runnable task = cronTask.getRunnable();
            if (this.scheduledTasks.containsKey(task)) {
                remove(task);
            }
            this.scheduledTasks.put(task, scheduleCronTask(cronTask));
        }
    }

    private TaskFuture scheduleCronTask(CronTask cronTask) {
        TaskFuture taskFuture = new TaskFuture();
        taskFuture.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return taskFuture;
    }
}
