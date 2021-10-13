package com.lichongbing.ltools.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 11:48 上午
 * @description: TODO
 */
@Data
@NoArgsConstructor
public class ResultBase<T> {

    private String status;
    private String message;
    private String des;
    private int count;
    private T result;

    public ResultBase(String status) {
        super();
        this.status = status;
    }

    public ResultBase(String status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public ResultBase(String status, String message, String des, T result) {
        super();
        this.status = status;
        this.message = message;
        this.des = des;
        this.result =result;
    }

    public ResultBase(String status, T result){
        super();
        this.status = status;
        this.result = result;
    }
    public ResultBase(String status, int count, T result){
        super();
        this.count = count;
        this.status = status;
        this.result = result;
    }
}

