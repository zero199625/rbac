package com.powernode.mapper;

import com.powernode.domain.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysRoleMapper {
    int deleteByPrimaryKey(Long roleId);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

    // 分页查询，返回参数就是返回的数据，因此必须是集合
    List<SysRole> getByPage(@Param("params") Map params);

    // 查询到的是 menuId 的集合
    List<Long> selectMenuById(@Param("roleId") Long roleId);

    List<SysRole> selectAllRoleByUserId(@Param("userId") Long userId);

    List<SysRole> selectAllRoles();
}