package com.powernode.commen;

import com.powernode.enums.ResultEnums;
import lombok.Data;

import java.util.HashMap;

/**
 * 结果 类
 */
@Data
public class Result<T> extends HashMap<String,Object> {

    private Integer code;

    private String msg;

    private T data;

    public Result() {
        this.code = ResultEnums.SUCCESS.getCode();
        this.msg = ResultEnums.SUCCESS.getMsg();
        this.put("code",this.code);
        this.put("msg",this.msg);
    }

    // msg自定义，code是玫举中的                  操作成功
    public Result(String msg) {
        this.code = ResultEnums.SUCCESS.getCode();
        this.msg = msg;
        this.put("code",this.code);
        this.put("msg",this.msg);
    }

    // code是玫举中的，msg是玫举的，data自定义     操作成功
    public Result(T data) {
        this.code = ResultEnums.SUCCESS.getCode();
        this.msg = ResultEnums.SUCCESS.getMsg();
        this.data = data;
        this.put("code",this.code);
        this.put("msg",this.msg);
        this.put("data",this.data);
    }

    // code是玫举中的，msg自定义，data自定义        操作成功
    public Result(String msg, T data) {
        this.code = ResultEnums.SUCCESS.getCode();
        this.msg = msg;
        this.data = data;
        this.put("code",this.code);
        this.put("msg",this.msg);
        this.put("data",this.data);
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
        this.put("code",this.code);
        this.put("msg",this.msg);
    }

    // code是玫举中的，msg自定义
    public Result(ResultEnums resultEnum, String msg) {
        this.code = resultEnum.getCode();
        this.msg = msg;
        this.put("code",this.code);
        this.put("msg",this.msg);
    }

    public Result(ResultEnums resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
        this.put("code",this.code);
        this.put("msg",this.msg);
    }

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.put("code",this.code);
        this.put("msg",this.msg);
        this.put("data",this.data);
    }

    public Result(ResultEnums resultEnum, Object data) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
        this.put("code",this.code);
        this.put("msg",this.msg);
        this.put("data",data);
    }


}