package com.powernode.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
    * 角色信息表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRole implements Serializable {
    /**
    * 角色ID
    */
    private Long roleId;

    /**
    * 角色名称
    */
    private String roleName;

    /**
    * 显示顺序
    */
    private Integer roleSort;

    /**
    * 角色备注
    */
    private String remark;

    /**
    * 角色状态（0正常 1停用）
    */
    private Integer status;

    /**
    * 删除标志（0存在 2删除）
    */
    private Integer delFlag;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}