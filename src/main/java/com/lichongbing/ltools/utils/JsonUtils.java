package com.lichongbing.ltools.utils;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 3:08 下午
 * @description: TODO
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;
import java.util.Map;

/**
 * JSON 工具类
 */
public class JsonUtils {

    /**
     * 将对象转化为json字符串
     * @param obj
     * @return
     */
    public static final String toJson(final Object obj) {
        return JSON.toJSONString(obj);
    }


    /**
     * 对象转化为json字符串
     *
     * @param obj
     * @param features
     * @return
     */
    public static final String toJson(final Object obj, SerializerFeature... features) {
        return JSON.toJSONString(obj, features);
    }


    /**
     * 对象转化为json字符串并格式化
     *
     * @param obj
     * @param format 是否格式化
     * @return
     */
    public static final String toJson(final Object obj, final boolean format) {

        return JSON.toJSONString(obj, format);
    }


    /**
     * 对对象指定字段进行过滤处理， 并生成json字符串
     *
     * @param obj      对象
     * @param fields   过滤处理字段
     * @param ignore   true：做忽略处理， false: 做包含处理
     * @param features json 特征， 传null则忽略
     * @return
     */
    public static final String toJson(final Object obj, final String[] fields, final boolean ignore, SerializerFeature... features) {
        if (fields == null || fields.length < 1) {
            return toJson(obj);
        }

        if (features == null) {
            features = new SerializerFeature[]{SerializerFeature.QuoteFieldNames};

        }

        return JSON.toJSONString(obj, new PropertyFilter() {
            @Override
            public boolean apply(Object object, String name, Object value) {
                for (int i = 0; i < fields.length; i++) {
                    if (name.equals(fields[i])) {
                        return !ignore;
                    }
                }
                return ignore;
            }
        }, features);
    }


    /**
     * 解析json字符串中某路径的值
     *
     * @param json
     * @param path
     * @param <E>
     * @return
     */
    public static final <E> E parse(final String json, final String path) {
        String[] keys = path.split(",");
        JSONObject obj = JSON.parseObject(json);
        for (int i = 0; i < keys.length - 1; i++) {
            obj = obj.getJSONObject(keys[i]);
        }

        return (E) obj.get(keys[keys.length - 1]);
    }


    /**
     * json字符串解析为对象
     *
     * @param json  代表一个对象的Json字符串
     * @param clazz 指定目标对象的类型，即返回对象的类型
     * @param <T>
     * @return 从json字符串解析出来的对象
     */
    public static final <T> T parse(final String json, final Class<T> clazz) {

        return JSON.parseObject(json, clazz);
    }


    /**
     * json字符串解析为对象
     *
     * @param json
     * @param path  逗号分隔的json层次结构
     * @param clazz
     * @param <T>
     * @return
     */
    public static final <T> T parse(final String json, final String path, final Class<T> clazz) {
        String[] keys = path.split(",");
        JSONObject obj = JSON.parseObject(json);
        for (int i = 0; i < keys.length - 1; i++) {
            obj = obj.getJSONObject(keys[i]);
        }

        String inner = obj.getString(keys[keys.length - 1]);
        return parse(inner, clazz);
    }


    /**
     * 将制定的对象经过字段过滤处理后，解析成为json集合
     *
     * @param obj
     * @param fields
     * @param ignore
     * @param clazz
     * @param features
     * @param <T>
     * @return
     */
    public static final <T> List<T> parseArray(final Object obj, final String[] fields, boolean ignore, final Class<T> clazz, final SerializerFeature... features) {
        String json = toJson(obj, fields, ignore, features);
        return parseArray(json, clazz);
    }


    /**
     * 从json字符串中解析出一个对象的集合，被解析字符串要求是合法的集合类型
     * 形如:["k1":"v1","k2":"v2",..."kn":"vn"]
     *
     * @param json  - [key-value-pair...]
     * @param clazz
     * @param <T>
     * @return
     */
    public static final <T> List<T> parseArray(final String json, final Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }


    /**
     * 从json字符串中按照路径寻找，并解析出一个对象的集合，例如：
     * {
     * "page_info":{
     * "items":{
     * "item":[{"name":"KelvinZ"},{"name":"Jobs"},...{"name":"Gates"}]
     * }
     * }
     *
     * @param json  json字符串
     * @param path  逗号分隔的json层次结构
     * @param clazz
     * @param <T>
     * @return
     */
    public static final <T> List<T> parseArray(final String json, final String path, final Class<T> clazz) {
        String[] keys = path.split(",");
        JSONObject obj = JSON.parseObject(json);
        for (int i = 0; i < keys.length - 1; i++) {
            obj = obj.getJSONObject(keys[i]);
        }

        String inner = obj.getString(keys[keys.length - 1]);
        List<T> result = parseArray(inner, clazz);

        return result;
    }

    /**
     * <pre>
     * 有些json的常见格式错误这里可以处理，以便给后续的方法处理
     * 常见错误：使用了\" 或者 "{ 或者 }"，腾讯的页面中常见这种格式
     *
     * @param invalidJson 包含非法格式的json字符串
     * @return
     */
    public static final String correctJson(final String invalidJson) {
        String content = invalidJson.replace("\\\"", "\"").replace("\"{", "{").replace("}\"", "}");
        return content;
    }


    /**
     * 格式化json
     *
     * @param json
     * @return
     */
    public static final String formatJson(String json) {
        Map<?, ?> map = (Map<?, ?>) JSON.parse(json);
        return JSON.toJSONString(map, true);
    }


    /**
     * 获取json中的子json
     * @param json
     * @param path
     * @return
     */
    public static final String getSubJson(String json, String path) {
        String[] keys = path.split(",");
        JSONObject obj = JSON.parseObject(json);
        for (int i = 0; i < keys.length - 1; i++) {
            obj = obj.getJSONObject(keys[i]);
            System.out.println(obj.toJSONString());
        }

        return obj != null ? obj.getString(keys[keys.length - 1]) : null;
    }

}

