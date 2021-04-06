package org.alex.platform.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    /**
     * 获取当前毫秒
     * @return 毫秒
     */
    public static long now() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前日期
     * @param patten 格式
     * @return 获取当前日期
     */
    public static String date(String patten) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patten);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取当前日期 yyyy-MM-dd HH:mm:ss
     * @return 获取当前日期
     */
    public static String date() {
        return date("yyyy-MM-dd HH:mm:ss");
    }
}
