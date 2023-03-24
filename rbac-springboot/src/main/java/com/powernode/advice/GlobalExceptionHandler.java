package com.powernode.advice;


import com.powernode.commen.Result;
import com.powernode.enums.ResultEnums;
import com.powernode.enums.StateEnums;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/*
*   全局异常处理类       本质上是一个增强，controller是最上面的层级，而异常时向上抛的，因此只需要对controller层进行增强
*                       @ControllerAdvice表示对controller类的增强
* */

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     *  value = Exception.class ：对controller层中出现的所有异常进行抓捕，并进行处理
     * @param e     controller层的异常
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody    // 设置返回数据为JSON字符串，若不设置则会跳转到一个静态页面
    public Result exceptionHandler(Exception e){
        // 将异常信息响应给前端      e.getMessage()就是 controller层中抛出BusinessException异常时，传入的参数
        return new Result(ResultEnums.ERROR,e.getMessage());
    }
}
