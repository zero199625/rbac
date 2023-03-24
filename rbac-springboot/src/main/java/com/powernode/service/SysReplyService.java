package com.powernode.service;

import com.powernode.domain.SysReply;

import java.util.List;

public interface SysReplyService{


    int deleteByPrimaryKey(Integer replyId);

    int insert(SysReply record);

    int insertSelective(SysReply record);

    SysReply selectByPrimaryKey(Integer replyId);

    int updateByPrimaryKeySelective(SysReply record);

    int updateByPrimaryKey(SysReply record);

    List<SysReply> selectByTopicId(Integer topicId);

    Integer replyCount(Integer topicId);
}
