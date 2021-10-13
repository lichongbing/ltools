package com.lichongbing.ltools.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常处理类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelfException extends RuntimeException{

    private Integer code;  //状态码
    private String msg;  //输出消息
}
