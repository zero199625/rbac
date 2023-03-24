package com.powernode.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 角色和菜单关联表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleMenu implements Serializable {
    /**
    * 角色ID
    */
    private Long roleId;

    /**
    * 菜单ID
    */
    private Long menuId;

    private static final long serialVersionUID = 1L;
}