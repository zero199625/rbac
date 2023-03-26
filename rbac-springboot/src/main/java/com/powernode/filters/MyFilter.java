package com.powernode.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/*
*   自定义过滤器filter  实现Filter接口，重写doFilter()方法
* */

@Component
public class MyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //设置编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        chain.doFilter(request,response);
    }

}
