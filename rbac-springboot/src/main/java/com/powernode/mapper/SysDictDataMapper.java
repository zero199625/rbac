package com.powernode.mapper;

import com.powernode.domain.SysDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysDictDataMapper {
    int deleteByPrimaryKey(Long dictCode);

    int insert(SysDictData record);

    int insertSelective(SysDictData record);

    SysDictData selectByPrimaryKey(Long dictCode);

    int updateByPrimaryKeySelective(SysDictData record);

    int updateByPrimaryKey(SysDictData record);

    // 注意：返回值是一个 集合
    List<SysDictData> getByPage(@Param("params") Map params);

    List<SysDictData> getDictDataByDictType(@Param("dictType") String dictType);
}