package com.powernode.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMsg implements Serializable {
    /**
    * 消息id
    */
    private Integer msgId;

    /*
    *   用户ID
    * */
    private Integer userId;

    /**
    * 消息标题
    */
    private String msgTitle;

    /**
    * 消息内容
    */
    private String msgContent;

    /**
    * 消息发送人(用户ID)
    */
    private Integer msgSenderId;

    /**
    * 消息接收人(用户ID)
    */
    private Integer msgReceiveId;

    /**
    * 邮箱编号(0 : 收件箱  1 : 发件箱  2 : 垃圾箱)
    */
    private Integer msgMailbox;

    /**
    * 消息类型(0 : 系统消息  1 : 用户消息)
    */
    private Integer msgType;

    /**
    * 用户级别(0 : 管理员  1 : 普通用户)
    */
    private Integer userType;

    /**
    * 是否已读(0 : 未读  1 : 已读)
    */
    private Integer msgRead;

    /**
    * 是否删除(0 : 未删除  1 : 删除)
    */
    private Integer msgDel;

    /**
    * 邮件发送时间
    */
    private Date msgSendTime;

    /**
    * 邮件接收时间
    */
    private Date msgReceiveTime;

    /**
    * 邮件放到垃圾箱的时间
    */
    private Date msgDeleteTime;

    /**
    * 邮件读取时间
    */
    private Date msgReadTime;

    private static final long serialVersionUID = 1L;
}