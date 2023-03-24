package com.powernode.service.impl;

import com.github.pagehelper.PageHelper;
import com.powernode.commen.Page;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.powernode.domain.SysDictData;
import com.powernode.mapper.SysDictDataMapper;
import com.powernode.service.SysDictDataService;

import java.util.List;

@Service
public class SysDictDataServiceImpl implements SysDictDataService{

    @Resource
    private SysDictDataMapper sysDictDataMapper;

    @Override
    public int deleteByPrimaryKey(Long dictCode) {
        return sysDictDataMapper.deleteByPrimaryKey(dictCode);
    }

    @Override
    public int insert(SysDictData record) {
        return sysDictDataMapper.insert(record);
    }

    @Override
    public int insertSelective(SysDictData record) {
        return sysDictDataMapper.insertSelective(record);
    }

    @Override
    public SysDictData selectByPrimaryKey(Long dictCode) {
        return sysDictDataMapper.selectByPrimaryKey(dictCode);
    }

    @Override
    public int updateByPrimaryKeySelective(SysDictData record) {
        return sysDictDataMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysDictData record) {
        return sysDictDataMapper.updateByPrimaryKey(record);
    }

    /**
     *  分页查询字典数据
     * @param page
     * @return
     */
    @Override
    public Page getByPage(Page page) {
        // 开启分页查询   返回的数据都放在了pageHelper中
        com.github.pagehelper.Page<Object> pageHelper = PageHelper.startPage(page.getPageNumber(), page.getPageSize());
        // 执行分页查询
        sysDictDataMapper.getByPage(page.getParams());
        page.setList(pageHelper.getResult());
        page.setTotalCount(pageHelper.getTotal());
        return page;
    }

    /**
     *  根据字典类型获取字典数据
     * @param dictType
     * @return
     */
    @Override
    public List<SysDictData> getDictDataByDictType(String dictType) {
        List<SysDictData> sysDictDataList =  sysDictDataMapper.getDictDataByDictType(dictType);
        return sysDictDataList;
    }

}
