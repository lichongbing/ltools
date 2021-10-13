package com.lichongbing.ltools.response;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 11:49 上午
 * @description: TODO
 */
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TaxationResponse {

    //    @ApiModelProperty(value = "是否成功")
//    private Boolean success;
    @ApiModelProperty(value = "返回码")
    private String code;
    @ApiModelProperty(value = "返回消息")
    private String msg;

    @ApiModelProperty(value = "返回数据条数")
    private Integer total;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<>();

    //// private List<Object> data = new ArrayList<>();


    private TaxationResponse() {

    }

    /**
     * 成功返回
     *
     * @return
     */
    public static TaxationResponse success() {
        TaxationResponse response = new TaxationResponse();
//        response.setSuccess(true);
        response.setCode(ResultCode.SUCCESSED);
        response.setMsg("成功");
        return response;
    }


    /**
     * 失败返回
     *
     * @return
     */
    public static TaxationResponse error() {
        TaxationResponse response = new TaxationResponse();
//        response.setSuccess(true);
        response.setCode(ResultCode.UNSUCCESS);
        response.setMsg("失败");
        return response;
    }

//    public TaxationResponse successed(Boolean success){
//        this.setSuccess(success);
//        return this;
//    }

    public TaxationResponse message(String message) {
        this.setMsg(message);
        return this;
    }

    public TaxationResponse code(String code) {
        this.setCode(code);
        return this;
    }

    public TaxationResponse total(Integer total) {
        this.setTotal(total);
        return this;
    }

    public TaxationResponse data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public TaxationResponse data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

//    public TaxationResponse data(List<Object> list) {
//        this.setData(list);
//        return this;
//    }
}

