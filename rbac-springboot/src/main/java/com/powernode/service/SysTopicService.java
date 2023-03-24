package com.powernode.service;

import com.powernode.commen.Page;
import com.powernode.domain.SysTopic;

public interface SysTopicService{


    int deleteByPrimaryKey(Integer topicId);

    int insert(SysTopic record);

    int insertSelective(SysTopic record);

    SysTopic selectByPrimaryKey(Integer topicId);

    int updateByPrimaryKeySelective(SysTopic record);

    int updateByPrimaryKey(SysTopic record);

    Page getByPage(Page page);
}
