package com.powernode.controller;

import com.powernode.commen.Result;
import com.powernode.domain.SysDept;
import com.powernode.enums.StateEnums;
import com.powernode.service.SysDeptService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sysDept")
public class SysDeptController extends BaseController{

    @Resource
    private SysDeptService sysDeptService;

    /**
     *  查询所有部门
     * @param sysDept
     * @return
     */
    @GetMapping("/listDept")
    public Result listDept(SysDept sysDept){
        List<SysDept> sysDeptList = sysDeptService.getAllDept(sysDept);
        return new Result("所有部门查询成功",sysDeptList);
    }

    /**
     *  添加部门
     * @param sysDept
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody SysDept sysDept){ // 请求参数是JSON字符串，参数会自动给到sysDept对象
        sysDept.setDelFlag(StateEnums.ENABLED.getCode());
        sysDept.setCreateBy(getUserName());
        sysDept.setCreateTime(new Date());
        sysDeptService.insert(sysDept);
        return new Result("部门添加成功");
    }

    /**
     *  修改部门
     * @param sysDept
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody SysDept sysDept){ // 请求参数是JSON字符串
        sysDept.setUpdateBy(getUserName());
        sysDept.setUpdateTime(new Date());
        sysDeptService.updateByPrimaryKey(sysDept);
        return new Result("部门修改成功");
    }

    /**
     *  删除部门   如果该部门有子部门，那么就不能删除，且删除也不是真的删除数据库中的数据，而是逻辑上的删除(将该条数据修改为停用)
     * @param deptId
     * @return
     */
    @DeleteMapping("/delete/{deptId}")
    public Result delete(@PathVariable Long deptId){ //deptId是Long类型的
        // 通过parentId查询，查询出来的都是其子部门
        Long count =  sysDeptService.selectDeptByParentId(deptId);
        // 如果有，不能删除
        if (count!=0) {
            return new Result("该部门还有子部门，不能删除！！");
        }
        // 如果没有，将该条数据的delFlag修改为1
        // 1.根据deptId查询该条数据
        SysDept sysDept = sysDeptService.selectByPrimaryKey(deptId);
        // 2.将该条数据额delFlag改为1，表示已删除   StateEnums.DELETED.getCode()=1
        sysDept.setDelFlag(StateEnums.DELETED.getCode());
        // 3.更新该条数据    也就是说，删除数据在数据库中其实是更新数据
        sysDeptService.updateByPrimaryKey(sysDept);
        return new Result("部门删除成功");
    }

    /**
     *  根据ID查询部门
     * @return
     */
    @GetMapping("/get/{deptId}")
    public Result get(@PathVariable Long deptId){
        SysDept sysDept = sysDeptService.selectByPrimaryKey(deptId);
        return new Result("根据ID查询部门成功",sysDept);
    }
}


























