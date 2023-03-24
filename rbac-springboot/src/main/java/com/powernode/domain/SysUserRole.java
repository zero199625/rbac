package com.powernode.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 用户和角色关联表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRole implements Serializable {
    /**
    * 用户ID
    */
    private Long userId;

    /**
    * 角色ID
    */
    private Long roleId;

    private static final long serialVersionUID = 1L;
}