package com.powernode.service;

import com.powernode.domain.SysRoleMenu;
public interface SysRoleMenuService{


    int deleteByPrimaryKey(Long roleId);

    int insert(SysRoleMenu record);

    int insertSelective(SysRoleMenu record);

    void insertRoleMenuConnection(Long roleId, Long[] menuIds);
}
