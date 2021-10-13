package com.lichongbing.ltools.response;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 11:47 上午
 * @description: TODO
 */
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Map;

public class ResponseBase {

    //    public static final String ok = "0";
//    public static final String failed = "-1";
    private String status = "0";
    private String message = "";
    private String des = "";
    private String result;

    public ResponseBase() {
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDes() {
        return this.des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getResult() {
        return this.result == null ? "" : this.result;
    }

    public void setResult(Object result) {
        if (result == null) {
            this.result = "";
        } else if (!(result instanceof String) && !(result instanceof Integer) && !(result instanceof Long) && !(result instanceof Boolean)) {
            this.result = JSONObject.toJSONString(result, new SerializerFeature[]{SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty}).replace(",null","").replace("null,","");
        } else {
            this.result = result.toString().replace(",null","").replace("null,","");
        }

    }

    public static ResponseBase success(Object result) {
        ResponseBase responseBase = new ResponseBase();
        responseBase.setStatus(ResultCode.OK);
        responseBase.setMessage("获取数据成功！");
        responseBase.setResult(result);
        return responseBase;
    }

    public static ResponseBase success(Object result, Map<String, Object> map) {
        ResponseBase responseBase = new ResponseBase();
        responseBase.setStatus(map.get("oucode").toString());
        responseBase.setMessage("获取数据成功！");
        responseBase.setDes(map.get("oudesc").toString().trim());
        responseBase.setResult(result);
        return responseBase;
    }

    public static ResponseBase fail(String message) {
        ResponseBase responseBase = new ResponseBase();
        responseBase.setStatus(ResultCode.FAILED);
        responseBase.setMessage(message);
        responseBase.setResult("{}");
        return responseBase;
    }

    public static ResponseBase fail() {
        ResponseBase responseBase = new ResponseBase();
        responseBase.setStatus(ResultCode.FAILED);
        responseBase.setMessage("获取数据失败！");
        return responseBase;
    }


}

