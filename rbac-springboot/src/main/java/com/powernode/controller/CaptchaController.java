package com.powernode.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.LineCaptcha;
import com.powernode.commen.Result;
import com.powernode.constants.CoreConstant;
import com.powernode.enums.ResultEnums;
import com.powernode.utils.ServletUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/*
*   获取验证码
* */

@RestController
public class CaptchaController {


    /**
     *  CoreConstant.CAPTCHA_URL：验证码的访问路径
     *      createCode 创建验证码，实现类需同时生成随机验证码字符串和验证码图片
     *      getCode()：获取验证码文字信息
     *      verify 验证验证码是否正确，建议忽略大小写
     *      write 将验证码写出到目标流或地址中
     */
    /*@GetMapping(CoreConstant.CAPTCHA_URL)
    public void getCaptcha(HttpServletResponse response) throws IOException {
        // 获取验证码
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200,100,5,40);
        // 1.将验证码图片写入D盘中
        //lineCaptcha.write("D:\\image");
        ServletOutputStream outputStream = response.getOutputStream();
        // 2.将验证码图片响应给前端
        lineCaptcha.write(outputStream);
        System.out.println("验证码为：" + lineCaptcha.getCode());
    }*/



    /**
     *  CoreConstant.CAPTCHA_URL：验证码的访问路径
     *      createCode 创建验证码，实现类需同时生成随机验证码字符串和验证码图片
     *      getCode()：获取验证码文字信息
     *      verify 验证验证码是否正确，建议忽略大小写
     *      write 将验证码写出到目标流或地址中
     *      getImageBase64Data()：将图片文件 变为 二进制文件
     */
    @GetMapping(CoreConstant.CAPTCHA_URL)
    public Result<String> getCaptcha(){
        // 1.获取验证码
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(100,40,1,20);
        // 2.将验证码存入session (以便在登录的时候做验证)
        HttpSession session = ServletUtils.getSession();
        session.setAttribute(CoreConstant.CAPTCHA_KEY,captcha);
        System.out.println(captcha.getCode());
        // 3.将 验证码图片 变为 字符串形式的经过编码之后的文件
        String imageBase64Data = captcha.getImageBase64Data();
        // 3.将结果放入result中，作为响应返回
        Result<String> rs = new Result<>(ResultEnums.SUCCESS.getMsg(),imageBase64Data);
        return rs;
    }

}













