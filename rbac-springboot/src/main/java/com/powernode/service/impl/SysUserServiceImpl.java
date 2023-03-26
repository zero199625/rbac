package com.powernode.service.impl;

import com.github.pagehelper.PageHelper;
import com.powernode.commen.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.powernode.domain.SysUser;
import com.powernode.mapper.SysUserMapper;
import com.powernode.service.SysUserService;

import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService{

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public int deleteByPrimaryKey(Long userId) {
        return sysUserMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public int insert(SysUser record) {
        return sysUserMapper.insert(record);
    }

    @Override
    public int insertSelective(SysUser record) {
        return sysUserMapper.insertSelective(record);
    }

    @Override
    public SysUser selectByPrimaryKey(Long userId) {
        return sysUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysUser record) {
        return sysUserMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysUser record) {
        return sysUserMapper.updateByPrimaryKey(record);
    }

    @Override
    public SysUser selectByUsernameAndPassword(String username, String password) {
        SysUser sysUser =  sysUserMapper.selectByUsernameAndPassword(username,password);
        return sysUser;
    }

    @Override
    public List<String> getPermissionsByUserId(Long userId) {
        List<String> permissions =  sysUserMapper.getPermissionsByUserId(userId);
        return permissions;
    }

    @Override
    public Page getByPage(Page page) {
        // 开启分页查询
        com.github.pagehelper.Page pageHelper = PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        // 执行分页查询   将查询到的信息放入pageHelper对象中
        sysUserMapper.getByPage(page.getParams());
        // 设置参数
        page.setList(pageHelper.getResult());
        page.setTotalCount(pageHelper.getTotal());
        return page;
    }

    @Override
    public SysUser selectByUserName(String userName) {
        return sysUserMapper.selectByUserName(userName);
    }

    @Override
    public List<Integer> selectAllNomalUserId() {
        List<Integer> allNomalUserId = sysUserMapper.selectAllNomalUserId();
        return allNomalUserId;
    }

    /**
     *  查询所有用户信息
     * @return
     */
    @Override
    public List<SysUser> getAllUser() {
        return sysUserMapper.getAllUser();
    }

}
