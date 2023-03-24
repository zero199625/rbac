package com.powernode.service;

import com.powernode.commen.Page;
import com.powernode.domain.SysRole;

import java.util.List;

public interface SysRoleService{


    int deleteByPrimaryKey(Long roleId);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    Page getByPage(Page page);

    List<Long> selectMenuById(Long roleId);


    List<SysRole> selectAllRoles();
}
