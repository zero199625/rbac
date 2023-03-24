package com.powernode.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.powernode.mapper.SysDeptMapper;
import com.powernode.domain.SysDept;
import com.powernode.service.SysDeptService;

import java.util.List;

@Service
public class SysDeptServiceImpl implements SysDeptService{

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Override
    public int deleteByPrimaryKey(Long deptId) {
        return sysDeptMapper.deleteByPrimaryKey(deptId);
    }

    @Override
    public int insert(SysDept record) {
        return sysDeptMapper.insert(record);
    }

    @Override
    public int insertSelective(SysDept record) {
        return sysDeptMapper.insertSelective(record);
    }

    @Override
    public SysDept selectByPrimaryKey(Long deptId) {
        return sysDeptMapper.selectByPrimaryKey(deptId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysDept record) {
        return sysDeptMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysDept record) {
        return sysDeptMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<SysDept> getAllDept(SysDept sysDept) {
        List<SysDept> sysDeptList =  sysDeptMapper.getAllDept(sysDept);
        return sysDeptList;
    }

    @Override
    public Long selectDeptByParentId(Long deptId) {
        Long count =  sysDeptMapper.selectDeptByParentId(deptId);
        return count;
    }

}
