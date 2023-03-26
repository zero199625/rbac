package com.powernode.config;


import com.powernode.filters.MyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/*
*   注册3大组件：1.注册servlet
*               2.注册过滤器filter
*               3.注册监听器listener
*   注意：类上要加 @Configuration，方法上要加 @Bean
* */

@Configuration
public class WebConfig {


    @Resource
    private MyFilter myFilter;



    /**
     *  注册过滤器filter
     * @return
     */
    @Bean
    public FilterRegistrationBean<MyFilter> filterFilterRegistrationBean(){
        FilterRegistrationBean<MyFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        /*
        *   setFilter()：注册过滤器
        *   addUrlPatterns()：设置拦截地址
        * */
        filterRegistrationBean.setFilter(myFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }




}
