package com.powernode.interceptors;


import com.alibaba.fastjson.JSON;
import com.powernode.commen.Result;
import com.powernode.constants.CoreConstant;
import com.powernode.domain.SysUser;
import com.powernode.enums.ResultEnums;
import com.powernode.utils.ServletUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/*
*   拦截器
* */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     *      判断用户是否登录，如果登录了，就放行(true)，否则不放行(false)
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.从session中获取用户信息
        HttpSession session = ServletUtils.getSession();
        SysUser sysUser = (SysUser) session.getAttribute(CoreConstant.SESSION_KEY);
        // 2.判断用户是否登录
        if (sysUser==null) {
            // 3.用户未登录，响应给前端 未登录信息
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(new Result<>(ResultEnums.USER_NOT_FOUND)));
            writer.flush();
            writer.close();
            // 未登录，不放行
            return false;
        }
        // 4.用户已登录，放行
        return true;
    }
}


















