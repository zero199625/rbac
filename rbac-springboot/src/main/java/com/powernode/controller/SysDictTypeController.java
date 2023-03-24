package com.powernode.controller;

import com.powernode.commen.Page;
import com.powernode.commen.Result;
import com.powernode.domain.SysDictType;
import com.powernode.enums.StateEnums;
import com.powernode.service.SysDictTypeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/*
 *   字典类型controller类
 * */

@RestController
@RequestMapping("/sysDictType")
public class SysDictTypeController extends BaseController {

    @Resource
    private SysDictTypeService sysDictTypeService;

    /**
     *      分页查询字典类型数据  请求参数类型是JSON字符串
     *                            查询参数为：pageNum、pageSize、dictName、dictType、status
     *                            Page类中没有dictName、status和 dictType属性，可以用Map集合 params 来接收这两个参数
     *      前端传参：
     *      {
     *     "pageNumber": 1,
     *     "pageSize": 10,,
     *     "params": {
     *         "dictName": "用户性别",          //字典类型名称
     *         "dictType":"sys_user_sex",      //字段类型
     *         "status": 0                     //字典类型状态
     *     }
     *      }
     * @param page
     * @return
     */
    @PostMapping("/getByPage")
    public Result getByPage(@RequestBody Page page){ // 由于请求参数是JSON字符串，所以这里得用@RequestBody
        page =  sysDictTypeService.selectSysDictTypeByPage(page);
        System.out.println(page);
        return new Result("分页查询字典类型成功",page);
    }

    /**
     *      添加字典类型       请求参数类型是JSON字符串
     *      只传了dictName、dictType、remark、status这4个参数，还有createBy和createTime需要设置
     * @param sysDictType
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody SysDictType sysDictType){
        // 1.设置添加用户
        sysDictType.setCreateBy(getUserName());
        // 2.设置添加事件
        sysDictType.setCreateTime(new Date());
        /*
        *   字典名称不能重复，可以根据字典名称到数据库中进行查询
        *       存在     不添加
        *       不存在   添加
        * */

        // 3.添加字典类型
        sysDictTypeService.insert(sysDictType);
        return new Result("字典添加成功");
    }

    /**
     *      根据ID(dicId)查询字典类型
     * @param dictId
     * @return
     */
    @GetMapping("/get/{dictId}")
    public Result get(@PathVariable Long dictId){  //注意这里的dicId是Long类型的
        SysDictType sysDictType = sysDictTypeService.selectByPrimaryKey(dictId);
        return new Result("字典类型查询成功",sysDictType);

    }

    /**
     *      根据ID删除字典类型
     * @param dictId
     * @return
     */
    @DeleteMapping("/delete/{dictId}")
    public Result delete(@PathVariable Long dictId){
        sysDictTypeService.deleteByPrimaryKey(dictId);
        return new Result("字典类型删除成功");
    }

    /**
     *      修改字典类型      请求参数是JSON字符串
     *      只传了dictName、dictType、remark、status、dictId，还要传 updateBy和 updateTime
     * @param sysDictType
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody SysDictType sysDictType){
        // 设置更新用户
        sysDictType.setUpdateBy(getUserName());
        // 设置更新时间
        sysDictType.setUpdateTime(new Date());
        /*
        *   字典名称不能重复，可以根据字典名称到数据库中进行查找
        *       存在： 1.是自己  除了名字不改，其他的都改
        *              2.不是自己  名字重复
        *       不存在：更新
        * */
        sysDictTypeService.updateByPrimaryKey(sysDictType);
        return new Result("字典类型修改成功");
    }

    /**
     *      获取所有字典类型(不分页)         status为0的字典类型
     * @return
     */
    @GetMapping("/getAllDictType")
    public Result getAllDictType(){
        List<SysDictType> sysDictTypeList =  sysDictTypeService.getAllDictType();
        return new Result("成功获取所有字典类型",sysDictTypeList);
    }


    /**
     *   同步字典数据到缓存    将数据库中的数据存入缓存
     *      原理：该方法可以将数据库中已经查过一遍的数据，缓存到Map集合中，下次再查找的时候，就直接到Map集合中去查找，不用再去数据库查找了
     * @return
     */
    @GetMapping("/refreshCache")
    public Result refreshCache(){
        // 缓存数据
        sysDictTypeService.refreshCache();
        return new Result("同步缓存成功");
    }









}
