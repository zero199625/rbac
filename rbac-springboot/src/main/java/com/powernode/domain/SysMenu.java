package com.powernode.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 菜单权限表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysMenu implements Serializable {
    /**
    * 菜单ID
    */
    private Long menuId;

    /**
    * 菜单名称
    */
    private String menuName;

    /**
    * 父菜单ID
    */
    private Long parentId;

    /**
    * 显示顺序
    */
    private Integer orderNum;

    /**
    * 路由地址
    */
    private String path;

    /**
    * 组件路径
    */
    private String component;

    /**
    * 菜单类型（0目录 1菜单 2按钮）
    */
    private Integer menuType;

    /**
    * 菜单状态（0显示 1隐藏）
    */
    private Integer visible;

    /**
    * 菜单状态（0正常 1停用）
    */
    private Integer status;

    /**
    * 权限标识
    */
    private String perms;

    /**
    * 菜单图标
    */
    private String icon;

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