package com.powernode.service.impl;

import com.powernode.domain.SysRole;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.powernode.mapper.SysRoleMenuMapper;
import com.powernode.domain.SysRoleMenu;
import com.powernode.service.SysRoleMenuService;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysRoleMenuServiceImpl implements SysRoleMenuService{

    @Resource
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public int deleteByPrimaryKey(Long roleId) {
        return sysRoleMenuMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public int insert(SysRoleMenu record) {
        return sysRoleMenuMapper.insert(record);
    }

    @Override
    public int insertSelective(SysRoleMenu record) {
        return sysRoleMenuMapper.insertSelective(record);
    }

    @Override
    public void insertRoleMenuConnection(Long roleId, Long[] menuIds) {
        // 根据roleId查询该roleId是否存在角色菜单关联
        Long count = sysRoleMenuMapper.selectByRoleId(roleId);
        // 如果存在，删除
        if (count>0) {
            sysRoleMenuMapper.deleteByPrimaryKey(roleId);
        }
        /*
        *   添加角色菜单关联  通过遍历 菜单数组 的形式，逐个添加角色菜单关联到集合中，再调用sql语句批量添加
        *       因为添加sql语句中的有roleId和menuId两个参数，而sysRoleMenu对象中有这两个属性，因此直接传sysRoleMenu对象即可
        * */
        List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
        for (Long menuId : menuIds) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(roleId);
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenuList.add(sysRoleMenu);
        }
        // 批量添加 角色菜单关联
        sysRoleMenuMapper.insertRoleMenuConnectionBatch(sysRoleMenuList);
    }

}
