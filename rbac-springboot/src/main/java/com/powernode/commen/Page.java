package com.powernode.commen;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页类

 */
@Data
public class Page<T> implements Serializable {

    /* 请求参数 */
    /**
     * 当前页数
     */
    private Integer pageNumber=1;

    /**
     * 每页显示条数
     */
    private Integer pageSize = 10;

    /**
     * 接收传参        将前端传入的多个请求参数(dictName、status)放到 map 集合中，这样避免了设立太多属性
     */
    private Map<String, Object> params = new HashMap<>(8);


    /* 响应参数 */
    /**
     * 总条数
     */
    private Long totalCount;

    /**
     * 总页数
     */
    private Integer totalPage;

    /**
     * 数据
     */
    private List<T> list;




    /**
     * 在获取 总页数 的时候，计算总条数   这样只要获取总条数，自然就获取了总页数
     *
     * @param totalCount
     */
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
        this.totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
    }

}