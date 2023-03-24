package com.powernode.controller;

import com.powernode.constants.CoreConstant;
import com.powernode.domain.SysUser;
import com.powernode.utils.ServletUtils;

import javax.servlet.http.HttpSession;

public class BaseController {


    /**
     *  获取当前登录的 用户对象
     * @return
     */
    public SysUser getCurrentUser(){
        HttpSession session = ServletUtils.getSession();
        return (SysUser) session.getAttribute(CoreConstant.SESSION_KEY);
    }


    /**
     *  获取当前登录用户的 用户名
     * @return
     */
    public String getUserName(){
        return getCurrentUser().getUserName();
    }

}
