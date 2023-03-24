package com.powernode.mapper;

import com.powernode.domain.SysMenu;
import com.powernode.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper {
    int deleteByPrimaryKey(Long menuId);

    int insert(SysMenu record);

    int insertSelective(SysMenu record);

    SysMenu selectByPrimaryKey(Long menuId);

    int updateByPrimaryKeySelective(SysMenu record);

    int updateByPrimaryKey(SysMenu record);

    // 管理员，查询所有菜单信息
    List<SysMenu> selectAllMenu(@Param("sysMenu") SysMenu sysMenu);

    List<SysMenu> selectMenuByPerms(@Param("sysMenu") SysMenu sysMenu, @Param("sysUser") SysUser sysUser);

    List<SysMenu> selectMenuByMenuAndCatalogue();

    Long selectMaxByMenuTypeAndParentId(@Param("menuType") Integer menuType,@Param("parentId") Long parentId);

    Long selectByParentId(@Param("menuId") Long menuId);

    List<SysMenu> selectMCByUserId(@Param("userId") Long userId);
}