package com.hws.service.exception;

import com.hws.model.vo.common.Result;
import com.hws.model.vo.common.ResultCodeEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    //全局异常
    @ExceptionHandler(value = Exception.class)   //出现异常就会执行
    @ResponseBody   //这样返回的不是json格式，需要加这个注解
    public Result error() {
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(MyException.class)
    @ResponseBody
    public Result error(MyException myException) {
        return Result.build(null, myException.getResultCodeEnum());
    }

}
