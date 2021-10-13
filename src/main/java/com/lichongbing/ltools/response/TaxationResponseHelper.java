package com.lichongbing.ltools.response;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 1:04 下午
 * @description: TODO
 */
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class TaxationResponseHelper {


    @ApiModelProperty(value = "返回码")
    private String code;
    @ApiModelProperty(value = "返回消息")
    private String msg;

    @ApiModelProperty(value = "返回数据条数")
    private Integer total;

    @ApiModelProperty(value = "返回数据")
    private List<Map<String, Object>> data = new ArrayList<>();


    private TaxationResponseHelper() {

    }

    /**
     * 成功返回
     *
     * @return
     */
    public static TaxationResponseHelper success() {
        TaxationResponseHelper response = new TaxationResponseHelper();
        response.setCode(ResultCode.SUCCESSED);
        response.setMsg("成功");
        return response;
    }


    /**
     * 失败返回
     *
     * @return
     */
    public static TaxationResponseHelper error() {
        TaxationResponseHelper response = new TaxationResponseHelper();
        response.setCode(ResultCode.UNSUCCESS);
        response.setMsg("失败");
        return response;
    }


    public TaxationResponseHelper message(String message) {
        this.setMsg(message);
        return this;
    }

    public TaxationResponseHelper code(String code) {
        this.setCode(code);
        return this;
    }

    public TaxationResponseHelper total(Integer total) {
        this.setTotal(total);
        return this;
    }


    public TaxationResponseHelper data(List<Map<String, Object>> data){
        this.setData(data);
        return this;
    }


}

