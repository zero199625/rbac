package com.powernode.service;

import com.powernode.domain.SysRole;
import com.powernode.domain.SysUserRole;

import java.util.List;

public interface SysUserRoleService{


    int deleteByPrimaryKey(Long userId);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    List<Long> selectAllRoleIdByUserId(Long userId);

    List<SysRole> selectAllRoles();


    void insertUserRoleConnection(Long userId, Long[] roleIds);
}
