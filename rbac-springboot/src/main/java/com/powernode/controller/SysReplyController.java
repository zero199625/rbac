package com.powernode.controller;


import com.powernode.commen.Result;
import com.powernode.domain.SysReply;
import com.powernode.service.SysReplyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("reply")
public class SysReplyController {

    @Resource
    private SysReplyService sysReplyService;


    /**
     *  根据 topicId 查询回复信息
     * @param topicId
     * @return
     */
    @GetMapping("getReplyById/{topicId}")
    public Result getReplyById(@PathVariable Integer topicId){
        List<SysReply> sysReplyList =  sysReplyService.selectByTopicId(topicId);
        return new Result("回复信息查询成功",sysReplyList);
    }


    /**
     *  新增回复信息
     * @return
     */
    @PostMapping("save")
    public Result save(@RequestBody SysReply sysReply){
        sysReply.setCreateDate(new Date());
        System.out.println(sysReply);
        sysReplyService.insert(sysReply);
        return new Result("回复成功");
    }

    /**
     *  根据topicId查询 回复人数量
     * @return
     */
    @GetMapping("count/{topicId}")
    public Result count(@PathVariable Integer topicId){
        Integer count = sysReplyService.replyCount(topicId);
        return new Result(count);
    }


}




















