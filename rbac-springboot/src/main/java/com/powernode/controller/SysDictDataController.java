package com.powernode.controller;

import com.powernode.commen.Page;
import com.powernode.commen.Result;
import com.powernode.domain.SysDictData;
import com.powernode.service.CacheService;
import com.powernode.service.SysDictDataService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sysDictData")
public class SysDictDataController extends BaseController {

    @Resource
    private SysDictDataService sysDictDataService;

    @Resource
    private CacheService cacheService;

    /**
     *      分页查询字典数据     请求参数是JSON字符串
     *          分页查询的 请求参数类型 和 响应数据类型 都是Page类型
     *              Page中的pageNumber、pageSize和params(Map类型)是请求参数
     *                      totalCount、totalPage和list(List类型)是响应数据
     * @param page
     * @return
     */
    @PostMapping("/getByPage")
    public Result getByPage(@RequestBody Page page){  //参数是Page类型是JSON字符串
        page = sysDictDataService.getByPage(page);
        return new Result("字典数据分页查询成功",  page);
    }

    /**
     *      添加字典数据    请求数据类型是JSON字符串
     *          还需设置的参数有：createBy和createTime
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody SysDictData sysDictData){
        // getUserName()：获取当前用户的 名字
        sysDictData.setCreateBy(getUserName());
        sysDictData.setCreateTime(new Date());
        sysDictDataService.insert(sysDictData);
        return new Result("字典数据添加成功");
    }

    /**
     *      修改数据类型    请求参数是JSON字符串
     *          还需设置的参数：updateBy 和 updateTime
     * @param sysDictData
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody SysDictData sysDictData){
        sysDictData.setUpdateBy(getUserName());
        sysDictData.setUpdateTime(new Date());
        sysDictDataService.updateByPrimaryKey(sysDictData);
        return new Result("字典数据修改成功");
    }

    /**
     *      根据ID查询字典数据
     * @param dictCode
     * @return
     */
    @GetMapping("/get/{dictCode}")
    public Result get(@PathVariable Long dictCode){
        SysDictData sysDictData = sysDictDataService.selectByPrimaryKey(dictCode);
        return new Result("字典数据查询成功",sysDictData);
    }

    /**
     *      根据ID删除字典数据
     * @param dictCode
     * @return
     */
    @DeleteMapping("/delete/{dictCode}")
    public Result delete(@PathVariable Long dictCode){
        sysDictDataService.deleteByPrimaryKey(dictCode);
        return new Result("字典数据删除成功");
    }


    /**
     *      根据字典类型获取字典数据   从缓存中获取，如果缓存中没有，从数据库中获取
     *          注意：只有 根据字典类型获取字典数据 是从缓存中查找，分页查询等都不是
     *          注意：返回的是一个List集合
     * @param dictType
     * @return
     */
    @GetMapping("/getDictDataByDictType/{dictType}")
    public Result getDictDataByDictType(@PathVariable String dictType){
        List<SysDictData> sysDictDataList =  cacheService.getDictDataByDictType(dictType);
        return new Result("成功根据字典类型获取字典数据",sysDictDataList);
    }



}














