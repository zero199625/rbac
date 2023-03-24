package com.powernode.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
    * 贴吧表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysTopic implements Serializable {
    /**
    * 帖子编号
    */
    private Integer topicId;

    /**
    * 标题
    */
    private String title;

    /**
    * 内容
    */
    private String content;

    /**
    * 作者
    */
    private String author;

    /**
    * 邮箱
    */
    private String email;

    /**
    * 发帖时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    /**
    * 点击量
    */
    private Integer clickAmount;

    private static final long serialVersionUID = 1L;
}