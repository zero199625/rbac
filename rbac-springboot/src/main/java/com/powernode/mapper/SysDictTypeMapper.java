package com.powernode.mapper;

import com.powernode.commen.Page;
import com.powernode.domain.SysDictData;
import com.powernode.domain.SysDictType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysDictTypeMapper {
    int deleteByPrimaryKey(Long dictId);

    int insert(SysDictType record);

    int insertSelective(SysDictType record);

    SysDictType selectByPrimaryKey(Long dictId);

    int updateByPrimaryKeySelective(SysDictType record);

    int updateByPrimaryKey(SysDictType record);

    // 这里传入的是一个Map集合，其中包括两个参数：dictName status
    // 注意：返回的是一个集合
    List<SysDictType> selectSysDictTypeByPage(@Param("params") Map params);

    // 获取所有的字典类型
    List<SysDictType> getAllDictType(@Param("status") Integer status);
}