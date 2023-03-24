package com.powernode.service.impl;

import com.powernode.domain.SysDictData;
import com.powernode.domain.SysDictType;
import com.powernode.enums.StateEnums;
import com.powernode.mapper.SysDictDataMapper;
import com.powernode.mapper.SysDictTypeMapper;
import com.powernode.service.CacheService;
import com.powernode.service.SysDictTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CacheServiceImpl implements CacheService {

    @Resource
    private SysDictDataMapper sysDictDataMapper;

    @Resource
    private SysDictTypeMapper sysDictTypeMapper;

    // Map集合 存放从数据库中获取的数据
    private static Map<String , List<SysDictData>> caches = new HashMap<>();

    /**
     *  将数据库中的数据存入缓存
     */
    @Override
    public void loadDictTypeData() {
        /**
         *   引入缓存
         *     字典数据 变化频率较低  每次获取字典数据 没有必要到数据库来查询  可以使用缓存来缓存字典数据
         *        如果客户端需要某个字典类型数据 后端提供一个接口 接收字典类型值 根据字典类型值到缓存查找即可
         *   刷新缓存   实际上就是将数据库字典数据查出来放入缓存
         *        将每个字典类型的字典记录存入一个特殊的数据结构中  后续使用时从这个数据结构取即可
         *           sys_user_sex---->List<SysDictData>
         *           sys_notice_type--->List<SysDictData>
         *           sys_login_status--->List<SysDictData>
         *          这个数据结构为 Map<String,List<SysDictData>>
         *
         */
        // 获取所有的字典类型  得是正常状态码的字典数据，所以要传入  StateEnums.ENABLED.getCode()  = 0
        List<SysDictType> sysDictTypeList =  sysDictTypeMapper.getAllDictType(StateEnums.ENABLED.getCode());
        // 遍历字典类型集合
        for (SysDictType sysDictType : sysDictTypeList) {
            // 根据字典类型，获取字典数据，由于一个字典类型可能有多条数据，因此使用集合
            List<SysDictData> sysDictDataList = sysDictDataMapper.getDictDataByDictType(sysDictType.getDictType());
            // 将获取到的字典数据集合放入Map集合
            caches.put(sysDictType.getDictType(),sysDictDataList);
        }


    }

    /**
     *  根据字典类型获取字典数据   从缓存中获取，如果没有就从数据库中获取，获取完之后将数据存入缓存
     * @param dictType
     * @return
     */
    @Override
    public List<SysDictData> getDictDataByDictType(String dictType) {
        /**
         * 根据字典类型获取字典数据
         *   如果caches 存在 直接获取即可
         *   如果caches 不存在(添加 修改 删除 在没有执行缓存同步时 可能caches不存在对应记录)
         *  查找顺序
         *     先从缓存查
         *        缓存存在 直接返回
         *        缓存不存在
         *     到数据库查找
         *         找到对应记录后 返回记录  同时将查到的记录放入caches
         */
        // 根据 字典类型 从数据库中查找 字典数据
        List<SysDictData> sysDictDataList = caches.get(dictType);
        // 如果数据存在缓存中
        if (!CollectionUtils.isEmpty(sysDictDataList)) { // 注意空指针异常  !sysDictDataList.isEmpty()
            // 将查询到的结果返回
            return sysDictDataList;
        }
        // 如果缓存中不存在，从数据库中查找
        sysDictDataList = sysDictDataMapper.getDictDataByDictType(dictType);
        // 将查询到的数据放入缓存
        caches.put(dictType,sysDictDataList);
        return sysDictDataList;
    }
}
