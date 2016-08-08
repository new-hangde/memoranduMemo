package com.zxy.memorandummemo.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.String;

/**
 * Created by zxy on 2016/7/26.
 */
public class TimeRead {
     TimeRead context = this;
    public static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}
