package com.powernode.controller;


import cn.hutool.crypto.digest.DigestUtil;
import com.powernode.annotations.AppPermission;
import com.powernode.commen.Page;
import com.powernode.commen.Result;
import com.powernode.constants.CoreConstant;
import com.powernode.domain.SysUser;
import com.powernode.enums.ResultEnums;
import com.powernode.enums.StateEnums;
import com.powernode.service.SysUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/*
*   用户controller类
* */

@RestController
@RequestMapping("sysUser")
public class SysUserController extends BaseController {

    @Resource
    private SysUserService sysUserService;



    /**
     *  分页查询用户
     * @param page
     * @return
     */
    @PostMapping("getByPage")
    @AppPermission(value = "system:user:query")  // 将 访问的资源 与 权限标识符 绑定
    public Result getByPage(@RequestBody Page page){  // 请求参数是JSON字符串
        // 请求参数是 page(pageNumber,pageSize,params)， 返回参数也是page(list,totalCount,totalPage)
        page =  sysUserService.getByPage(page);
        return new Result("用户分页查询成功",page);
    }


    /**
     *  添加用户     数据库中用户名唯一，因此要进行判断
     *              根据请求参数中的 userName 查询数据库中是否存在该用户
     *                 1.不存在，添加
     *                 2.存在，不添加
     * @param sysUser
     * @return
     */
    @PostMapping("save")
    @AppPermission(value = "system:user:add")  // 将 访问的资源 与 权限标识符 绑定
    public Result save(@RequestBody SysUser sysUser){  // 请求参数是JSON字符串
        SysUser temp = sysUserService.selectByUserName(sysUser.getUserName());
        if (temp!=null) { // 不为空，说明存在
            return new Result(ResultEnums.ERROR,"用户已存在，请重新添加");
        }
        // 设置默认密码123456 ，对密码进行加密处理     DigestUtil.md5Hex(参数)：对参数进行加密处理
        sysUser.setPassword(DigestUtil.md5Hex(CoreConstant.DEFAULT_PASSWORD));
        // StateEnums.NOT_DELETED.getCode()=0
        sysUser.setDelFlag(StateEnums.NOT_DELETED.getCode());
        // StateEnums.USER_TYPE_NORMAL.getCode()=1
        sysUser.setUserType(StateEnums.USER_TYPE_NORMAL.getCode());
        sysUser.setCreateBy(getUserName());
        sysUser.setCreateTime(new Date());
        sysUserService.insertSelective(sysUser);
        return new Result("用户添加成功");
    }


    /**
     *  更新用户       数据库中用户名唯一
     *                根据请求参数中的 userName 查询数据库中是否存在该用户
     *                  1.不存在，更新
     *                  2.存在，比较userId：(1)相同，说明查到的就是当前修改的用户，只是修改的不是 userName    更新
     *                                     (2)不同，不能更新
     * @param sysUser
     * @return
     */
    @PutMapping("update")
    @AppPermission(value = "system:user:update")
    public Result update(@RequestBody SysUser sysUser){  // 请求参数是JSON字符串
        SysUser temp = sysUserService.selectByUserName(sysUser.getUserName());
        // 如果用户存在，且userId不同时，不能修改
        if (temp!=null && !sysUser.getUserId().equals(temp.getUserId())) {
            return new Result("用户名已存在");
        }
        // 其他情况都能修改    StateEnums.USER_TYPE_NORMAL.getCode()=1
        sysUser.setUpdateBy(getUserName());
        sysUser.setUpdateTime(new Date());
        sysUserService.updateByPrimaryKeySelective(sysUser);
        return new Result("用户更新成功");
    }


    /**
     *  删除用户   逻辑上的删除，将 delFlag 改为 1
     * @param userId
     * @return
     */
    @DeleteMapping("delete/{userId}")
    @AppPermission(value = "system:user:remove")
    public Result delete(@PathVariable Long userId){
        SysUser sysUser = new SysUser();
        // StateEnums.DELETED.getCode()=1
        sysUser.setDelFlag(StateEnums.DELETED.getCode());
        // 这一步表明新建的 sysUser 就是当前用户(虽然只有一个userId)
        sysUser.setUserId(userId);
        // 由于sysUser只有一个userId，以此更新用户必须用updateByPrimaryKeySelective()，否则其他参数都会变为null
        sysUserService.updateByPrimaryKeySelective(sysUser);
        return new Result("用户删除成功");
    }


    /**
     *  用户重置密码   将用户密码改为 加密后的默认密码 123456
     * @return
     */
    @GetMapping("resetPwd/{userId}")
    public Result resetPwd(@PathVariable Long userId){
        SysUser sysUser = new SysUser();
        // 这一步表明新建的 sysUser 就是当前用户(虽然只有一个userId)
        sysUser.setUserId(userId);
        // 将当前用户的密码设为默认的123456
        sysUser.setPassword(DigestUtil.md5Hex(CoreConstant.DEFAULT_PASSWORD));
        // 由于sysUser只有一个userId，以此更新用户必须用updateByPrimaryKeySelective()，否则其他参数都会变为null
        sysUserService.updateByPrimaryKeySelective(sysUser);
        return new Result("用户密码重置成功");
    }


    /**
     *  通过userId查询用户信息
     * @param userId
     * @return
     */
    @GetMapping("get/{userId}")
    public Result getByUserId(@PathVariable Long userId){// 注意这里的userId为Long类型，因为数据库中是bigint类型
        SysUser sysUser = sysUserService.selectByPrimaryKey(userId);
        return new Result(sysUser);
    }




}


















