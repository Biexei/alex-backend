package org.alex.platform.task;

import org.alex.platform.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * 定时任务运行类
 */
public class TaskRunnable implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(TaskRunnable.class);

    private final String beanName;

    private final String methodName;

    private final Object[] params;

    public TaskRunnable(String beanName, String methodName) {
        this(beanName, methodName, (Object) null);
    }

    public TaskRunnable(String beanName, String methodName, Object...params) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
    }

    @Override
    public void run() {
        LOG.info("定时任务开始执行 - bean：{}，方法：{}，参数：{}", beanName, methodName, params);
        long startTime = System.currentTimeMillis();

        try {
            Object target = SpringUtil.getBean(beanName);

            Method method;
            if (null != params && params.length > 0) {
                Class<?>[] paramCls = new Class[params.length];
                for (int i = 0; i < params.length; i++) {
                    paramCls[i] = params[i].getClass();
                }
                method = target.getClass().getDeclaredMethod(methodName, paramCls);
            } else {
                method = target.getClass().getDeclaredMethod(methodName);
            }

            ReflectionUtils.makeAccessible(method);
            if (null != params && params.length > 0) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }
        } catch (Exception ex) {
            LOG.error(String.format("定时任务执行异常 - bean：%s，方法：%s，参数：%s ", beanName, methodName, Arrays.toString(params)), ex);
        }
        long times = System.currentTimeMillis() - startTime;
        LOG.info("定时任务执行结束 - bean：{}，方法：{}，参数：{}，耗时：{} 毫秒", beanName, methodName, params, times);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TaskRunnable that = (TaskRunnable) o;
        if (params == null) {
            return beanName.equals(that.beanName) &&
                    methodName.equals(that.methodName) &&
                    that.params == null;
        }
        return beanName.equals(that.beanName) &&
                methodName.equals(that.methodName) &&
                Arrays.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        if (params == null) {
            return Objects.hash(beanName, methodName);
        }
        return Objects.hash(beanName, methodName, Arrays.toString(params));
    }
}

