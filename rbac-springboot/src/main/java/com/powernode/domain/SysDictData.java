package com.powernode.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 字典数据表
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysDictData implements Serializable {
    /**
    * 字典编码
    */
    private Long dictCode;

    /**
    * 字典排序
    */
    private Integer dictSort;

    /**
    * 字典标签
    */
    private String dictLabel;

    /**
    * 字典键值
    */
    private Integer dictValue;

    /**
    * 字典类型
    */
    private String dictType;

    /**
    * 字典皮肤(success,info,warning,danger)
    */
    private String dictSkin;

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
    // timezone：设置时区，GMT-8表示东八区
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