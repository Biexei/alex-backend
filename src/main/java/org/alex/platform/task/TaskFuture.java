package org.alex.platform.task;

import java.util.concurrent.ScheduledFuture;

public final class TaskFuture {

    public volatile ScheduledFuture<?> future;
    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}

