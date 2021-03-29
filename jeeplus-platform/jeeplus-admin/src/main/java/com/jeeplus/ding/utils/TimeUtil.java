package com.jeeplus.ding.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: lh
 * @date: 2021/3/1
 */


public class TimeUtil {
    /**
     * 获取当前时间戳
     * @return
     */
    public static Long getUnixTime() {
        return (System.currentTimeMillis() / 1000);
    }

    /**
     * 获取当前时间
     */
    public static String getCurrent(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  df.format(new Date());
    }

    /**
     * string 转 时间戳
     * @param time
     * @return
     * @throws ParseException
     */
    public static Long getUnixTime(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(time);
            return date.getTime()/1000;
        }catch (ParseException e){
            return 0L;
        }
    }
}
