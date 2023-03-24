package com.powernode.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.powernode.commen.Result;
import com.powernode.constants.CoreConstant;
import com.powernode.domain.SysUser;
import com.powernode.enums.ResultEnums;
import com.powernode.enums.StateEnums;
import com.powernode.vo.RouterVo;
import com.powernode.vo.SysMenuVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.powernode.domain.SysMenu;
import com.powernode.mapper.SysMenuMapper;
import com.powernode.service.SysMenuService;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService{

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public int deleteByPrimaryKey(Long menuId) {
        return sysMenuMapper.deleteByPrimaryKey(menuId);
    }

    @Override
    public int insert(SysMenu record) {
        return sysMenuMapper.insert(record);
    }

    @Override
    public int insertSelective(SysMenu record) {
        return sysMenuMapper.insertSelective(record);
    }

    @Override
    public SysMenu selectByPrimaryKey(Long menuId) {
        return sysMenuMapper.selectByPrimaryKey(menuId);
    }

    @Override
    public int updateByPrimaryKeySelective(SysMenu record) {
        return sysMenuMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysMenu record) {
        return sysMenuMapper.updateByPrimaryKey(record);
    }

    /**
     *  查询所有菜单权限
     *      如果是管理员，查询所有   如果是普通用户，根据其权限查
     * @param sysMenu
     * @return
     */
    public List<SysMenu> selectAllMenu(SysMenu sysMenu, SysUser sysUser) {
        List<SysMenu> sysMenuList=null;
        // 如果是管理员      管理员的 userType=0  StateEnums.USER_TYPE_ADMIN.getCode()=0
        if (sysUser.getUserType().equals(StateEnums.USER_TYPE_ADMIN.getCode())) {
            sysMenuList =  sysMenuMapper.selectAllMenu(sysMenu);
        }else {
            // 如果是普通用户   普通用户的 userType=1
            sysMenuList = sysMenuMapper.selectMenuByPerms(sysMenu,sysUser);
        }
        return sysMenuList;
    }

    /**
     *  添加菜单权限    menuType=0：一级菜单   menuType=1：二级菜单  menuType=2：三级菜单
     *      根据菜单级别(menu_type=0/1/2)和 上级菜单ID(parent_id)
     * @param sysMenu
     */
    @Override
    public int insertByMenuTypeAndParentId(SysMenu sysMenu) {
        // 获取 菜单级别menu_type 和 上级菜单ID(parent_id)
        Integer menuType = sysMenu.getMenuType();
        Long parentId = sysMenu.getParentId();
        /*
        *   如果出现多个用户同时添加菜单权限，会出现查询到的max相同，添加的主键menuId相同的情况，这时数据库会报错，导致程序无法走下去
        *   解决方法：1.使用synchronized对方法进行上锁，但是这样效率会将低
        *            2.while(true) + try-catch
        * */
        int num = 0;
        while (true){ // while的作用：异常被catch到之后，再走一遍添加流程，这样就算有多条数据，也可以以此执行添加操作
            try { // try-catch的作用：由于添加同一条数据的时候已报错，程序无法进行下去，try-catch就算让程序可以继续进行下去
                // 根据这两个参数查询 该菜单级别中 menuId 的最大值
                Long max =  sysMenuMapper.selectMaxByMenuTypeAndParentId(menuType,parentId);
                // 判断最大值是否存在
                if (max!=null) { // 如果存在
                    // 待添加的menuId = menuId最大值+1
                    sysMenu.setMenuId(max+1);
                }else { // 如果不存在
                    // 判断是不是一级菜单   StateEnums.MENU_M.getCode() =0
                    if (menuType.equals(StateEnums.MENU_M.getCode())) {
                        // 如果是，menuId=1
                        sysMenu.setMenuId(1L);
                    }else {
                        // 如果不是，menuId= 上级菜单menuId拼接01，也就是 本级菜单parentId拼接01
                        sysMenu.setMenuId(Long.parseLong(parentId+"01"));
                    }
                }
                // 数据库的主键值唯一，如果添加同一条数据，数据库会报异常，而不是添加同一条记录
                num = sysMenuMapper.insert(sysMenu);
                break;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        // 4.执行添加操作
        return num;
    }


    @Override
    public Long selectByParentId(Long menuId) {
        return sysMenuMapper.selectByParentId(menuId);
    }



    /**
     *  只查询目录和菜单    如果是管理员，查询所有   如果是普通用户，根据其用户ID查询
     * @return
     */
    @Override
    public List<SysMenu> selectMenuByMenuAndCatalogue(SysUser sysUser) {
        List<SysMenu> result = null;
        // 如果是管理员
        if (sysUser.getUserType().equals(StateEnums.USER_TYPE_ADMIN.getCode())) {
            // 查询所有目录和菜单
            result = sysMenuMapper.selectMenuByMenuAndCatalogue();
        }else {
            // 如果是普通用户，根据用户ID查询
            result = sysMenuMapper.selectMCByUserId(sysUser.getUserId());
        }
        return result;
    }



    /**
     *  获取动态路由    只有一级、二级菜单才有路由，因此只获取 目录和菜单
     *      List<SysMenu>  -->  List<SysMenuVo>  ->  List<SysMenuVo>
     * @return
     */
    @Override
    public List<RouterVo> getRouters(SysUser sysUser) {
        /*
        *   1.获取List<SysMenu>     判断用户类型，只查询目录和菜单
        *       管理员：查询所有
        *       普通用户：根据其扮演的角色，查询这些角色所拥有的的菜单
        *   2.根据List<SysMenu>    构建   List<SysMenuVo>
        *       先获取一级菜单(目录)     menuType=0(系统管理、日志管理)
        *       将二级菜单添加为，对应的一级菜单的子菜单  children属性
        *       最终得到一个 分层的List<SysMenuVo>
                     集合中的每个元素都是一个对象(一级菜单/目录)，对象的children属性是对应的 二级菜单的集合
        *   3.根据分层的List<SysMenuVo>  构建   路由集合List<SysRouterVo>
        * */

        /*
        *   1.获取List<SysMenu> ：根据用户类型，查询当前用户的 一级、二级菜单(目录和菜单)，
        * */
        List<SysMenu> sysMenuList = selectMenuByMenuAndCatalogue(sysUser);
        /*
        *   2.根据查到的List<SysMenu> 构建 List<SysMenuVo>
        * */
        List<SysMenuVo> sysMenuVoList = new ArrayList<>();
        // (1)将 List<SysMenu> 转化为 List<SysMenuVo>
        sysMenuList.forEach(sysMenu -> {
            SysMenuVo sysMenuVo = new SysMenuVo();
            /*
            *   BeanUtils.copyProperties(source, target)：根据对应的属性，将source对象复制给target对象
            * */
            BeanUtils.copyProperties(sysMenu,sysMenuVo);
            sysMenuVoList.add(sysMenuVo);
            System.out.println(sysMenuVoList);
        });
        // (2)获取一级菜单集合(目录集合，即menuType=0的菜单集合)
        List<SysMenuVo> firstSysMenuVoList = new ArrayList<>();
        sysMenuVoList.forEach(sysMenuVo->{
            // 取出firstSysMenuVoList集合中，menuType=0的SysMenuVo对象
            if (sysMenuVo.getMenuType().equals(StateEnums.MENU_M.getCode())) {  // StateEnums.MENU_M.getCode() = 0
                firstSysMenuVoList.add(sysMenuVo);
            }
        });
        // (3)将二级菜单添加到，对应的一级菜单的children属性中   最终得到一个分层的List<SysMenuVo> firstSysMenuVoList
        sysMenuVoList.forEach(childSysMenuVo -> {
            firstSysMenuVoList.forEach(parentSysMenuVo->{
                // 如果某 菜单A 的parentId是 菜单B 的menuId，则该 菜单A 为 菜单B 的子菜单
                if (childSysMenuVo.getParentId().equals(parentSysMenuVo.getMenuId())) {
                    parentSysMenuVo.getChildren().add(childSysMenuVo);
                }
            });
        });
        /*
        *   3.根据分层的List<SysMenuVo>  构建   路由集合List<SysRouterVo>
        * */
        List<RouterVo> routerVoList =  buildRouterVoList(firstSysMenuVoList);
        return routerVoList;
    }

    /**
     *  将分层的List<SysMenuVo> 转化为 路由集合List<SysRouterVo>
     *      一级菜单和二级菜单的 path、component、redirect、alwaysShow 这几个属性有些不同     (menuType)
     * @param firstSysMenuVoList
     * @return
     */
    private List<RouterVo> buildRouterVoList(List<SysMenuVo> firstSysMenuVoList) {
        // 用于返回的 List<RouterVo>
        List<RouterVo> routerVoList = new ArrayList<>();
        // 遍历 List<SysMenuVo>，将其中的每一个 sysMenuVo 都转化为 routerVo
        for (SysMenuVo sysMenuVo : firstSysMenuVoList) {
            RouterVo routerVo = new RouterVo();
            /*
            *   路由名称name        对应 SysMenuVo类 中的 path 属性       name = sysMenuVo.getPath()
            * */
            routerVo.setName(sysMenuVo.getPath());
            /*
            *   路由地址 path        由 SysMenuVo类 中的 menuType属性 决定     一级菜单的 path 比二级菜单多了一个 /
            *           menuType=0 ->  path = "/" + sysMenuVo.getPath()        目录
            *           menuType!=0 -> path=sysMenuVo.getPath()                二级菜单
            *   StateEnums.MENU_M.getCode() = 0     CoreConstant.URL_SPLIT = "/"
            * */
            routerVo.setPath(sysMenuVo.getMenuType().equals(StateEnums.MENU_M.getCode())?(CoreConstant.URL_SPLIT +sysMenuVo.getPath()):sysMenuVo.getPath());
            /*
            *   组件地址component     由 SysMenuVo类 中的 menuType属性 决定
            *           menuType=0  -> component=Layout                      目录
            *           menuType!=0 -> component=sysMenuVo.getComponent()   二级菜单     例：system/role/sys-role-list
            *   实际上，在路由文件中，component并不是属性，而是一个方法，如：component: () => import('@/views/system/sysDictType/sys-dict-type-list')
            * */
            routerVo.setComponent(sysMenuVo.getMenuType().equals(StateEnums.MENU_M.getCode())?"Layout":sysMenuVo.getComponent());
            /*
            *   是否重定向redirect    由 SysMenuVo类 中的 menuType属性 决定 ，只有一级菜单才有redirect，二级菜单没有
            *           menuType=0 ->  redirect = "noRedirect"
            *           menuType!=0 -> redirect = null
            *   CoreConstant.NO_REDIRECT = "noRedirect"
             * */
            routerVo.setRedirect(sysMenuVo.getMenuType().equals(StateEnums.MENU_M.getCode())?CoreConstant.NO_REDIRECT:null);
            /*
            *   是否隐藏hidden        由 SysMenuVo类 中的 visible属性 决定
            *           sysMenuVo.getVisible()=0  ->  hidden = false      显示
            *           sysMenuVo.getVisible()=1  ->  hidden = true       隐藏
            * */
            routerVo.setHidden(sysMenuVo.getVisible()==0?false:true);
            /*
            *   是否永远展示alwaysShow   由SysMenuVo类 中的 menuType属性决定   如果为true，即使子菜单只有一个，也会展示层级关系
            *           sysMenuVo.getMenuType()=0  ->  alwaysShow = true
            *           sysMenuVo.getMenuType()=1  ->  alwaysShow = false
            *   只有 menuType为0 的目录才会显示子菜单 ，menuType为1的没有子菜单
            * */
            routerVo.setAlwaysShow(sysMenuVo.getMenuType().equals(StateEnums.MENU_M)?true:false);
            /*
            *   路由的元信息meta    包含 title 和 icon 两个属性
            *       title：菜单属性  对应 SysMenuVo类 中的 menuName 属性     title = sysMenuVo.getMenuName()
            *       icon ：图标属性  对应 SysMenuVo类 中的 icon 属性         icon = sysMenuVo.getIcon()
            * */
            routerVo.setMeta(new RouterVo.MetaVo(sysMenuVo.getMenuName(),sysMenuVo.getIcon()));
            /*
            *   子菜单的路由children    routerVo中的children属性由sysMenuVo中的children属性决定
            *       利用递归的原理，将子菜单的 List<SysMenuVo> 转化为 List<SysRouterVo>
            * */
            /*
            *   如果 List<SysMenuVo> 中的元素(sysMenuVo)的children(List<SysMenuVo>)不为null，将children转化为List<RouterVo>
            *   并将得到的结果(List<RouterVo>)设置为 routerVoList 中元素(routerVo)的children
            * */
            List<SysMenuVo> children = sysMenuVo.getChildren();
            if (CollectionUtil.isNotEmpty(children)) {
                routerVo.setChildren(buildRouterVoList(children));
            }
            /*
            *   List<SysRouterVo> 构建完成
            * */
            routerVoList.add(routerVo);
        }
        return routerVoList;
    }
}
