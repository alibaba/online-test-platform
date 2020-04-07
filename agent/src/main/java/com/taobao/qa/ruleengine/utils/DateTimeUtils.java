package com.taobao.qa.ruleengine.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    public static String getCurrentTimeInFormat() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public static String getCurrentDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(new Date());
    }

    public static Long getCurrentTimeStamp() {
        return System.currentTimeMillis();
    }
}
