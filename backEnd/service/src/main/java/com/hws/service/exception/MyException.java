package com.hws.service.exception;

import com.hws.model.vo.common.ResultCodeEnum;
import lombok.Data;

//TODO 不加data注解就不能使用getCode()、getResultCodeEnum()、set...的方法
@Data
public class MyException extends RuntimeException{

    private Integer code;
    private String msg;
    private ResultCodeEnum resultCodeEnum;

    public MyException(ResultCodeEnum resultCodeEnum) {
        this.resultCodeEnum = resultCodeEnum;
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }
    public MyException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
