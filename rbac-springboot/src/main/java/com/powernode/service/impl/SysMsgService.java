package com.powernode.service.impl;

import com.powernode.commen.Page;
import com.powernode.domain.SysMsg;

import java.util.List;

public interface SysMsgService{


    int deleteByPrimaryKey(Integer msgId);

    int insert(SysMsg record);

    int insertSelective(SysMsg record);

    SysMsg selectByPrimaryKey(Integer msgId);

    int updateByPrimaryKeySelective(SysMsg record);

    int updateByPrimaryKey(SysMsg record);

    Page selectByPage(Page page);



}
