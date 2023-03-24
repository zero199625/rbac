package com.powernode.mapper;

import com.powernode.domain.SysReply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysReplyMapper {
    int deleteByPrimaryKey(Integer replyId);

    int insert(SysReply record);

    int insertSelective(SysReply record);

    SysReply selectByPrimaryKey(Integer replyId);

    int updateByPrimaryKeySelective(SysReply record);

    int updateByPrimaryKey(SysReply record);

    List<SysReply> selectByTopicId(@Param("topicId") Integer topicId);

    Integer replyCount(@Param("topicId") Integer topicId);
}