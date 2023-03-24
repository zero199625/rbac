package com.powernode.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 路由视图类    配置动态路由 需要什么属性，此类中就添加什么属性
 *      作为动态路由的 响应数据 返回给前端，其中包含了设置动态路由所需要的所有数据
 */
@Data
public class RouterVo implements Serializable {

    /**
     * 路由名称            对应 SysMenuVo类 中的 menuName 属性     name = sysMenuVo.getMenuName()
     */
    private String name;

    /**
     * 路由地址    由 SysMenuVo类 中的 menuType属性 决定     一级菜单的 path 比二级菜单多了一个 /
     *          menuType=0 ->  path = "/" + sysMenuVo.getPath()      目录
     *          menuType!=0 -> path=sysMenuVo.getPath()              二级菜单
     */
    private String  path;

    /**
     * 组件地址      由 SysMenuVo类 中的 menuType属性 决定
     *          menuType=0 -> component=Layout                      目录
     *          menuType!=0 -> component=SysMenuVo.getComponent()   二级菜单     例：system/role/sys-role-list
     */
    private String component;

    /**
     * 当设置为 noRedirect 的时候，点击该路由不会发生跳转
     *          由 SysMenuVo类 中的 menuType属性 决定 ，只有一级菜单才有redirect，二级菜单没有
     *          menuType=0 ->  redirect = "noRedirect"         点击目录不会跳出页面或组件
     *          menuType!=0 -> redirect = null                 点击二级菜单，会跳出对应的组件
     */
    private String redirect;

    /**
     * 是否隐藏      由 SysMenuVo类 中的 visible属性 决定
     *              sysMenuVo.getVisible()=0  ->  hidden = false
     *              sysMenuVo.getVisible()=1  ->  hidden = true
     */
    private boolean hidden;

    /**
     * 是否永远展示。如果为true，即使子菜单只有一个，也会展示层级关系
     *     alwaysShow属性 由SysMenuVo类 中的 menuType属性决定
     *          sysMenuVo.getMenuType()=0  ->  alwaysShow = true
     *          sysMenuVo.getMenuType()=1  ->  alwaysShow = false
     *     即：只有 menuType为0 的目录才会显示子菜单 ，menuType为1的没有子菜单
     */
    private boolean alwaysShow;

    /**
     * 子菜单     用来存储子组件的数据 ，由于 子组件的属性 和 父组件的属性 类型一样，因此集合的泛型还是SysMenuVo
     */
    private List<RouterVo> children=new ArrayList<>(0);

    /**
     * 路由的元信息   包含 title 和 icon 两个属性
     */
    private MetaVo meta;

    /**
     * 路由元信息     内部类
     */
    @Data
    public static class MetaVo implements Serializable {
        /**
         * 菜单名称            对应 SysMenuVo类 中的 menuName 属性     title = sysMenuVo.getMenuName()
         */
        private String title;

        /**
         * 菜单图标            对应 SysMenuVo类 中的 icon 属性         icon = sysMenuVo.getIcon()
         */
        private String icon;

        public MetaVo(String title, String icon) {
            this.title = title;
            this.icon = icon;
        }
    }
}