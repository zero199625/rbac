package com.powernode.controller;


import com.powernode.commen.Page;
import com.powernode.commen.Result;
import com.powernode.domain.SysRole;
import com.powernode.domain.SysUser;
import com.powernode.enums.StateEnums;
import com.powernode.service.SysRoleMenuService;
import com.powernode.service.SysRoleService;
import com.powernode.service.SysUserRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Statement;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("sysRole")
public class SysRoleController extends BaseController{

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    /**
     *  分页查询角色
     * @param page
     * @return
     */
    @PostMapping("getByPage")
    public Result getByPage(@RequestBody Page page){  // 请求参数是JSON字符串
        // 请求参数是page(pageNum、pageSize、params)，返回参数也是page(list、totalCount、totalPage)
        page =  sysRoleService.getByPage(page);
        return new Result("角色查询成功",page);
    }


    /**
     *  添加角色
     * @param sysRole
     * @return
     */
    @PostMapping("save")
    public Result save(@RequestBody SysRole sysRole){  // 请求参数是JSON字符串
        sysRole.setCreateBy(getUserName());
        sysRole.setCreateTime(new Date());
        // 记住要将delFlag设为0   StateEnums.NOT_DELETED.getCode()=0
        sysRole.setDelFlag(StateEnums.NOT_DELETED.getCode());
        sysRoleService.insert(sysRole);
        return new Result("角色添加成功");
    }


    /**
     *  修改角色
     * @param sysRole
     * @return
     */
    @PutMapping("update")
    public Result update(@RequestBody SysRole sysRole){  // 请求参数是JSON字符串
        sysRole.setUpdateBy(getUserName());
        sysRole.setUpdateTime(new Date());
        // StateEnums.NOT_DELETED.getCode() = 0
        /*sysRole.setDelFlag(StateEnums.NOT_DELETED.getCode());*/
        sysRoleService.updateByPrimaryKeySelective(sysRole);
        return new Result("角色修改成功");
    }


    /**
     *  删除角色     如果有   逻辑上的删除(将delFlag改为1)
     * @param roleId
     * @return
     */
    @DeleteMapping("delete/{roleId}")
    public Result delete(@PathVariable Long roleId){
        // 根据角色ID roleId查询当前角色
        SysRole sysRole = sysRoleService.selectByPrimaryKey(roleId);
        // 将当前角色的delFlag改为1，逻辑上的删除   StateEnums.DELETED.getCode()=1
        sysRole.setDelFlag(StateEnums.DELETED.getCode());
        sysRoleService.updateByPrimaryKeySelective(sysRole);
        return new Result("角色删除成功");
    }


    /**
     *  根据角色ID查询角色
     * @param roleId
     * @return
     */
    @GetMapping("get/{roleId}")
    public Result get(@PathVariable Long roleId){
        SysRole sysRole = sysRoleService.selectByPrimaryKey(roleId);
        return new Result("查询成功",sysRole);
    }


    /**
     *  根据角色ID查询当前角色所拥有的菜单权限
     *      由于前端选中了一级或二级菜单，那该菜单下面所有子菜单都会被选中，
     *      而如果选中一个三级菜单，其所在菜单级别的的一级和二级菜单都会被选中，因此只需要查询三级菜单即可
     *        只查三级菜单权限(10001、10002...)，排除一级(1、2)、二级(101)
     *        将该角色的所拥有的的menuId中属于 一级或二级 查出来，然后使用 not in ，这样查出来的菜单权限就不包括一级和二级菜单权限了
     * @param roleId
     * @return
     */
    @GetMapping("getCurrentRoleHasMenuIdsByRoleId/{roleId}")
    public Result getCurrentRoleHasMenuIdsByRoleId(@PathVariable Long roleId){
        // 根据角色ID(roleId)查询其 所有菜单ID(menuId)
        List<Long> menuIds =  sysRoleService.selectMenuById(roleId);
        return new Result("查询成功",menuIds);
    }


    /**
     *  保存角色菜单之间的关联    先删除，再添加    根据 角色菜单表sys_role_menu 来进行添加
     *            前端通过勾选，来选择角色，选中的就保存，这是新增操作，如果之前勾选了的，现在不勾选了，其实是删除操作
     *            因此，保存用户和角色之间的关系 并不是 单纯的增加，而是 删除+增加
     *            如果用传统办法，还要一个个判断是否被删除了，再根据情况添加，效率太低
     *            效率最高的办法就是，将之前所有关系都删除，再添加选中的用户角色关系
     * @param roleId
     * @param menuIds
     * @return
     */
    @PostMapping("saveRoleMenu")
    public Result saveRoleMenu(Long roleId, Long[] menuIds){
        // 根据roleId删除 sys_role_menu 表中角色菜单关联
        sysRoleMenuService.deleteByPrimaryKey(roleId);
        // 根据 roleId 和 menuIds 在sys_role_menu表中添加角色菜单关联
        sysRoleMenuService.insertRoleMenuConnection(roleId,menuIds);
        return new Result("角色菜单关联保存成功");
    }


    /**
     *  根据 用户ID 查询用户已拥有的 所有角色ID
     * @param userId
     * @return
     */
    @GetMapping("getRoleIdsByUserId/{userId}")
    public Result getRoleIdsByUserId(@PathVariable Long userId){
        List<Long> roleIdList =  sysUserRoleService.selectAllRoleIdByUserId(userId);
        return new Result("查询成功",roleIdList);
    }


    /**
     *  查询所有角色，不分页
     * @return
     */
    @GetMapping("getAllRoles")
    public Result getAllRoles(){
        // 根据当前用户的 userId，查询所有的用户角色
        List<SysRole> roleList = sysRoleService.selectAllRoles();
        return new Result("查询成功",roleList);
    }


    /**
     *  保存用户和角色之间的关系   先删除，再添加    根据 userId 和 所有角色ID roleIds
     *      前端通过勾选，来选择角色，选中的就保存，这是新增操作，如果之前勾选了的，现在不勾选了，其实是删除操作
     *      因此，保存用户和角色之间的关系 并不是 单纯的增加，而是 删除+增加
     *      如果用传统办法，还要一个个判断是否被删除了，再根据情况添加，效率太低
     *      效率最高的办法就是，将之前所有关系都删除，再添加选中的用户角色关系
     * @param userId
     * @param roleIds
     * @return
     */
    @PostMapping("saveUserRoles")
    public Result saveUserRoles(Long userId, Long[] roleIds){
        // 删除所有用户和角色之间的关系   根据userId
        sysUserRoleService.deleteByPrimaryKey(userId);
        // 保存用户角色和角色之间的关系   根据userId和roleIds
        sysUserRoleService.insertUserRoleConnection(userId, roleIds);
        return new Result("保存成功");
    }



}






