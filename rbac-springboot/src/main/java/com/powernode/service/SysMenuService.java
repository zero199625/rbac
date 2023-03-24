package com.powernode.service;

import com.powernode.domain.SysMenu;
import com.powernode.domain.SysUser;
import com.powernode.vo.RouterVo;

import java.util.List;

public interface SysMenuService{


    int deleteByPrimaryKey(Long menuId);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(Long menuId);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    List<SysMenu> selectAllMenu(SysMenu sysMenu, SysUser sysUser);

    List<SysMenu> selectMenuByMenuAndCatalogue(SysUser sysUser);

    int insertByMenuTypeAndParentId(SysMenu sysMenu);

    Long selectByParentId(Long menuId);

    List<RouterVo> getRouters(SysUser sysUser);
}
