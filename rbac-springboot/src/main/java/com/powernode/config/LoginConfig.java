package com.powernode.config;


import com.powernode.constants.CoreConstant;
import com.powernode.interceptors.LoginInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/*
*   注册过滤器
* */

@Component
public class LoginConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(CoreConstant.LOGIN_URL,CoreConstant.CAPTCHA_URL);
    }
}
