package com.powernode.service;

import com.powernode.commen.Page;
import com.powernode.domain.SysDictType;
import com.powernode.enums.StateEnums;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDictTypeService{


    int deleteByPrimaryKey(Long dictId);

    int insert(SysDictType record);

    int insertSelective(SysDictType record);

    SysDictType selectByPrimaryKey(Long dictId);

    int updateByPrimaryKeySelective(SysDictType record);

    int updateByPrimaryKey(SysDictType record);

    Page selectSysDictTypeByPage(Page page);

    void refreshCache();

    List<SysDictType> getAllDictType();
}
