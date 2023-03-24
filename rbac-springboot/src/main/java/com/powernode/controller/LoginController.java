package com.powernode.controller;


import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.crypto.digest.DigestUtil;
import com.powernode.commen.Result;
import com.powernode.constants.CoreConstant;
import com.powernode.domain.SysUser;
import com.powernode.enums.ResultEnums;
import com.powernode.service.SysUserService;
import com.powernode.utils.ServletUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
*   登录controller类
*   1.用户登录
*   2.用户退出
*   3.得到用户信息
* */

@RestController
public class LoginController extends BaseController{

    @Resource
    private SysUserService sysUserService;

    /**
     *      用户登录
     * @param username    用户名
     * @param password    密码
     * @param code        验证码文本
     * @return
     */
    @PostMapping(CoreConstant.LOGIN_URL)
    public Result login(String username, String password, String code){
        /**
         * 登录逻辑处理
         *    1.校验验证码
         *       验证码为空 或者 不正确 方法结束
         *       验证码如果正确 session 删除验证码key(如果不删除，下次用这个验证码就仍然可以登录)
         *    2.根据用户名 和 密码 到数据库查询用户记录
         *       用户记录不存在 方法结束
         *    3.用户存在  将用户信息存入session 返回result 携带用户信息
         */
        // 1.获取验证码
        HttpSession session = ServletUtils.getSession();
        CircleCaptcha captcha = (CircleCaptcha) session.getAttribute(CoreConstant.CAPTCHA_KEY);
        // 2.校验验证码是否为空、验证码是否正确
        if (captcha==null) {
            return new Result(ResultEnums.ERROR,"验证码为空或已过期");
        }
        if (!captcha.verify(code)) {
            return new Result(ResultEnums.ERROR,"验证码错误");
        }
        // 3.验证码正确，移除session中的验证码(以免使用旧的验证码还能登录)
        session.removeAttribute(CoreConstant.CAPTCHA_KEY);
        // 4.根据用户名 和 密码 到数据库中查找  (对密码进行加密处理，否则数据库中密码一目了然)
        password = DigestUtil.md5Hex(password);
        SysUser sysUser =  sysUserService.selectByUsernameAndPassword(username,password);
        System.out.println(sysUser);
        // 5.校验记录是否存在
        if (sysUser==null) {
            return new Result(ResultEnums.USER_PASSWORD_ERROR);
        }
        // 6.用户存在，将用户信息存入session(表示已经登录过了)
        session.setAttribute(CoreConstant.SESSION_KEY,sysUser);
        // 7.登陆成功，返回result(携带用户信息)
        return new Result("用户登录成功",sysUser);
    }

    /**
     *      用户退出(从会话中移除用户信息)
     * @return
     */
    @GetMapping("/logout")
    public Result logout(){
        // 1.将用户信息从session中移除
        HttpSession session = ServletUtils.getSession();
        session.removeAttribute(CoreConstant.SESSION_KEY);
        // session.invalidate()：将session设置为失效   和session.removeAttribute()类似的效果
        session.invalidate();
        // 2.退出成功，返回result
        return new Result("退出成功");
    }

    /**
     *      得到用户信息(已登录)和用户权限信息(位于数据库menu表中)
     * @return
     */
    @GetMapping("/getUserInfo")
    public Result getUserInfo(){
        // 1.从会话中获取到登录的用户对象
        SysUser sysUser = getCurrentUser();
        // 2.防止前端可以直接看到密码，所以将密码设置成空字符串
        sysUser.setPassword("");
        System.out.println(sysUser);
        // 3.根据用户id获取用户的 权限信息(permission：select、insert、delete、update...)
        List<String > permissions =  sysUserService.getPermissionsByUserId(sysUser.getUserId());
        // 4.将用户对象 和 权限信息(位于数据库menu表中)放入Map集合中   (因为要一次性将permissions集合和sysUser对象放入result中，所以只能用map集合)
        Map<String,Object> map = new HashMap<>();
        map.put("sysUser",sysUser);
        map.put("permissions",permissions);

        // 将 用户权限 放入session会话
        HttpSession session = ServletUtils.getSession();
        session.setAttribute("permissions",permissions);

        // 5.将map集合放入result对象中
        Result<Map<String,Object>> rs = new Result<>(map);
        // 6.返回result(包括用户信息 和 用户权限信息)
        return rs;
    }
}


















