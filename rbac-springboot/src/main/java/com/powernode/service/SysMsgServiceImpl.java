package com.powernode.service;

import com.github.pagehelper.PageHelper;
import com.powernode.commen.Page;
import com.powernode.domain.SysUser;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.powernode.mapper.SysMsgMapper;
import com.powernode.domain.SysMsg;
import com.powernode.service.impl.SysMsgService;

import java.util.List;

@Service
public class SysMsgServiceImpl implements SysMsgService{

    @Resource
    private SysMsgMapper sysMsgMapper;


    @Override
    public int deleteByPrimaryKey(Integer msgId) {
        return sysMsgMapper.deleteByPrimaryKey(msgId);
    }

    @Override
    public int insert(SysMsg record) {
        return sysMsgMapper.insert(record);
    }

    @Override
    public int insertSelective(SysMsg record) {
        return sysMsgMapper.insertSelective(record);
    }

    @Override
    public SysMsg selectByPrimaryKey(Integer msgId) {
        return sysMsgMapper.selectByPrimaryKey(msgId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysMsg record) {
        return sysMsgMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysMsg record) {
        return sysMsgMapper.updateByPrimaryKey(record);
    }

    /**
     *  分页查询邮箱    根据邮箱编号 msgMailbox 查询
     * @param page
     * @return
     */
    @Override
    public Page selectByPage(Page page) {
        // 开启分页查询
        com.github.pagehelper.Page<Object> pageHelper = PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        // 执行分页查询
        sysMsgMapper.getByPage(page.getParams());
        // 返回查询结果
        page.setList(pageHelper.getResult());
        page.setTotalCount(pageHelper.getTotal());
        return page;
    }



}
