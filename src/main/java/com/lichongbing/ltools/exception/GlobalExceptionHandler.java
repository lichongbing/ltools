package com.lichongbing.ltools.exception;

import com.lichongbing.ltools.response.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 11:46 上午
 * @description: TODO
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 指定统一异常执行方法
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseHelper error(Exception e){
        e.printStackTrace();
        return ResponseHelper.error().message("执行了全局异常处理......");
    }
    /**
     * 指定统一异常执行方法
     * @param e
     * @return
     */
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public ResponseHelper error(ArithmeticException e){
        e.printStackTrace();
        return ResponseHelper.error().message("执行了ArithmeticException异常处理......");
    }


    /**
     * 自定义异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(SelfException.class)
    @ResponseBody
    public ResponseHelper error(SelfException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return ResponseHelper.error().code(e.getCode()).message(e.getMsg());
    }

}
