package com.powernode.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 用户信息表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUser implements Serializable {
    /**
    * 用户ID
    */
    private Long userId;

    /**
    * 部门编号
    */
    private Long deptId;

    /**
    * 用户账号
    */
    private String userName;

    /**
    * 用户昵称
    */
    private String nickName;

    /**
    * 用户类型（0超级用户 1系统用户）
    */
    private Integer userType;

    /**
    * 用户邮箱
    */
    private String email;

    /**
    * 手机号码
    */
    private String phone;

    /**
    * 用户性别（0男 1女 2未知）
    */
    private Integer sex;

    /**
    * 头像地址
    */
    private String avatar;

    /**
    * 密码
    */
    private String password;

    /**
    * 帐号状态（0正常 1停用）
    */
    private Integer status;

    /**
    * 删除标志（0存在 2删除）
    */
    private Integer delFlag;

    /**
    * 最后登录IP
    */
    private String loginIp;

    /**
    * 最后登录时间
    */
    private Date loginDate;

    /**
    * 创建者
    */
    private String createBy;

    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
    * 更新者
    */
    private String updateBy;

    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}