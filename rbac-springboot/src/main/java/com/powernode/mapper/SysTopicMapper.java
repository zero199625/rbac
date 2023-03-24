package com.powernode.mapper;

import com.powernode.domain.SysTopic;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysTopicMapper {
    int deleteByPrimaryKey(Integer topicId);

    int insert(SysTopic record);

    int insertSelective(SysTopic record);

    SysTopic selectByPrimaryKey(Integer topicId);

    int updateByPrimaryKeySelective(SysTopic record);

    int updateByPrimaryKey(SysTopic record);

    List<SysTopic> getByPage(@Param("params") Map params);
}