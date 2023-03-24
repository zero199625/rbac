package com.powernode.mapper;

import com.powernode.domain.SysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMenuMapper {
    int deleteByPrimaryKey(@Param("roleId") Long roleId);

    int insert(SysRoleMenu record);

    int insertSelective(SysRoleMenu record);


    void insertRoleMenuConnectionBatch(@Param("sysRoleMenuList") List<SysRoleMenu> sysRoleMenuList);

    Long selectByRoleId(@Param("roleId") Long roleId);
}