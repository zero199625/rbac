package com.powernode.service.impl;

import com.github.pagehelper.PageHelper;
import com.powernode.commen.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.powernode.mapper.SysRoleMapper;
import com.powernode.domain.SysRole;
import com.powernode.service.SysRoleService;

import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService{

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public int deleteByPrimaryKey(Long roleId) {
        return sysRoleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public int insert(SysRole record) {
        return sysRoleMapper.insert(record);
    }

    @Override
    public int insertSelective(SysRole record) {
        return sysRoleMapper.insertSelective(record);
    }

    @Override
    public SysRole selectByPrimaryKey(Long roleId) {
        return sysRoleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysRole record) {
        return sysRoleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysRole record) {
        return sysRoleMapper.updateByPrimaryKey(record);
    }

    @Override
    public Page getByPage(Page page) {
        // 开启分页查询
        com.github.pagehelper.Page pageHelper =  PageHelper.startPage(page.getPageNumber(),page.getPageSize());
        // 执行分页查询
        sysRoleMapper.getByPage(page.getParams());
        page.setList(pageHelper.getResult());
        page.setTotalCount(pageHelper.getTotal());
        return page;
    }

    /**
     *  根据 roleId 查询只有三级菜单的 menuId
     * @param roleId
     * @return
     */
    @Override
    public List<Long> selectMenuById(Long roleId) {
        return sysRoleMapper.selectMenuById(roleId);
    }

    @Override
    public List<SysRole> selectAllRoles() {
        return sysRoleMapper.selectAllRoles();
    }

}
