package com.lichongbing.ltools.response;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 11:48 上午
 * @description: TODO
 */
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseHelper {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;
    @ApiModelProperty(value = "返回码")
    private Integer code;
    @ApiModelProperty(value = "返回消息")
    private String message;
    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<>();


    private ResponseHelper(){

    }

    /**
     * 成功返回
     * @return
     */
    public static ResponseHelper success(){
        ResponseHelper response = new ResponseHelper();
        response.setSuccess(true);
        response.setCode(ResultCode.SUCCESS);
        response.setMessage("成功");
        return response;
    }


    /**
     * 失败返回
     * @return
     */
    public static ResponseHelper error(){
        ResponseHelper response = new ResponseHelper();
        response.setSuccess(false);
        response.setCode(ResultCode.ERROR);
        response.setMessage("失败");
        return response;
    }

    public ResponseHelper successed(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public ResponseHelper message(String message){
        this.setMessage(message);
        return this;
    }

    public ResponseHelper code(Integer code){
        this.setCode(code);
        return this;
    }

    public ResponseHelper data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public ResponseHelper data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}

