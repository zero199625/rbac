package com.powernode.controller;

import com.powernode.commen.Page;
import com.powernode.commen.Result;
import com.powernode.domain.SysMsg;
import com.powernode.domain.SysUser;
import com.powernode.service.SysUserService;
import com.powernode.service.impl.SysMsgService;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("sysMsg")
public class SysMsgController extends BaseController{

    @Resource
    private SysMsgService sysMsgService;

    @Resource
    private SysUserService sysUserService;


    /**
     *  分页查询邮箱     根据邮箱编号 msgMailbox 和 当前用户ID 查询
     * @param page
     * @return
     */
    @PostMapping("getByPage")
    public Result getByPage(@RequestBody Page page){
        Map params = page.getParams();
        params.put("userId",getCurrentUser().getUserId());
        page.setParams(params);
        page = sysMsgService.selectByPage(page);
        return new Result("分页查询成功", page);
    }


    /**
     *  根据消息ID查询消息
     * @param msgId
     * @return
     */
    @GetMapping("getByMsgId/{msgId}")
    public Result getByMsgId(@PathVariable Integer msgId){
        SysMsg sysMsg = sysMsgService.selectByPrimaryKey(msgId);
        return new Result("查询成功", sysMsg);
    }


    /**
     *  点到面发送消息：1.管理员新增一条 发送消息
     *                2.所有普通用户新增一条 接收消息
     *  前端的发送消息：msgTitle   msgContent
     * @param sysMsg
     * @return
     */
    @PostMapping("sendAll")
    public Result sendAll(@RequestBody SysMsg sysMsg){
        SysUser user = getCurrentUser();


        /*
        *   收件人   接收消息
        * */
        // 查询所有普通用户的userId   去重查询   查询到的是单个普通用户的userId   这就是接收人id
        List<Integer> allNomalUserId = sysUserService.selectAllNomalUserId();
        // 过滤掉管理员的userId
        allNomalUserId.remove(0);
        // 遍历普通用户id，每个普通用户新增一条接收消息
        for (Integer id : allNomalUserId) {
            /*
             *   发送人   发送消息
             * */
            // 接收人id
            sysMsg.setMsgReceiveId(id);

            // 发送人邮箱编号   发件箱 1
            sysMsg.setMsgMailbox(1);
            sysMsg.setMsgDel(0);
            // 用户ID
            sysMsg.setUserId(user.getUserId().intValue());
            // 发件人ID
            sysMsg.setMsgSenderId(user.getUserId().intValue());
            // 发件时间
            sysMsg.setMsgSendTime(new Date());
            // 消息类型  系统消息
            sysMsg.setMsgType(0);
            // 用户级别  管理员
            sysMsg.setUserType(0);
            sysMsgService.insertSelective(sysMsg);


            // 接收时间
            sysMsg.setMsgReceiveTime(new Date());
            // 用户id    就是接收人id
            sysMsg.setUserId(id);
            // 收件人邮箱编号   收件箱0
            sysMsg.setMsgMailbox(0);
            // 是否已读   0 ：未读   1 ：已读
            sysMsg.setMsgRead(0);
            // 用户级别  管理员
            sysMsg.setUserType(1);
            sysMsgService.insertSelective(sysMsg);

        }
        return new Result("系统消息发送成功");
    }


    /**
     *      点到点发送消息：1.发送人新增一条 发送消息
     *                     2.收件人新增一条 收件消息
     *      前端的发送消息：msgTitle   msgContent   msgReceiveId
     * @param sysMsg
     * @return
     */
    @PostMapping("send")
    public Result send(@RequestBody SysMsg sysMsg){
        System.out.println("收件人 -->" + sysMsg);

        /*
        *   发送人   发送消息
        * */
        SysUser user = getCurrentUser();
        // 邮箱编号   发件箱 1
        sysMsg.setMsgMailbox(1);
        sysMsg.setMsgDel(0);
        // 用户ID
        sysMsg.setUserId(user.getUserId().intValue());
        /*// 发件人ID
        sysMsg.setMsgSenderId(user.getUserId().intValue());*/
        // 收件人ID
        /*sysMsg.setMsgReceiveId(sysMsg.getMsgReceiveId());*/
        // 发件时间
        sysMsg.setMsgSendTime(new Date());
        // 消息类型
        if (user.getUserId()==1) {  //系统消息
            sysMsg.setMsgType(0);
            sysMsg.setUserType(0);
        }else {  // 用户消息
            sysMsg.setMsgType(1);
            // 用户级别
            sysMsg.setUserType(1);
        }
        sysMsgService.insertSelective(sysMsg);
        /*
        *   收件人   接收消息
        * */
        // 用户 ID

        sysMsg.setUserId(sysMsg.getMsgReceiveId());
        // 收件人邮箱编号  0
        sysMsg.setMsgMailbox(0);
        // 邮件接受时间
        sysMsg.setMsgReceiveTime(new Date());
        // 是否已读   0 ：未读   1 ：已读
        sysMsg.setMsgRead(0);
        sysMsgService.insertSelective(sysMsg);
        return new Result("发送成功");
    }


    /**
     *  根据消息ID 删除消息：1.放入垃圾箱：将 msgMailbox 改为2
     *                      2.彻底删除：将msgDel改为1
     *  放入垃圾箱
     * @param msgId
     * @return
     */
    @DeleteMapping("putToDustbin/{msgId}")
    public Result putToDustbin(@PathVariable Integer msgId){
        /*
        *   将消息放入垃圾箱
        * */
        // 根据msId查询到该条消息
        SysMsg sysMsg = sysMsgService.selectByPrimaryKey(msgId);
        sysMsg.setMsgMailbox(2);
        sysMsg.setMsgDeleteTime(new Date());
        sysMsgService.updateByPrimaryKeySelective(sysMsg);
        return new Result("成功将消息放入垃圾箱");
    }


    /**
     *  根据消息ID 删除消息：1.放入垃圾箱：将 msgMailbox 改为2
     *                      2.彻底删除消息：将msgDel改为1
     *  彻底删除消息
     * @param msgId
     * @return
     */
    @DeleteMapping("delete/{msgId}")
    public Result delete(@PathVariable Integer msgId){
        /*
         *   彻底删除消息
         * */
        // 根据msId查询到该条消息
        SysMsg sysMsg = sysMsgService.selectByPrimaryKey(msgId);
        sysMsg.setMsgDel(1);
        sysMsgService.updateByPrimaryKeySelective(sysMsg);
        return new Result("彻底删除消息");
    }


    /**
     *   根据id修改消息
     *      将消息修改为已读，将 msgRead 改为 1       还有msgReadTime
     * @return
     */
    @PutMapping("isRead/{msgId}")
    public Result isRead(@PathVariable Integer msgId){
        SysMsg sysMsg = sysMsgService.selectByPrimaryKey(msgId);
        sysMsg.setMsgRead(1);
        sysMsg.setMsgReadTime(new Date());
        sysMsgService.updateByPrimaryKeySelective(sysMsg);
        return new Result("已读");
    }


}
