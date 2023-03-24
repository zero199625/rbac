package com.powernode.mapper;

import com.powernode.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysUserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser selectByUsernameAndPassword(@Param("username") String username,@Param("password") String password);

    List<String> getPermissionsByUserId(@Param("userId") Long userId);

    // 注意：持久层中，分页查询返回参数是集合类型
    List getByPage(@Param("params") Map params);

    SysUser selectByUserName(@Param("userName") String userName);

}