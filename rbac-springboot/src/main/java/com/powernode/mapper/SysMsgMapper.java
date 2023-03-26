package com.powernode.mapper;

import com.powernode.domain.SysMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysMsgMapper {
    /**
     * delete by primary key
     * @param msgId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer msgId);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(SysMsg record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(SysMsg record);

    /**
     * select by primary key
     * @param msgId primary key
     * @return object by primary key
     */
    SysMsg selectByPrimaryKey(Integer msgId);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(SysMsg record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(SysMsg record);

    List<SysMsg> getByPage(@Param("params") Map params);

}
