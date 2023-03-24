package com.powernode.service;

import com.powernode.commen.Page;
import com.powernode.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserService{


    int deleteByPrimaryKey(Long userId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser selectByUsernameAndPassword(String username, String password);

    List<String> getPermissionsByUserId(Long userId);

    Page getByPage(Page page);

    SysUser selectByUserName(String userName);
}
