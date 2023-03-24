package com.powernode.service;

import com.powernode.domain.SysDept;

import java.util.List;

public interface SysDeptService{


    int deleteByPrimaryKey(Long deptId);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Long deptId);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    List<SysDept> getAllDept(SysDept sysDept);

    Long selectDeptByParentId(Long deptId);
}
