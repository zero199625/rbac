package com.powernode.controller;


import com.powernode.commen.Page;
import com.powernode.commen.Result;
import com.powernode.domain.SysTopic;
import com.powernode.service.SysTopicService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("topic")
public class SysTopicController {


    @Resource
    private SysTopicService sysTopicService;

    /**
     *  分页查询帖子
     * @param page
     * @return
     */
    @PostMapping("getByPage")
    public Result getByPage(@RequestBody Page page){
        page =  sysTopicService.getByPage(page);
        return new Result("分页查询成功",page);
    }

    /**
     *  发表帖子
     * @param sysTopic
     * @return
     */
    @PostMapping("save")
    public Result save(@RequestBody SysTopic sysTopic){
        sysTopic.setCreateDate(new Date());
        sysTopic.setClickAmount(0);
        sysTopicService.insertSelective(sysTopic);
        return new Result("帖子发表成功");
    }


    /**
     *  根据ID获取帖子信息
     * @return
     */
    @GetMapping("getTopicById/{topicId}")
    public Result getTopicById(@PathVariable Integer topicId){
        SysTopic sysTopic = sysTopicService.selectByPrimaryKey(topicId);
        return new Result("查询成功", sysTopic);
    }


    /**
     *  根据ID更新帖子点击量
     * @param topicId
     * @return
     */
    @PutMapping("update/{topicId}")
    public Result update(@PathVariable Integer topicId){
        SysTopic sysTopic = sysTopicService.selectByPrimaryKey(topicId);
        Integer count = sysTopic.getClickAmount();
        //  帖子点击量加一
        count = count + 1;
        sysTopic.setClickAmount(count);
        // 更新帖子
        sysTopicService.updateByPrimaryKeySelective(sysTopic);
        return new Result("帖子点击量更新成功");
    }

















}
