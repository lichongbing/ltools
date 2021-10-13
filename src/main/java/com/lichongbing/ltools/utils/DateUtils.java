package com.lichongbing.ltools.utils;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 3:05 下午
 * @description: TODO
 */

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {

    public static String dateFormat(Date date){
        if (date == null){
            return null;
        }else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
            String dateFormat = format.format(date);

            return dateFormat;
        }
    }
}

