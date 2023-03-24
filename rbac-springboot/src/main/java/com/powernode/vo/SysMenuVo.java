package com.powernode.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *      菜单类的魔改类   比菜单类多了一个 children属性(List集合)
 */
@Data
public class SysMenuVo implements Serializable {
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
     * 子节点        和 SysMenuVo类 不同的属性，用来存储 子菜单的数据
     */
    private List<SysMenuVo> children=new ArrayList<>();

    private static final long serialVersionUID = 1L;

}