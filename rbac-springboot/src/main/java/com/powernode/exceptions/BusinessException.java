package com.powernode.exceptions;

/*
*   自定义异常类
* */

public class BusinessException extends RuntimeException{

    private Integer code=500;

    private String message="操作错误";

    public BusinessException(String message){
        //向上传递
        super(message);
        this.message = message;
    }

    public BusinessException(Integer code, String message){
        //向上传递
        super(message);
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
