package com.powernode.controller;


import com.powernode.commen.Result;
import com.powernode.domain.SysMenu;
import com.powernode.domain.SysUser;
import com.powernode.enums.ResultEnums;
import com.powernode.service.SysMenuService;
import com.powernode.vo.RouterVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sysMenu")
public class SysMenuController extends BaseController{

    @Resource
    private SysMenuService sysMenuService;

    /**
     *  查询所有菜单权限
     *      如果是管理员，查询所有   如果是普通用户，根据其权限查
     * @param sysMenu
     * @return
     */
    @GetMapping("/listMenu")
    public Result listMenu(SysMenu sysMenu){
        // 是不是管理员，通过userName进行判断，如果不是管理员，需要通过user_id来查询，这两个参数sysUser对象都有，所有将用户对象作为参数传入
        List<SysMenu> sysMenuList =  sysMenuService.selectAllMenu(sysMenu,getCurrentUser());
        return new Result("成功查询所有菜单权限",sysMenuList);
    }

    /**
     *  添加菜单权限      会根据菜单级别(menu_type=0/1/2)和 上级菜单ID(parent_id) 进行添加
     * @param sysMenu
     * @return
     */
    @PostMapping("/save")
    public Result save(@RequestBody SysMenu sysMenu){  //请求参数是JSON字符串，最终映射在SysMenu对象上
        sysMenu.setCreateBy(getUserName());
        sysMenu.setCreateTime(new Date());
        // 根据菜单级别(menu_type=0/1/2)和 上级菜单ID(parent_id) 进行添加
        sysMenuService.insertByMenuTypeAndParentId(sysMenu);
        return new Result("成功添加菜单权限");
    }


    /**
     *  修改菜单权限
     * @param sysMenu
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody SysMenu sysMenu){  //请求参数是JSON字符串
        sysMenu.setUpdateBy(getUserName());
        sysMenu.setUpdateTime(new Date());
        /*
        *   updateByPrimaryKeySelective()：会先判断参数是否有传入(判空处理)，如果未传入，则原参数不变
        *   updateByPrimaryKey()：如果参数未传入，则该参数为空
        * */
        sysMenuService.updateByPrimaryKeySelective(sysMenu);
        return new Result("成功修改菜单权限");
    }

    /**
     *  删除菜单权限    如果有子菜单，则不能删除   如果没有，则删除(真正意义上的删除)
     * @param menuId
     * @return
     */
    @DeleteMapping("/delete/{menuId}")
    public Result delete(@PathVariable Long menuId){
        // 根据parentId查询菜单数据，判断是否查到了数据
         Long count =  sysMenuService.selectByParentId(menuId);
        // 查到数据，说明有子菜单，不能删除
        if (count!=0) {
            return new Result(ResultEnums.ERROR.getCode(),"该菜单下有子菜单，不能删除");
        }
        sysMenuService.deleteByPrimaryKey(menuId);
        return new Result("成功删除菜单权限");
    }

    /**
     *  根据菜单ID查询菜单权限
     * @param menuId
     * @return
     */
    @GetMapping("/get/{menuId}")
    public Result get(@PathVariable Long menuId){
        SysMenu sysMenu = sysMenuService.selectByPrimaryKey(menuId);
        return new Result("查询成功",sysMenu);
    }


    /**
     *  只查询菜单和目录        menuId=1、2、100、101、102、103、104、105、201、202
     *                    或者 parentId=0、1、2
     *                    或者 menu_type=0、1   √
     * @return
     */
    @GetMapping("/listMenuMC")
    public Result listMenuMC(){
        List<SysMenu> sysMenuList =  sysMenuService.selectMenuByMenuAndCatalogue(getCurrentUser());
        return new Result("成功查询菜单和目录",sysMenuList);
    }


    /**
     *  获取动态路由   返回的是多个 路由视图类RouterVo 组成的List集合   List<SysMenuVo>
     * @return
     */
    @GetMapping("getRouters")
    public Result<List<RouterVo>> getRouters(){
        List<RouterVo> routerVoList =  sysMenuService.getRouters(getCurrentUser());
        return new Result<>("动态路由获取成功",routerVoList);
    }

}


















