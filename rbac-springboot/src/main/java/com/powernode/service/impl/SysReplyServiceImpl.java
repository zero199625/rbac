package com.powernode.service.impl;

import com.powernode.domain.SysReply;
import com.powernode.mapper.SysReplyMapper;
import com.powernode.service.SysReplyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysReplyServiceImpl implements SysReplyService{

    @Resource
    private SysReplyMapper sysReplyMapper;

    @Override
    public int deleteByPrimaryKey(Integer replyId) {
        return sysReplyMapper.deleteByPrimaryKey(replyId);
    }

    @Override
    public int insert(SysReply record) {
        return sysReplyMapper.insert(record);
    }

    @Override
    public int insertSelective(SysReply record) {
        return sysReplyMapper.insertSelective(record);
    }

    @Override
    public SysReply selectByPrimaryKey(Integer replyId) {
        return sysReplyMapper.selectByPrimaryKey(replyId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysReply record) {
        return sysReplyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysReply record) {
        return sysReplyMapper.updateByPrimaryKey(record);
    }

    /**
     *  根据topicId查询回复信息
     * @param topicId
     * @return
     */
    @Override
    public List<SysReply> selectByTopicId(Integer topicId) {
        return sysReplyMapper.selectByTopicId(topicId);
    }

    /**
     *  根据
     * @param topicId
     * @return
     */
    @Override
    public Integer replyCount(Integer topicId) {
        return sysReplyMapper.replyCount(topicId);
    }

}



























