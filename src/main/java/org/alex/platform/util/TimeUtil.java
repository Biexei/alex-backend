package org.alex.platform.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    public static String getDate(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static Long getTimeStamp(){
        return new Date().getTime();
    }

}
