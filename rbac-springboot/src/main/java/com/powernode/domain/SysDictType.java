package com.powernode.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 字典类型表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDictType implements Serializable {
    /**
    * 字典主键
    */
    private Long dictId;

    /**
    * 字典名称
    */
    private String dictName;

    /**
    * 字典类型
    */
    private String dictType;

    /**
    * 状态（0正常 1停用）
    */
    private Integer status;

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

    /**
    * 备注
    */
    private String remark;

    private static final long serialVersionUID = 1L;
}