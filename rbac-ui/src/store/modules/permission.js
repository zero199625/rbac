
/* 这里不需要再引入asyncRoutes了，因为 asyncRoutes动态路由 由后端返回的数据来定义 */
import {constantRoutes } from '@/router'

// 引入 sysMenuApi，获取 用来转化为动态路由的字符串
import sysMenuApi from "@/api/system/sysMenu"

// 引入 Layout组件，将其赋给一级菜单的component
import Layout from "@/layout/index"

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
/* 对路由进行过滤  routes就是传过来的路由数组 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  // 遍历路由数组
  routes.forEach(route => {
    // tmp是路由数组中的 每一个路由
    const tmp = { ...route }
   /* // 判断数组中的路由是否有权限
    if (hasPermission(roles, tmp)) {
      // 如果该路由有权限，再判断该路由的children是否存在
      if (tmp.children) {
        // 如果children存在，再对children进行过滤，判断children是否有权限
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      /!*
      *   将所有路由放到一个数组中，并返回
      *       最终返回的路由都已经 分配好了角色，角色分配好了权限：
      *         不再像原来那样拥有所有角色，且每个角色都被分配了所有权限
      *         而是该路由 被分配了多少角色就拥有多少角色，角色被分配的多少权限就拥有多少权限
      * *!/
      res.push(tmp)
    }*/

    // 判断数组中的JSON字符串的 component属性 是否为null
    if (tmp.component) {
      // 不为null，若 component属性值 为 Layout字符串，说明是一级菜单
      if (tmp.component ==='Layout') {
        // 将其 component属性 定义为 Layout组件
        tmp.component = Layout
      }else {
        // 不是一级菜单，将其 component属性 定义为 相应的组件       loadView()方法：将传入的字符串 以组件的形式返回
        tmp.component = loadView(tmp.component)
      }
    }
    // 再判断 是否存在children属性(对象) 和 children属性(对象)中是否有值
    if (tmp.children && tmp.children[0]) {
      // 如果都有，说明子菜单存在，给子菜单添加路由
      tmp.children = filterAsyncRoutes(tmp.children)
    }
    /*
    *   将JSON字符串中的菜单都添加了路由，然后返回
    * */
    res.push(tmp)
  })
  return res
}


//在我们接口提供的数据中，并不是完整的引入路径。如果提供了以 `@` 开头的完整路径，会报错。因此我们需要手动引入，就是上面代码中的 `loadView`
export const loadView = (view) => { // 路由懒加载
  return (resolve) => require([`@/views/${view}`], resolve)
}


const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  /* 根据角色 获取动态路由 */
  /*
  *   根据后端返回的结果，动态获取路由
  * */
  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      // 定义一个容器，作为动态路由
      let accessedRoutes
      /*/!* 判断角色是不是管理员 *!/
      if (roles.includes('admin')) {
        /!* 如果是管理员，分配所有的路由 *!/
        accessedRoutes = asyncRoutes || []
      } else {
     /!*
     *   如果不是管理员，对路由进行过滤，只分配用户所拥有的的路由
     *       最终返回的路由都已经 分配好了角色，角色分配好了权限：
     *         不再像原来那样拥有所有角色，且每个角色都被分配了所有权限
     *         而是该路由 被分配了多少角色就拥有多少角色，角色被分配的多少权限就拥有多少权限
     * *!/
        accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      }*/

      sysMenuApi.getRouters().then(rs=>{
        // 这里的accessedRoutes还只是分层的JSON字符串
        accessedRoutes = rs.data
        /*
        *   对accessedRoutes进行过滤操作，给其中的一级、二级菜单添加对应的路由
        *       将其中的一级菜单的component，定义为 Layout组件   如：  component: Layout
        *       将其中的二级菜单的component，定义为 对应的组件      如：  component: () => import('@/views/system/sysDictType/sys-dict-type-list')
        * */
        accessedRoutes = filterAsyncRoutes(accessedRoutes)
        /* 添加动态路由 */
        commit('SET_ROUTES', accessedRoutes)
        resolve(accessedRoutes)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
