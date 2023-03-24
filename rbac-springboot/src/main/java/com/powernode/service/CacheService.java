package com.powernode.service;

import com.powernode.domain.SysDictData;

import java.util.List;

public interface CacheService {


    void loadDictTypeData();


    List<SysDictData> getDictDataByDictType(String dictType);
}
