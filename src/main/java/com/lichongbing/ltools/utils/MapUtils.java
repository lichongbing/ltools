package com.lichongbing.ltools.utils;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 3:10 下午
 * @description: TODO
 */

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * map工具类
 */
public class MapUtils {

    /**
     * 对象转map方法
     *
     * @param t            对象
     * @param ignoreFields 忽略字段
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T t, String... ignoreFields) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = t.getClass().getDeclaredFields();
        List<String> list = Arrays.asList(ignoreFields);
        Arrays.stream(fields).forEach(data -> {
            data.setAccessible(true);
            try {
                if (list.isEmpty() || !list.contains(data.getName())) {

                    map.put(data.getName(), data.get(t));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return map;
    }
}

