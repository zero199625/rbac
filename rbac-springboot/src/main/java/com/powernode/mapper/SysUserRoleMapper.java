package com.powernode.mapper;

import com.powernode.domain.SysRole;
import com.powernode.domain.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserRoleMapper {
    int deleteByPrimaryKey(@Param("userId") Long userId);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    List<Long> selectAllRoleIdByUserId(@Param("userId") Long userId);

    List<SysRole> selectAllRoleByUserId(@Param("userId") Long userId);

    void insertUserRoleConnectionBitch(@Param("sysUserRoleList") List<SysUserRole> sysUserRoleList);

    List<SysRole> selectAllRoles();
}