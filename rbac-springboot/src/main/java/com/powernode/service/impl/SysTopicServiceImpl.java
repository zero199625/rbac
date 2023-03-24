package com.powernode.service.impl;

import com.github.pagehelper.PageHelper;
import com.powernode.commen.Page;
import com.powernode.domain.SysTopic;
import com.powernode.mapper.SysTopicMapper;
import com.powernode.service.SysTopicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysTopicServiceImpl implements SysTopicService{

    @Resource
    private SysTopicMapper sysTopicMapper;

    @Override
    public int deleteByPrimaryKey(Integer topicId) {
        return sysTopicMapper.deleteByPrimaryKey(topicId);
    }

    @Override
    public int insert(SysTopic record) {
        return sysTopicMapper.insert(record);
    }

    @Override
    public int insertSelective(SysTopic record) {
        return sysTopicMapper.insertSelective(record);
    }

    @Override
    public SysTopic selectByPrimaryKey(Integer topicId) {
        return sysTopicMapper.selectByPrimaryKey(topicId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysTopic record) {
        return sysTopicMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysTopic record) {
        return sysTopicMapper.updateByPrimaryKey(record);
    }

    /**
     *  分页查询帖子
     * @param page
     * @return
     */
    @Override
    public Page getByPage(Page page) {
        // 开启分页查询
        com.github.pagehelper.Page<Object> pageHelper = PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        // 执行分页查询
        sysTopicMapper.getByPage(page.getParams());
        // 返回查询数据
        page.setList(pageHelper.getResult());
        page.setTotalCount(pageHelper.getTotal());
        return page;
    }

}
