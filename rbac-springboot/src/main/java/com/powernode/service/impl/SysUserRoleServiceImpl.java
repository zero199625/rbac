package com.powernode.service.impl;

import com.powernode.domain.SysRole;
import com.powernode.domain.SysUser;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.powernode.mapper.SysUserRoleMapper;
import com.powernode.domain.SysUserRole;
import com.powernode.service.SysUserRoleService;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserRoleServiceImpl implements SysUserRoleService{

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public int deleteByPrimaryKey(Long userId) {
        return sysUserRoleMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public int insert(SysUserRole record) {
        return sysUserRoleMapper.insert(record);
    }

    @Override
    public int insertSelective(SysUserRole record) {
        return sysUserRoleMapper.insertSelective(record);
    }

    @Override
    public List<Long> selectAllRoleIdByUserId(Long userId) {
        return sysUserRoleMapper.selectAllRoleIdByUserId(userId);
    }

    @Override
    public List<SysRole> selectAllRoles() {
        return sysUserRoleMapper.selectAllRoles();
    }

    /**
     *  保存用户和角色之间的关系
     * @param userId
     * @param roleIds
     */
    @Override
    public void insertUserRoleConnection(Long userId, Long[] roleIds) {
        /*
        *   保险起见，先查一遍，判断是否没有删干净，再根据情况是否删除
        * */
        List<Long> roleIdList = sysUserRoleMapper.selectAllRoleIdByUserId(userId);
        if (roleIdList!=null) {// 不为空，再删一遍
            sysUserRoleMapper.deleteByPrimaryKey(userId);
        }
        // 注意：这里创建集合的时候一定不能不赋值，或者赋值null，会报空指针异常！！！！
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        // 遍历角色数组，将userId和roleId添加到集合中，然后到sql中一次性批量添加
        for (Long roleId : roleIds) {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            sysUserRoleList.add(sysUserRole);
        }
        // 批量添加
        sysUserRoleMapper.insertUserRoleConnectionBitch(sysUserRoleList);
    }

}
